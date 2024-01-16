import React, { useState, useEffect } from "react";
import { Link, Outlet, useNavigate, useOutlet } from "react-router-dom";
import logo from "../logo.png";
import { axios } from "./../axios";
import { Loading } from "../../components/Loading/Loading";
import { Table, Button, Navbar, Container, Nav } from "react-bootstrap";
import AdminCharts from "./AdminCharts";

const Admin = () => {
  const [users, setUsers] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [failed, setFailed] = useState(null);
  const [darkMode, setDarkMode] = useState(false); // New state for dark mode
  const outlet = useOutlet();

  useEffect(() => {
    setIsLoading(true);
    axios
      .get("/admin/users", {
        headers: { "Content-Type": "application/json" },
        withCredentials: true,
      })
      .then((response) => {
        setUsers(response.data);
        setIsLoading(false);
      })
      .catch((e) => {
        setFailed(e.response?.data.message);
        setIsLoading(false);
      });
  }, []);

  const navigate = useNavigate();

  const deleteUser = (id) => {
    setIsLoading(true);
    axios
      .delete(`/admin/users/${id}`, {
        headers: { "Content-Type": "application/json" },
        withCredentials: true,
      })
      .then((response) => {
        setIsLoading(false);
        window.location.reload();
      })
      .catch((e) => {
        setFailed(e.response?.data.message);
        setIsLoading(false);
      });
  };

  const handleEditClick = (id) => {
    navigate(`/admin/users/${id}`);
  };

  const handleSortClick = () => {
    setUsers([...users].sort((a, b) => a.role.length - b.role.length));
  };

  const handleLogout = () => {
    // Add your logout logic here
  };

  const handleDarkModeToggle = () => {
    setDarkMode(!darkMode);
    // You can add logic here to apply dark mode styles to your app
  };

  return (
    <div className={`page-center ${darkMode ? "dark-mode" : ""}`}>
      {isLoading && <Loading />}
      <div className="page-center-in">
        <Navbar bg="light" expand="lg">
          <Container>
            <Navbar.Brand>
              <img
                src={logo}
                alt="Logo"
                width={120}
                height={30}
                className="d-inline-block align-top"
              />
            </Navbar.Brand>
            <Navbar.Toggle aria-controls="navbar-nav" />
            <Navbar.Collapse id="navbar-nav" className="justify-content-end">
              <Nav>
                <Nav.Link href="#" onClick={handleDarkModeToggle}>
                  {darkMode ? "Light Mode" : "Dark Mode"}
                </Nav.Link>
                <Nav.Link href="#" onClick={handleLogout}>
                  Logout
                </Nav.Link>
              </Nav>
            </Navbar.Collapse>
          </Container>
        </Navbar>
        {outlet ? (
          <Outlet />
        ) : (
          <div className="container-fluid">
            <form className="sign-box">
              <div className="form-group">
                <Table striped bordered hover style={{ marginTop: "30px" }}>
                  <thead>
                    <tr>
                      <th>#</th>
                      <th>First Name</th>
                      <th>Last Name</th>
                      <th>Role</th>
                      <th>Email</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {users.map((user, index) => (
                      <tr key={index}>
                        <td>{index + 1}</td>
                        <td>{user.first_name}</td>
                        <td>{user.last_name}</td>
                        <td>
                          {user.role.length > 1
                            ? user.role[0] + ", " + user.role[1]
                            : user.role[0]}
                        </td>
                        <td>{user.email}</td>
                        <td>
                          <Button
                            variant="info"
                            onClick={() => handleEditClick(user._id)}
                          >
                            Edit
                          </Button>
                          <Button
                            variant="danger"
                            onClick={() => deleteUser(user._id)}
                          >
                            Delete
                          </Button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
                <Button variant="secondary" onClick={handleSortClick}>
                  Sort by Roles
                </Button>
              </div>
            </form>
          </div>
        )}

        <AdminCharts />
      </div>
    </div>
  );
};

export default Admin;
