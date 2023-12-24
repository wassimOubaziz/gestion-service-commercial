//////////////////////login///////////////////////////
const router = require("express").Router();
const jwt = require("jsonwebtoken");
const User = require("./../model/User"); // Adjust the path based on your project structure

router.post("/", async (req, res) => {
  try {
    const { email, password } = req.body;
    console.log(email, password);
    if (!email || !password) {
      return res
        .status(400)
        .json({ message: "Email and password must not be empty" });
    }

    const user = await User.findOne({ email }).select("+password");

    if (!user) {
      return res.status(401).json({ message: "Invalid email or password" });
    }

    if (!user.isValide) {
      return res.status(400).json({ message: "This account is not valid!" });
    }

    const isPasswordValid = await user.checkPassword(password, user.password);

    if (!isPasswordValid) {
      return res.status(401).json({ message: "Invalid email or password" });
    }

    const token = jwt.sign({ id: user._id }, process.env.JWT_SECRET, {
      expiresIn: process.env.JWT_EXPIRE_TIME,
    });

    user.active = true;
    await user.save({ validateBeforeSave: false });

    user.password = undefined;

    user.isValide = undefined;

    res.cookie("jwt", token, {
      expires: new Date(
        Date.now() + process.env.COOKIE_EXPIRE_TIME * 24 * 60 * 60 * 1000
      ),
      httpOnly: true,
    });

    res.status(200).json({
      status: "success",
      token,
      role: user.role,
    });
  } catch (e) {
    res.status(400).json({ message: e.message });
  }
});

module.exports = router;
