const router = require("express").Router();
const User = require("./../model/User");
const BusinessRequest = require("./../model/BusinessRequest");

router.get("/posts", async (req, res) => {
  try {
    const businessRequests = await BusinessRequest.find();
    res.status(200).json(businessRequests);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

router.get("/users", async (req, res) => {
  try {
    //get all the users do not get only the users with the role admin
    const users = await User.find({ role: { $ne: "admin" } });
    res.status(200).json(users);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

router.get("/users/:id", async (req, res) => {
  try {
    const userId = req.params.id;
    const user = await User.findById(userId);

    if (!user) {
      return res.status(404).json({ message: "User not found" });
    }

    res.status(200).json(user);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

router.delete("/users/:id", async (req, res) => {
  try {
    const userId = req.params.id;
    const deletedUser = await User.findByIdAndDelete(userId);

    if (!deletedUser) {
      return res.status(404).json({ message: "User not found" });
    }

    res.status(200).json({ message: "User deleted successfully" });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

router.put("/users/:id", async (req, res) => {
  try {
    const userId = req.params.id;
    //get new user
    const body = req.body;
    const newUser = await User.findByIdAndUpdate(userId, body, { new: true });
    if (!newUser) {
      return res.status(404).json({ message: "User not found" });
    }
    res.status(200).json(newUser);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

router.patch("/users/:id", async (req, res) => {
  try {
    const userId = req.params.id;

    // Update the user's role using findByIdAndUpdate
    const updatedUser = await User.findByIdAndUpdate(
      userId,
      { $set: { role: ["server", "client"] } },
      { new: true } // This ensures that the updated document is returned
    );

    if (!updatedUser) {
      return res
        .status(404)
        .json({ message: "User not found or role already updated" });
    }

    res
      .status(200)
      .json({ message: "User role updated successfully", user: updatedUser });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

router.get("/posts/:userId", async (req, res) => {
  try {
    const userId = req.params.userId;

    // Find all BusinessRequests associated with the given userId
    const businessRequests = await BusinessRequest.find({ userId });

    res.status(200).json(businessRequests);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

router.patch("/users/:id/only-client", async (req, res) => {
  try {
    const userId = req.params.id;

    // Update the user's role using findByIdAndUpdate
    const user = await User.findById(userId);
    if (!user) {
      return res.status(404).json({ message: "User not found" });
    }
    user.role = ["client"];
    await user.save();
    res
      .status(200)
      .json({ message: "User role updated successfully", user: user });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

router.delete("/posts/:requestId", async (req, res) => {
  try {
    const requestId = req.params.requestId;

    // Delete the BusinessRequest associated with the given userId and requestId
    const deletedRequest = await BusinessRequest.findByIdAndDelete({
      _id: requestId,
    });

    if (!deletedRequest) {
      return res
        .status(404)
        .json({ message: "Business request not found or already deleted" });
    }

    res.status(200).json({ message: "Business request deleted successfully" });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

module.exports = router;
