const router = require("express").Router();
const User = require("./../model/User");
const BusinessRequest = require("./../model/BusinessRequest");
const Message = require("./../model/Message");
const moment = require("moment");
const admin = require("firebase-admin");
const nodemailer = require("nodemailer");

// GET all business requests
router.get("/posts", async (req, res) => {
  try {
    // Find all business requests that are only in progress
    const allRequests = await BusinessRequest.find({
      status: "in progression",
    });

    if (!allRequests || allRequests.length === 0) {
      return res.status(404).json({ message: "No business requests found" });
    }

    res.status(200).json(allRequests);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

// GET a specific post by ID
router.get("/posts/:id", async (req, res) => {
  try {
    const postId = req.params.id;

    // Find the post by its ID
    const post = await BusinessRequest.findById(postId);

    if (!post) {
      return res.status(404).json({ message: "Business Request not found" });
    }

    res.status(200).json(post);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

// PUT update status of a specific post by ID

router.put("/posts/:id", async (req, res) => {
  try {
    const postId = req.params.id;
    console.log("yess");
    const updatedPost = await BusinessRequest.findByIdAndUpdate(
      postId,
      { status: "completed" },
      { new: true }
    );

    console.log(updatedPost);

    if (!updatedPost) {
      return res.status(404).json({ message: "Post not found" });
    }

    const clientUser = await User.findById(updatedPost.userId);

    if (!clientUser) {
      return res.status(404).json({ message: "Client user not found" });
    }

    // Save notification to Firebase Realtime Database
    const notificationRef = admin.database().ref("notifications");
    const newNotificationRef = notificationRef.push();

    const notificationData = {
      userId: clientUser._id,
      title: "Business Request Completed",
      body: "Your business request has been completed. You can download the PDF now.",
      timestamp: admin.database.ServerValue.TIMESTAMP,
    };

    //send an email
    const transporter = nodemailer.createTransport({
      service: "gmail",
      auth: {
        user: process.env.EMAIL_SECRET,
        pass: process.env.PASSWORD_SECRET,
      },
    });
    try {
      await transporter.sendMail({
        from: process.env.EMAIL_SECRET,
        to: clientUser.email,
        subject: "Your business request has been completed",
        text: `Your business request has been completed. You can download the PDF now.`,
        html: `<div style="background-color: #f2f2f2; padding: 20px;">
        <h2>Your business request has been completed</h2>
        <p>You can download the PDF now.</p>`,
      });
    } catch (e) {
      throw new Error("Failed to send verification email");
    }

    if (
      clientUser.fcmToken === undefined ||
      clientUser.fcmToken === "" ||
      clientUser.fcmToken === null
    ) {
      return res.status(200).json(updatedPost);
    }

    newNotificationRef.set(notificationData);
    // Send FCM notification using Firebase Admin SDK
    const message = {
      token: clientUser.fcmToken,
      notification: {
        title: "Business Request Completed",
        body: "Your business request has been completed. You can download the PDF now.",
      },
    };

    const response = await admin.messaging().send(message);
    console.log("Notification sent successfully:", response);

    res.status(200).json(updatedPost);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

router.put("/posts/:id/refuse", async (req, res) => {
  try {
    const postId = req.params.id;

    // Find the post by its ID and update the status
    const updatedPost = await BusinessRequest.findByIdAndUpdate(
      postId,
      { status: "refuse" },
      { new: true }
    );

    if (!updatedPost) {
      return res.status(404).json({ message: "Post not found" });
    }

    res.status(200).json(updatedPost);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

// Route to get all the clients who send messages
// Define the route to get the last message for each client
router.get("/messages", async (req, res) => {
  try {
    // Aggregate to get the last message for each room
    const lastMessages = await Message.aggregate([
      {
        $group: {
          _id: "$roomId",
          lastMessage: { $last: "$$ROOT" },
        },
      },
    ]);

    // Fetch user details for each roomId
    const userDetailsPromises = lastMessages.map(async (msg) => {
      const user = await User.findById(msg._id);
      return {
        message: msg.lastMessage.message,
        userId: msg.lastMessage.roomId,
        last_name: user ? user.last_name : null,
        email: user ? user.email : null,
        timeElapsed: moment
          .duration(new Date() - msg.lastMessage.timestamp)
          .humanize(),
      };
    });

    // Wait for all user details promises to resolve
    const userDetails = await Promise.all(userDetailsPromises);

    res.json(userDetails);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: "Internal Server Error" });
  }
});

module.exports = router;
