import React from "react";
import "./loading.css";
export const Loading = ({ op = 0.8 }) => {
  return (
    <div
      className="Loading"
      style={{ backgroundColor: `rgba(255, 255, 255, ${op})` }}
    >
      <div
        className="spinner-border spinner-border-lg text-primary"
        role="status"
      >
        <span className="visually-hidden">Loading...</span>
      </div>
    </div>
  );
};
