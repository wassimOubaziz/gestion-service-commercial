require("dotenv").config();
const mongoose = require("mongoose");
const http = require("http");
const socketIO = require("socket.io");

const app = require("./app");
const server = http.createServer(app);
const io = socketIO(server);

const URL = process.env.URL;

mongoose.set("strictQuery", true);
mongoose
  .connect(URL, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  })
  .then(() => {
    console.log("connected to mongo");
  });

const PORT = process.env.PORT || "3600";

app.listen(PORT, "0.0.0.0", () => console.log("http://localhost:" + PORT));
