const mongoose = require("mongoose");
const validator = require("validator");

const businessRequestSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: "User", required: true },
  companyName: { type: String, required: true },
  address: { type: String, required: true },
  phoneNumber: { type: String, required: true },
  email: { type: String, required: true },
  activityType: { type: String, required: true },
  dateOfBirth: {
    type: Date,
    required: [true, "user must have a date of birth"],
    validate: [
      {
        validator: function (value) {
          const birthYear = new Date(value).getFullYear();
          const currentYear = new Date().getFullYear();
          return currentYear - birthYear >= 0 && currentYear - birthYear <= 120;
        },
        message: "Please provide a valid date of birth",
      },
      {
        validator: validator.isDate,
        message: "Please provide a valid date",
      },
    ],
  },
  nationality: {
    type: String,
    required: [true, "you need to provide nationality"],
  },
  nationalityNum: {
    type: String,
    required: [true, "you need to provide nationality number"],
  },
  isDeclared: {
    type: Boolean,
    required: [true, "you need to check the declaration box"],
  },
  name: {
    type: String,
    required: true,
  },
  status: {
    type: String,
    default: "in progression",
  },
  paid: {
    type: Boolean,
    default: false,
  },
  createdAt: {
    type: Date,
    default: new Date(),
  },
});

businessRequestSchema.index(
  { userId: 1, companyName: 1, activityType: 1 },
  { unique: true }
);

const BusinessRequest = mongoose.model(
  "BusinessRequest",
  businessRequestSchema
);

module.exports = BusinessRequest;
