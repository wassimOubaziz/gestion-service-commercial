require("dotenv").config();
const mongoose = require("mongoose");
const app = require("./app");
const server = require("http").createServer(app);
const io = require("socket.io")(server);

io.on("connection", (client) => {
  console.log(`Client connected: ${client.id}`);

  client.on("new_message", (chat) => {
    console.log(`New message received: ${JSON.stringify(chat)}`);
    io.emit("broadcast", chat);
  });

  client.on("disconnect", () => {
    console.log(`Client disconnected: ${client.id}`);
  });
});

const URL = process.env.URL;

mongoose.set("strictQuery", true);
mongoose
  .connect(URL, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  })
  .then(() => {
    console.log("Connected to MongoDB");
  })
  .catch((error) => {
    console.error("Error connecting to MongoDB:", error);
  });

const PORT = process.env.PORT || "3600";

server.listen(PORT, "0.0.0.0", () => {
  console.log(`Server listening on http://localhost:${PORT}`);
});
