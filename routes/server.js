const router = require("express").Router();
const User = require("./../model/User");
const BusinessRequest = require("./../model/BusinessRequest");

// GET all business requests
router.get("/posts", async (req, res) => {
  try {
    // Find all business requests
    const allRequests = await BusinessRequest.find();

    if (!allRequests || allRequests.length === 0) {
      return res.status(404).json({ message: "No business requests found" });
    }

    res.status(200).json(allRequests);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

// GET a specific post by ID
router.get("/posts/:id", async (req, res) => {
  try {
    const postId = req.params.id;

    // Find the post by its ID
    const post = await BusinessRequest.findById(postId);

    if (!post) {
      return res.status(404).json({ message: "Business Request not found" });
    }

    res.status(200).json(post);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

// PUT update status of a specific post by ID
router.put("/posts/:id", async (req, res) => {
  try {
    const postId = req.params.id;

    // Find the post by its ID and update the status
    const updatedPost = await BusinessRequest.findByIdAndUpdate(
      postId,
      { status: "completed" },
      { new: true }
    );

    if (!updatedPost) {
      return res.status(404).json({ message: "Post not found" });
    }

    res.status(200).json(updatedPost);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

module.exports = router;
