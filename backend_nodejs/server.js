// app.js or index.js

require("dotenv").config();
const express = require("express");
const http = require("http");
const mongoose = require("mongoose");
const initializeSocket = require("./socket");

const app = require("./app");
const server = http.createServer(app);

// Add your middleware and route configurations here

// Connect to MongoDB
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
  .then(() => {
    const PORT = process.env.PORT || "3600";

    // Pass the server instance to initializeSocket
    const io = initializeSocket(server);

    server.listen(PORT, "0.0.0.0", () => {
      console.log(`Server listening on http://localhost:${PORT}`);
    });
  })
  .catch((error) => {
    console.error("Error connecting to MongoDB:", error);
  });
