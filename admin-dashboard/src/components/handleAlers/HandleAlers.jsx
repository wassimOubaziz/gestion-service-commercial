import React from "react";
import "./handleAlers.css";

function HandleAlers({ message, status }) {
  return (
    <>
      {status ? (
        <div
          className="alert alert-success"
          style={{
            position: "absolute",
            zIndex: "1000",
            top: "40px",
            minWidth: "400px",
            textAlign: "center",
          }}
          role="alert"
        >
          {message}
        </div>
      ) : (
        <div
          className="alert alert-danger"
          style={{
            position: "absolute",
            zIndex: "1000",
            top: "40px",
            minWidth: "400px",
            textAlign: "center",
          }}
          role="alert"
        >
          {message}
        </div>
      )}
    </>
  );
}

export default HandleAlers;
