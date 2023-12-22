const mongoose = require("mongoose");

const messageSchema = new mongoose.Schema({
  userId: {
    type: mongoose.Schema.Types.ObjectId, // Assuming userId is an ObjectId
    ref: "User", // Reference to the User model
    required: true,
  },
  roomId: {
    type: mongoose.Schema.Types.ObjectId, // Assuming userId is an ObjectId
    ref: "User", // Reference to the User model
    required: true,
  },
  message: {
    type: String,
    required: true,
  },
  timestamp: {
    type: Date,
    default: Date.now,
  },
  isSelf: {
    type: Boolean,
    default: false,
  },
});

const Message = mongoose.model("Message", messageSchema);

module.exports = Message;
