const express = require("express");
const app = express();
const cors = require("cors");
const bodyParser = require("body-parser");

const { protect, permition } = require("./routes/authorisation");

const register = require("./routes/register");
const login = require("./routes/login");
const client = require("./routes/client");
const verify = require("./routes/verify");
const server = require("./routes/server");
const rating = require("./routes/rating");
const admin = require("./routes/admin");
const forgotPassword = require("./routes/forgetPassword");
const notification = require("./routes/notification");
const logout = require("./routes/logout");
const switchacount = require("./routes/switchacount");
const static = require("./routes/statistic");
var admine = require("firebase-admin");

var serviceAccount = require("./register-commerce-firebase-adminsdk-gdqfy-a26084119b.json");

admine.initializeApp({
  credential: admine.credential.cert(serviceAccount),
  databaseURL:
    "https://register-commerce-default-rtdb.europe-west1.firebasedatabase.app",
});

// app.use((req, res, next) => {
//   req.io = app.get("io");
//   next();
// });

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.use(
  cors({
    origin: [
      "http://localhost:3000",
      "www.localhost:3000",
      "localhost:3000",
      "http://192.168.144.219:3000",
    ],
    credentials: true,
    exposedHeaders: ["Set-Cookie"],
  })
);

app.use(express.static("public"));
app.use(express.json());

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////// Routes ////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

app.use("/register", register);
app.use("/forgot-password", forgotPassword);
app.use("/login", login);
app.use("/verify", verify);
app.use("/client", protect, permition("client"), client);
app.use("/server", server);
app.use("/admin", admin);
app.use("/rating", protect, rating);
app.use("/notifications", protect, notification);
app.use("/switch-role", protect, switchacount);
app.use("/static", static);

app.use("/logout", protect, logout);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//exporting app
module.exports = app;
