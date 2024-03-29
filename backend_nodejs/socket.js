const socketIO = require("socket.io");
const Message = require("./model/Message");
const User = require("./model/User");
const jwt = require("jsonwebtoken");

async function initializeSocket(server) {
  const io = socketIO(server);

  io.use((socket, next) => {
    const token = socket.handshake.query.token;

    // Validate and decode the token
    jwt.verify(token, process.env.JWT_SECRET, async (err, decoded) => {
      if (err) {
        return next(new Error("Authentication error"));
      }
      const user = await User.findById(decoded.id);
      // Attach user details to the socket
      socket.decoded = user;
      next();
    });
  });

  io.on("connection", (socket) => {
    console.log(`Client connected: ${socket.id}`);

    socket.on("new_message", async (data) => {
      data = JSON.parse(data);
      console.log(data);
      if (!data.roomId) {
        roomId = socket.decoded._id.toString();
      } else {
        roomId = data.roomId;
        data.isSelf = false;
      }
      // Save the message to the database
      const message = new Message({
        userId: socket.decoded._id.toString(),
        isSelf: data.isSelf,
        roomId: roomId,
        message: data.message,
      });

      await message.save();

      const user = await User.findById(socket.decoded._id.toString());

      // Emit the message to the room
      io.to(roomId).emit("broadcast", {
        userId: user.last_name,
        isSelf: data.isSelf,
        roomId: roomId,
        message: data.message,
      });
    });

    socket.on("join_room", async (roomIdc) => {
      // Modify this logic based on your requirements
      let roomId = "";
      if (!roomIdc) {
        roomId = socket.decoded._id.toString();
      } else {
        roomId = roomIdc;
      }

      if (!roomId || typeof roomId !== "string" || roomId.trim() === "") {
        // Handle invalid roomId
        console.error(`Invalid roomId provided by client ${socket.id}`);
        // Emit an error event or take appropriate action
        socket.emit("join_room_error", "Invalid room ID");
        return;
      }

      console.log(`Client ${socket.id} joined room ${roomId}`);
      socket.join(roomId);

      // Check if the room exists
      const existingMessages = await Message.find({ roomId });

      if (existingMessages.length === 0) {
        // Create an empty message for the room
        const emptyMessage = new Message({
          userId: "65a10b5099d2700d7199c6e5",
          roomId: roomId,
          isSelf: false,
          message: "Welcome to the Support Hub! 🚀",
        });

        await emptyMessage.save();
      }

      // Retrieve and send old messages for the room
      const messages = await Message.find({ roomId }).sort({ timestamp: 1 });
      const messagesWithUserDetails = await Promise.all(
        messages.map(async (message) => {
          const user = await User.findById(message.userId).exec();
          return {
            userId: user.last_name, // Replace last_name with the actual user property you want to send
            roomId: roomId,
            isSelf: message.isSelf,
            message: message.message, // Use message.text to get the message content
          };
        })
      );

      // Emit old_messages with the array of messages and user details
      socket.emit("old_messages", messagesWithUserDetails);
    });

    socket.on("disconnect", () => {
      console.log(`Client disconnected: ${socket.id}`);
    });
  });

  return io;
}

module.exports = initializeSocket;
