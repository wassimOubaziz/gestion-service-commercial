const router = require("express").Router();
const User = require("./../model/User");

router.put("/", async (req, res) => {
  const userid = req.user._id;

  const user = await User.findById(userid);
  if (user.role.length > 1) {
    if (user.role[0] == "client") {
      user.role = ["server", "client"];
    } else if (user.role[0] == "server") {
      user.role = ["client", "server"];
    }
    await user.save();
    return res.status(200).json({ message: "good" });
  } else {
    return res.status(401).json({
      message:
        "your not a service-client to do that if you think there is an error contact the admin",
    });
  }
});

module.exports = router;
