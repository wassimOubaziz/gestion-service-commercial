//create Rating model
const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const ratingSchema = new Schema({
  user: { type: mongoose.Schema.Types.ObjectId, ref: "User", required: true },
  rating: { type: Number, require: true },
});

const Rating = mongoose.model("Rating", ratingSchema);

module.exports = Rating;
