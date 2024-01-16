import React, { useEffect, useState } from "react";
import "./../assets/vendor/css/core.css";
import "./../assets/vendor/css/theme-default.css";
import "./../assets/vendor/css/pages/page-auth.css";
import "./../assets/css/demo.css";
import logo from "../logo.png";
import { axios } from "./../axios";
import { Link, Navigate, useNavigate, useParams } from "react-router-dom";
import { useCookies } from "react-cookie";
import { Loading } from "../../components/Loading/Loading";
import HandleAlers from "../../components/handleAlers/HandleAlers";

const SignIn = () => {
  const [faild, SetFaild] = useState(``);
  const [role, setRole] = useState([]);
  const [cookies, setCookie, removeCookie] = useCookies(["token", "role"]);
  const [isLoading, setIsLoading] = useState(false);
  const naviage = useNavigate();

  useEffect(() => {
    if (!cookies.token && cookies.role) {
      removeCookie("role");
    }
  }, []);

  const handelSubmitForm = (e) => {
    e.preventDefault();
    removeCookie("token");
    removeCookie("role");
    const obj = { email: e.target[0].value, password: e.target[1].value };
    const login = async () => {
      try {
        const response = await axios.post("/login", obj, {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        });
        setRole("admin");
        if (response.data.role.includes("admin")) {
          setCookie("token", response?.data?.token);
          setCookie("role", response?.data.role, {
            expires: new Date(Date.now() + 24 * 60 * 60 * 1000 * 5),
          });
        } else {
          SetFaild("You are not admin, go to phone app to login");
        }
      } catch (e) {
        SetFaild(e.response?.data.message);
      }
      setIsLoading(false);
    };
    setIsLoading(true);
    login();
  };
  const { sinup = "" } = useParams();
  const [check, setCheck] = useState(`${sinup.replace("%20", " ")}`);

  if (cookies.token && cookies.role.includes("admin")) {
    return <Navigate to={`/admin`} />;
  }

  return (
    <div className="container-xxl">
      {isLoading ? <Loading /> : ""}
      <div className="authentication-wrapper authentication-basic container-p-y">
        {check && sinup ? <HandleAlers message={check} status={true} /> : ""}

        {faild ? <HandleAlers message={faild} status={false} /> : ""}

        <div className="authentication-inner">
          {/* Register */}
          <div className="card">
            <div className="card-body">
              {/* Logo */}
              <div className="app-brand justify-content-center">
                <Link to={"/"} className="app-brand-link gap-2">
                  <img
                    src={logo}
                    alt="logo Sijili"
                    height={"40px"}
                    width={"150px"}
                  />
                </Link>
              </div>
              {/* /Logo */}
              <h4 className="mb-2">
                Welcome to SIJILI{" "}
                <span role="img" aria-label="emoji">
                  👋
                </span>
              </h4>
              <p className="mb-4">
                Please sign-in to your account and start the adventure
              </p>
              <form
                id="formAuthentication"
                className="mb-3"
                style={{ display: "block" }}
                onSubmit={handelSubmitForm}
              >
                <div className="mb-3">
                  <label htmlFor="email" className="form-label">
                    Email
                  </label>
                  <input
                    type="email"
                    className="form-control"
                    id="email"
                    name="email"
                    placeholder="Enter your email"
                    autoFocus
                    required
                  />
                </div>
                <div className="mb-3 form-password-toggle">
                  <div className="d-flex justify-content-between">
                    <label className="form-label" htmlFor="password">
                      Password
                    </label>
                    <a href="auth-forgot-password-basic.html">
                      <small>Forgot Password?</small>
                    </a>
                  </div>
                  <div className="input-group input-group-merge">
                    <input
                      type="password"
                      id="password"
                      className="form-control"
                      name="password"
                      placeholder="············"
                      aria-describedby="password"
                      required
                    />
                    <span className="input-group-text cursor-pointer">
                      <i className="bx bx-hide" />
                    </span>
                  </div>
                </div>
                <div className="mb-3">
                  <div className="form-check">
                    <input
                      className="form-check-input"
                      type="checkbox"
                      id="remember-me"
                    />
                    <label className="form-check-label" htmlFor="remember-me">
                      Remember Me
                    </label>
                  </div>
                </div>
                <div className="mb-3">
                  <button
                    className="btn btn-primary d-grid w-100"
                    type="submit"
                  >
                    Sign in
                  </button>
                </div>
              </form>
              <p className="text-center">
                <span>New on our platform?</span>
                <Link to={"/sign-up"}>
                  <span>Create an account</span>
                </Link>
              </p>
            </div>
          </div>
          {/* /Register */}
        </div>
      </div>
    </div>
  );
};

export default SignIn;
