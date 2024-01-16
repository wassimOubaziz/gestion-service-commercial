const router = require("express").Router();
const User = require("./../model/User");
const BusinessRequest = require("./../model/BusinessRequest");
const Message = require("./../model/Message");

router.get("/service", async (req, res) => {
  try {
    const currentDate = new Date();
    const twelveMonthsAgo = new Date();
    twelveMonthsAgo.setMonth(currentDate.getMonth() - 12);

    const result = await BusinessRequest.aggregate([
      {
        $match: {
          createdAt: { $gte: twelveMonthsAgo, $lt: currentDate },
        },
      },
      {
        $group: {
          _id: { $month: "$createdAt" },
          count: { $sum: 1 },
        },
      },
    ]);

    const countsByMonth = Array.from({ length: 12 }, (_, index) => ({
      month: index + 1,
      count: 0,
    }));

    result.forEach((item) => {
      const monthIndex = item._id - 1;
      countsByMonth[monthIndex].count = item.count;
    });
    res.json(countsByMonth);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: "Internal Server Error" });
  }
});

router.get("/admin", async (req, res) => {
  try {
    console.log("admin");
    const usersCount = await User.countDocuments();
    const messagesCount = await Message.countDocuments();
    const businessRequestsCount = await BusinessRequest.countDocuments();

    // Additional data for charts
    const userRoles = await User.aggregate([
      { $group: { _id: "$role", count: { $sum: 1 } } },
    ]);

    const messageData = await Message.aggregate([
      { $group: { _id: "$isSelf", count: { $sum: 1 } } },
    ]);

    const businessRequestStatus = await BusinessRequest.aggregate([
      { $group: { _id: "$status", count: { $sum: 1 } } },
    ]);
    // Send the data to the frontend
    res.json({
      usersCount,
      messagesCount,
      businessRequestsCount,
      userRoles,
      messageData,
      businessRequestStatus,
      // Add additional data properties as needed
    });
  } catch (error) {
    console.error("Error fetching chart data:", error);
    res.status(500).json({ error: "Internal Server Error" });
  }
});

module.exports = router;
