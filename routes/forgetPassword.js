const express = require("express");
const router = express.Router();
const nodemailer = require("nodemailer");
const User = require("../model/User");

// Route to initiate the forgot password process
router.post("/forgot-password", async (req, res) => {
  try {
    const { email } = req.body;

    // Find the user by email
    const user = await User.findOne({ email });

    if (!user) {
      return res.status(404).json({ message: "User not found" });
    }

    // Generate a unique validation code
    const validationCode = generateValidationCode();

    // Store the validation code in the user's document
    user.validationCode = validationCode;
    user.validationCodeExpires = Date.now() + 3600000; // 1 hour

    await user.save();
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
        to: body.email,
        subject: "Please validate your account",
        text: `Validation code is: ${verificationCode}`,
        html: `<div style="background-color: #f2f2f2; padding: 20px;">
          <h2>Thanks for registering!</h2>
          <p>Please use the following code to validate your account:</p>
          <p style="font-size: 24px; font-weight: bold;">${verificationCode}</p>
      </div>`,
      });
    } catch (e) {
      throw new Error("Failed to send verification email");
    }

    res.status(200).json({ message: "Validation code sent successfully" });
  } catch (error) {
    if (error.message.includes("Failed to send verification email")) {
      // Handle email sending error
      return res
        .status(500)
        .json({ message: "Failed to send verification email" });
    }

    // If there is any other error, revert changes in the user document
    await User.updateOne(
      { email: user.email },
      { validationCode: undefined, validationCodeExpires: undefined }
    );

    res.status(500).json({ message: "Internal Server Error" });
  }
});

// Route to handle the password reset
router.post("/reset-password", async (req, res) => {
  try {
    const { email, newPassword } = req.body;

    // Find the user by email and validation code
    const user = await User.findOne({
      email,
      validationCodeExpires: { $gt: Date.now() }, // Validation code should be valid
    });

    if (!user) {
      return res
        .status(401)
        .json({ message: "Invalid validation code or email" });
    }

    // Update the password
    user.password = newPassword;
    user.validationCode = undefined;
    user.validationCodeExpires = undefined;

    await user.save();

    res.status(200).json({ message: "Password reset successful" });
  } catch (error) {
    res.status(500).json({ message: "Internal Server Error" });
  }
});

// Function to generate a random 4-digit validation code
function generateValidationCode() {
  return Math.floor(1000 + Math.random() * 9000).toString();
}

module.exports = router;
