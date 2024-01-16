import React, { useState } from "react";
import { Navbar, Container, Nav, Dropdown } from "react-bootstrap";
import { useCookies } from "react-cookie";
import userImage from "./user1.png"; // Import your user image
import { useNavigate } from "react-router-dom";

const AppNavbar = () => {
  const [cookies, setCookie, removeCookie] = useCookies(["token", "role"]);
  const [darkTheme, setDarkTheme] = useState(true);
  const navigate = useNavigate();

  const handleThemeToggle = () => {
    setDarkTheme(!darkTheme);
    // Implement logic to toggle dark theme in your application
  };

  const handleLogout = () => {
    // Remove cookies on logout
    removeCookie("token");
    removeCookie("role");
    navigate("/login");
    // You can also redirect the user to the login page or perform other logout actions
  };

  return (
    <Navbar bg={darkTheme ? "dark" : "light"} variant="dark" expand="lg">
      <Container>
        <Navbar.Brand href="#home">
          <img
            src={userImage}
            alt="User"
            className="user-image me-2"
            width={"30px"}
            height={"30px"}
            style={{ borderRadius: "50%" }}
          />
          EHealth
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="navbar" />
        <Navbar.Collapse id="navbar" className="justify-content-end">
          <Nav className="me-auto">{/* Add your navigation links here */}</Nav>
          <Nav>
            <Dropdown align="end">
              <Dropdown.Toggle
                variant={darkTheme ? "success" : "primary"}
                id="dropdown-basic"
              >
                <i className={`bi bi-${darkTheme ? "sun" : "moon"}`}></i>
              </Dropdown.Toggle>

              <Dropdown.Menu>
                <Dropdown.Item onClick={handleThemeToggle}>
                  {darkTheme ? "Switch to Light Theme" : "Switch to Dark Theme"}
                </Dropdown.Item>
                <Dropdown.Item onClick={handleLogout}>Logout</Dropdown.Item>
              </Dropdown.Menu>
            </Dropdown>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default AppNavbar;
