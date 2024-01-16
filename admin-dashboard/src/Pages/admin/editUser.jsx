import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { axios } from "./../axios";
import { Loading } from "../../components/Loading/Loading";
import { Button, Form } from "react-bootstrap";

const EditedUser = () => {
  const { userId } = useParams();
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [editedUser, setEditedUser] = useState({
    first_name: "",
    last_name: "",
    email: "",
  });

  useEffect(() => {
    setIsLoading(true);
    axios
      .get(`/admin/users/${userId}`, {
        headers: { "Content-Type": "application/json" },
        withCredentials: true,
      })
      .then((response) => {
        setUser(response.data);
        setEditedUser({
          first_name: response.data.first_name,
          last_name: response.data.last_name,
          email: response.data.email,
        });
        setIsLoading(false);
      })
      .catch((error) => {
        console.error("Error fetching user details:", error);
        setIsLoading(false);
      });
  }, [userId]);

  const navigate = useNavigate();

  const handleEditClick = () => {
    setIsEditing(true);
  };

  const handleCancelEdit = () => {
    setIsEditing(false);
    // Reset the edited user to the current user's data
    setEditedUser({
      first_name: user.first_name,
      last_name: user.last_name,
      email: user.email,
    });
  };

  const handleSaveEdit = () => {
    setIsLoading(true);
    axios
      .put(
        `/admin/users/${userId}`,
        {
          first_name: editedUser.first_name,
          last_name: editedUser.last_name,
          email: editedUser.email,
        },
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        }
      )
      .then((response) => {
        setIsEditing(false);
        setUser(response.data);
        setIsLoading(false);
      })
      .catch((error) => {
        console.error("Error updating user details:", error);
        setIsLoading(false);
      });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEditedUser((prevUser) => ({
      ...prevUser,
      [name]: value,
    }));
  };

  const handleTurnToServiceClient = () => {
    setIsLoading(true);
    // Assuming you have an API endpoint to handle turning a user into a service-client
    axios
      .patch(
        `/admin/users/${userId}`,
        {},
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        }
      )
      .then((response) => {
        // Handle success, e.g., show a success message or update the user state
        console.log("User turned into a service-client", response.data);
        setIsLoading(false);
        navigate(0);
      })
      .catch((error) => {
        console.error("Error turning user into a service-client:", error);
        setIsLoading(false);
      });
  };

  const handleTurnToOnlyClient = () => {
    setIsLoading(true);
    // Assuming you have an API endpoint to handle turning a user into a service-client
    axios
      .patch(
        `/admin/users/${userId}/only-client`,
        {},
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        }
      )
      .then((response) => {
        // Handle success, e.g., show a success message or update the user state
        console.log("User turned into a service-client", response.data);
        setIsLoading(false);
        navigate(0);
      })
      .catch((error) => {
        console.error("Error turning user into a service-client:", error);
        setIsLoading(false);
      });
  };

  if (isLoading) {
    return <Loading />;
  }

  if (!user) {
    return <div>User not found</div>;
  }

  return (
    <div className="page-center">
      <div className="page-center-in">
        <div className="container-fluid">
          <div className="sign-box">
            <div className="sign-avatar">
              <h1>{`${user.first_name} ${user.last_name}`}</h1>
              <p>Email: {user.email}</p>
              <p>Role: {user.role.join(", ")}</p>

              {isEditing ? (
                <Form>
                  <Form.Group controlId="formFirstName">
                    <Form.Label>First Name</Form.Label>
                    <Form.Control
                      type="text"
                      name="first_name"
                      value={editedUser.first_name}
                      onChange={handleInputChange}
                    />
                  </Form.Group>
                  <Form.Group controlId="formLastName">
                    <Form.Label>Last Name</Form.Label>
                    <Form.Control
                      type="text"
                      name="last_name"
                      value={editedUser.last_name}
                      onChange={handleInputChange}
                    />
                  </Form.Group>
                  <Form.Group controlId="formEmail">
                    <Form.Label>Email</Form.Label>
                    <Form.Control
                      type="email"
                      name="email"
                      value={editedUser.email}
                      onChange={handleInputChange}
                    />
                  </Form.Group>
                  <Button variant="success" onClick={handleSaveEdit}>
                    Save
                  </Button>
                  <Button variant="danger" onClick={handleCancelEdit}>
                    Cancel
                  </Button>
                </Form>
              ) : (
                <div>
                  <Button variant="primary" onClick={handleEditClick}>
                    Edit
                  </Button>
                  {user.role.length == 1 ? (
                    <Button
                      variant="warning"
                      onClick={handleTurnToServiceClient}
                    >
                      Turn to Service-Client
                    </Button>
                  ) : (
                    <Button variant="warning" onClick={handleTurnToOnlyClient}>
                      {" "}
                      Turn to Client{" "}
                    </Button>
                  )}
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EditedUser;
