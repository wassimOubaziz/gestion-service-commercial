const router = require("express").Router();
const User = require("./../model/User");
const BusinessRequest = require("./../model/BusinessRequest");
const PDFDocument = require("pdfkit");

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
      name: user.first_name + " " + user.last_name,
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
    const requestId = req.params.id;
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

// DELETE a specific business request by ID
router.delete("/posts/:id", async (req, res) => {
  try {
    const requestId = req.params.id;
    const userIdFromRequest = req.user.id; // Assuming user ID is stored in req.user after authentication

    // Find the business request by its ID
    const businessRequest = await BusinessRequest.findById(requestId);

    if (!businessRequest) {
      return res.status(404).json({ message: "Business request not found" });
    }

    // Check if the authenticated user is the owner of the business request
    if (businessRequest.userId.toString() !== userIdFromRequest) {
      return res.status(403).json({
        message:
          "Unauthorized: You don't have permission to delete this request",
      });
    }

    // Delete the business request
    const deletedRequest = await BusinessRequest.findByIdAndDelete(requestId);

    if (!deletedRequest) {
      return res.status(404).json({ message: "Business request not found" });
    }

    res.status(200).json({ message: "Business request deleted successfully" });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

// Validate payment route
router.put("/:id/validate-payment", async (req, res) => {
  try {
    const requestId = req.params.id;

    // Update the paid status to true in the database
    const result = await BusinessRequest.updateOne(
      { _id: requestId },
      { $set: { paid: true } }
    );

    if (result.nModified === 0) {
      return res.status(404).json({ message: "Business request not found" });
    }

    res.status(200).json({
      message: "Payment validated successfully",
      requestId: requestId,
    });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

// Route to generate and download a customized PDF
router.get("/:id/register-pdf", async (req, res) => {
  const id = req.params.id;

  const post = await BusinessRequest.findById(id);

  console.log(post.userId.toString(), req.user._id.toString());

  // Check if the authenticated user is the owner of the business request
  if (post.userId.toString() !== req.user._id.toString()) {
    return res.status(403).json({
      message: "Unauthorized: You don't have permission to get this pdf",
    });
  }

  if (!post.paid || post.status !== "completed") {
    return res
      .status(401)
      .json({ message: "Business request not yet completed or paid" });
  }

  // Create a new PDF document
  const doc = new PDFDocument();
  const userData = {
    "Company Name": post.companyName,
    Address: post.address,
    "Full Name": post.name,
    "Phone Number": post.phoneNumber,
    Email: post.email,
    "Activity Type": post.activityType,
    "Date Of Birth": post.dateOfBirth,
    Nationality: post.nationality,
    "Nationality Number": post.nationalityNum,
    Declared: post.isDeclared,
    Paid: post.paid,
    "ID verification": post._id,
  };

  // Generate a unique filename for the PDF
  const filename = `business_request_${post._id}.pdf`;

  // Set response headers for PDF download
  res.setHeader("Content-Type", "application/pdf");
  res.setHeader("Content-Disposition", `attachment; filename=${filename}`);

  // Pipe the PDF content to the response
  doc.pipe(res);

  // Customize the PDF content
  doc.fontSize(25).text("Company Registration PDF", { align: "center" });
  doc.moveDown(); // Add a line break
  doc
    .fontSize(14)
    .text(
      "To complete the registration process, please print this PDF and visit our office at Rue 25, Constantine. Present this document to our registration desk to facilitate the issuance of your official business registration certificate. Thank you for choosing our services."
    );

  doc.moveDown();
  doc.moveDown();

  // Add user information to the PDF
  Object.entries(userData).forEach(([key, value]) => {
    doc.fontSize(12).text(`${key}: ${value}`);
    doc.moveDown(); // Add a line break
  });

  // Finalize the PDF and end the response
  doc.end();
});

module.exports = router;
