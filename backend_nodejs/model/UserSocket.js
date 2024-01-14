const mongoose = require("mongoose");

const userSocketSchema = new mongoose.Schema({
  userId: {
    type: String,
    required: true,
    unique: true,
  },
  socketId: {
    type: String,
    required: true,
  },
});

const UserSocket = mongoose.model("UserSocket", userSocketSchema);

module.exports = UserSocket;
