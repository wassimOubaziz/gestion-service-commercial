import React, { useState, useEffect } from "react";
import { Bar, Pie, Doughnut } from "react-chartjs-2";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { CategoryScale, LinearScale, BarElement, Title } from "chart.js";
import { axios } from "./../axios";

// Register necessary scales and elements
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement
);

const AdminCharts = () => {
  const [chartData, setChartData] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get("/static/admin");
        setChartData(response.data);
      } catch (error) {
        console.error("Error fetching chart data:", error);
      }
    };

    fetchData();
  }, []);

  return (
    <div>
      {chartData && (
        <div>
          {/* Bar Chart - User Roles */}

          <div
            style={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              flexWrap: "wrap",
            }}
          >
            <div style={{ width: "450px" }}>
              <Bar
                data={{
                  labels: chartData.userRoles.map((role) =>
                    role._id.join(", ")
                  ),
                  datasets: [
                    {
                      label: "User Roles",
                      data: chartData.userRoles.map((role) => role.count),
                      backgroundColor: [
                        "rgba(75,192,192,0.2)",
                        "rgba(255,99,132,0.2)",
                      ],
                      borderColor: ["rgba(75,192,192,1)", "rgba(255,99,132,1)"],
                      borderWidth: 1,
                    },
                  ],
                }}
              />
            </div>

            {/* Pie Chart - Message Data */}
            <div style={{ width: "450px" }}>
              <Pie
                data={{
                  labels: ["Not Self", "Self"],
                  datasets: [
                    {
                      data: chartData.messageData.map((data) => data.count),
                      backgroundColor: [
                        "rgba(255,99,132,0.2)",
                        "rgba(75,192,192,0.2)",
                      ],
                      hoverBackgroundColor: [
                        "rgba(255,99,132,0.8)",
                        "rgba(75,192,192,0.8)",
                      ],
                    },
                  ],
                }}
              />
            </div>

            {/* Doughnut Chart - Business Request Status */}
            <div style={{ width: "450px" }}>
              <Doughnut
                data={{
                  labels: chartData.businessRequestStatus.map(
                    (status) => status._id
                  ),
                  datasets: [
                    {
                      data: chartData.businessRequestStatus.map(
                        (status) => status.count
                      ),
                      backgroundColor: ["rgba(255,99,132,0.2)"],
                      hoverBackgroundColor: ["rgba(255,99,132,0.8)"],
                    },
                  ],
                }}
              />
            </div>
          </div>

          {/* Add more charts for other data as needed */}
        </div>
      )}
    </div>
  );
};

export default AdminCharts;
