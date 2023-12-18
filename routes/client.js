const router = require("express").Router();
const User = require("./../model/User");
const BusinessRequest = require("./../model/BusinessRequest");

router.route("/post").post(async (req, res) => {
  try {
    const body = req.body;

    const userId = req.user._id;

    // Check if the user exists
    const user = await User.findById(userId);
    if (!user) {
      return res.status(404).json({ message: "User not found" });
    }

    // Create a new business request
    const newRequest = new BusinessRequest({
      userId: userId,
      companyName: body.companyName,
      address: body.address,
      phoneNumber: body.phoneNumber,
      email: user.email,
      name: user.first_name,
      activityType: body.activityType,
      dateOfBirth: body.dateOfBirth,
      nationality: body.nationality,
      nationalityNum: body.nationalityNum,
      isDeclared: body.isDeclared,
    });

    // Save the business request to the database
    await newRequest.save();

    res.status(201).json({ message: "Business request saved successfully" });
  } catch (error) {
    res.status(400).json({ message: error.message });
  }
});

// Route to get all business requests for a specific user
router.get("/posts", async (req, res) => {
  try {
    const userId = req.user._id;

    // Find all business requests for the specified user
    const userRequests = await BusinessRequest.find({ userId });

    res.status(200).json(userRequests);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

// Route to get a specific business request by its ID
router.get("/posts/:id", async (req, res) => {
  try {
    const requestId = req.params.requestId;

    // Find the business request by its ID
    const businessRequest = await BusinessRequest.findById(requestId);

    if (!businessRequest) {
      return res.status(404).json({ message: "Business request not found" });
    }

    res.status(200).json(businessRequest);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

module.exports = router;
