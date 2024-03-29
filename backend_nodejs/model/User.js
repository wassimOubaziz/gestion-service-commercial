const mongoose = require("mongoose");
const validator = require("validator");
const bcrypt = require("bcryptjs");
const Schema = mongoose.Schema;

const userSchima = new Schema({
  first_name: {
    type: String,
    required: [true, "user must have a name"],
    lowercase: true,
  },
  last_name: {
    type: String,
    required: [true, "user must have an surname"],
    lowercase: true,
  },
  email: {
    type: String,
    required: [true, "user must have an email"],
    unique: [true, "email must be unique"],
    lowercase: true,
    validate: [validator.isEmail, "plz provide a valide email"],
  },
  password: {
    type: String,
    required: [true, "user must have a password"],
    minlength: [8, "password must be at least 8 length"],
    select: false,
  },
  role: [
    {
      type: String,
      default: "client",
      enum: ["client", "server", "admin"],
      lowercase: true,
    },
  ],
  active: {
    type: Boolean,
    default: false,
  },
  createdAt: {
    type: Date,
    default: () => Date.now(),
  },
  changedPassword: Date,
  validationCode: {
    type: String,
  },
  isValide: {
    type: Boolean,
    default: false,
  },
  validationCodeExpires: {
    type: Date,
  },
  deviceToken: { type: String },
  fcmToken: { type: String, default: undefined },
  notificationSeen: { type: Boolean, default: false },
});

//this will work when i update the password or when i create new user
userSchima.pre("save", async function (next) {
  if (!this.isModified("password")) return next();
  this.password = await bcrypt.hash(this.password, 12);
  this.changedPassword = Date.now();
  next();
});

userSchima.methods.checkPassword = async (userPass, hashPass) => {
  return bcrypt.compare(userPass, hashPass);
};

//this will return true if the time jwt < time changed password
userSchima.methods.changedPasswordAfter = function (JWTTimestamp) {
  if (this.changedPassword) {
    return JWTTimestamp < parseInt(this.changedPassword.getTime() / 1000, 10);
  }

  return false;
};

const User = mongoose.model("User", userSchima);

module.exports = User;
