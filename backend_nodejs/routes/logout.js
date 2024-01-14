const router = require("express").Router();
const User = require("./../model/User");

router.put("/", async (req, res) => {
  try {
    console.log("logout");
    const userId = req.user._id;
    const user = await User.findById(userId);
    user.fcmToken = undefined;
    await user.save({ validateBeforeSave: false });
    res.status(200).json({ message: "User logged out successfully" });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

module.exports = router;
