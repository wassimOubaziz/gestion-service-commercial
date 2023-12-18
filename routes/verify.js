//////////////////////login///////////////////////////
const router = require("express").Router();
const jwt = require("jsonwebtoken");
const User = require("./../model/User"); // Adjust the path based on your project structure

router.post("/", async (req, res) => {
  try {
    const { email, verificationCode } = req.body;
    console.log(email, verificationCode);

    if (!email || !verificationCode) {
      return res
        .status(400)
        .json({ message: "Email and verification code must be provided" });
    }

    const user = await User.findOne({ email });

    if (!user) {
      return res.status(404).json({ message: "User not found" });
    }

    // Check if the entered verification code matches the stored code
    if (user.validationCode !== verificationCode) {
      return res.status(401).json({ message: "Invalid verification code" });
    }

    // Update user's isValide property to true
    user.isValide = true;
    await user.save({ validateBeforeSave: false });

    res.status(200).json({ message: "Verification successful", status: true });
  } catch (e) {
    res.status(400).json({ message: e.message });
  }
});

module.exports = router;
