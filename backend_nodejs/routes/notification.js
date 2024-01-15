const router = require("express").Router();
const admin = require("firebase-admin");
const User = require("./../model/User");

router.get("/", async (req, res) => {
  try {
    const userId = req.user._id.toString();
    console.log(userId);

    await User.findByIdAndUpdate(userId, { notificationSeen: false });

    // Reference to the notifications node in your Firebase Realtime Database
    const notificationsRef = admin.database().ref("notifications");

    // Query notifications for the specific user
    const userNotificationsSnapshot = await notificationsRef
      .orderByChild("userId")
      .equalTo(userId)
      .once("value");
    const userNotifications = userNotificationsSnapshot.val();

    if (!userNotifications) {
      return res
        .status(404)
        .json({ message: "Notifications not found for the user" });
    }

    // Convert the notifications to an array
    const notificationsArray = Object.values(userNotifications);
    console.log(notificationsArray);
    res.status(200).json(notificationsArray);
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: "Internal server error" });
  }
});

router.get("/notif", async (req, res) => {
  const user = await User.findById(req.user._id);
  if (user.notificationSeen) {
    return res.status(200).json(true);
  }
  res.status(200).json(false);
});

router.delete("/:timestamp", async (req, res) => {
  try {
    const userId = req.user._id.toString();
    const timestamp = parseInt(req.params.timestamp);

    // Reference to the notifications node in your Firebase Realtime Database
    const notificationsRef = admin.database().ref("notifications");

    // Query notifications for the specific user and timestamp
    const userNotificationsSnapshot = await notificationsRef
      .orderByChild("userId")
      .equalTo(userId)
      .once("value");

    const userNotifications = userNotificationsSnapshot.val();

    if (!userNotifications) {
      return res
        .status(404)
        .json({ message: "Notifications not found for the user" });
    }

    // Iterate over notifications to find the one with the specified timestamp
    let notificationIdToDelete;
    Object.keys(userNotifications).forEach((notificationId) => {
      const notification = userNotifications[notificationId];
      if (notification.timestamp === timestamp) {
        notificationIdToDelete = notificationId;
        return;
      }
    });

    // Check if a notification with the specified timestamp was found
    if (!notificationIdToDelete) {
      return res.status(404).json({
        message:
          "Notification not found for the user with the specified timestamp",
      });
    }

    // Delete the notification
    await notificationsRef.child(notificationIdToDelete).remove();

    res.status(200).json({ message: "Notification deleted successfully" });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: "Internal server error" });
  }
});

module.exports = router;
