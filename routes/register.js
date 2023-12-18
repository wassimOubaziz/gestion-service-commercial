const router = require("express").Router();
const nodemailer = require("nodemailer");
const jwt = require("jsonwebtoken");
const User = require("../model/User"); // Adjust the path based on your project structure

//////////////////////register///////////////////////////
router.post("/", async (req, res) => {
  try {
    const body = req.body;
    console.log(req.body);
    body.role = ["client"];
    // Generate a random 4-digit verification code
    const verificationCode = Math.floor(1000 + Math.random() * 9000);

    const transporter = nodemailer.createTransport({
      service: "gmail",
      auth: {
        user: process.env.EMAIL_SECRET,
        pass: process.env.PASSWORD_SECRET,
      },
    });

    const user = await User.findOne({ email: body.email });

    if (!user) {
      // Store the verification code in the user object
      body.validationCode = verificationCode;

      const token = jwt.sign({ email: body.email }, process.env.JWT_SECRET);
      body.validationToken = token;

      await User.create(body);

      // Sending email verification
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
        // If sending email fails, delete the user
        await User.deleteOne({ email: body.email });
        throw new Error("Failed to send verification email");
      }

      return res.status(200).json({
        message: "Validation Email successfully sent. Please check your email.",
      });
    }

    // If the user already exists
    return res.status(400).json({ message: "User already exists" });
  } catch (e) {
    res.status(400).json({ message: e.message });
  }
});

module.exports = router;
