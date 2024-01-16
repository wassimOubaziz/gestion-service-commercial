import "./App.css";
import { Route, Routes } from "react-router-dom";
import SignIn from "./Pages/signin/SignIn";
import Admin from "./Pages/admin/admin";
import EditUser from "./Pages/admin/editUser";

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/" exact element={<SignIn />} />
        <Route path="/login" exact element={<SignIn />} />
        <Route path="/admin" exact element={<Admin />}>
          <Route path="users/:userId" exact element={<EditUser />} />
        </Route>
      </Routes>
    </div>
  );
}

export default App;
