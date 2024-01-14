const router = require("express").Router();
const Rating = require("./../model/Rating");

router.post("/", async (req, res) => {
  try {
    await Rating.create({ user: req.user.id, rating: body.rating });
    res.status(200).json({ message: "thanks for your rating!!" });
  } catch (error) {
    res.status(400).json({ message: "Error: " + error.message });
  }
});

module.exports = router;
