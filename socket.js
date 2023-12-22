const socketIO = require("socket.io");
const Message = require("./model/Message");
const User = require("./model/User");

async function initializeSocket(server) {
  const io = socketIO(server);

  io.on("connection", (socket) => {
    console.log(`Client connected: ${socket.id}`);

    socket.on("new_message", async (data) => {
      data = JSON.parse(data);
      console.log(data);

      // Save the message to the database
      const message = new Message({
        userId: data.userId,
        isSelf: data.isSelf,
        roomId: data.roomId,
        message: data.message,
      });

      await message.save();

      // Emit the message to the room
      io.to(data.roomId).emit("broadcast", data);
    });

    socket.on("join_room", async (roomId) => {
      if (!roomId || typeof roomId !== "string" || roomId.trim() === "") {
        // Handle invalid roomId
        console.error(`Invalid roomId provided by client ${socket.id}`);
        // You can emit an error message or take appropriate action
        return;
      }

      console.log(`Client ${socket.id} joined room ${roomId}`);
      socket.join(roomId);

      // Check if the room exists
      const existingMessages = await Message.find({ roomId });

      if (existingMessages.length === 0) {
        // Create an empty message for the room
        const emptyMessage = new Message({
          userId: roomId, // You can use a system user for empty messages
          roomId: roomId,
          isSelf: false,
          message: "Room created",
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
            roomId: message.roomId,
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
