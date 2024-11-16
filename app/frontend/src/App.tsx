import HomePage from "./components/publicPages/HomePage/HomePage";
import AdminPage from "./components/adminPages/AdminPage/AdminPage";
import LoginPage from "./components/publicPages/LoginPage/LoginPage";
import LostPage from "./components/publicPages/LostPage/LostPage";
import TopNavBar from "./components/publicPages/TopNavBar/TopNavBar";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Cookies from "universal-cookie";

import React, { ReactNode } from "react";
import EventWorkerPage from "./components/eventWorkerPages/EventWorkerPage/EventWorkerPage";
import PrivateUserPage from "./components/privatePages/PrivateUserPage/PrivateUserPage";
import EventManagerPage from "./components/eventManagerPages/EventManagerPage/EventWorkerPage";
export const NON_TOP_NAV_BAR_ID = "non-top-nav-bar-id-12321";
function App() {
  const zoomLevelWidth = window.innerWidth / 1879;
  const zoomLevelHeight = window.innerHeight / 939;
  let zoomLevel;
  if (window.innerWidth < window.innerHeight)
    zoomLevel =
      zoomLevelWidth < zoomLevelHeight ? zoomLevelHeight : zoomLevelWidth;
  else
    zoomLevel =
      zoomLevelWidth < zoomLevelHeight ? zoomLevelWidth : zoomLevelHeight;
  document.body.style.setProperty("-moz-transform", `scale(${zoomLevel})`);
  document.body.style.setProperty("zoom", `${zoomLevel * 100}%`);

  //Add a variable to control the ui if the user is logged in
  const [isLoggedIn, setIsLoggedIn] = React.useState(
    new Cookies().get("token") !== undefined
  );
  const [loggedInName, setLoggedInName] = React.useState(
    new Cookies().get("name")
  );
  const [loggedInPicture, setLoggedInPicture] = React.useState(
    new Cookies().get("picture")
  );

  const [isEventWorker, setIsEventWorker] = React.useState(
    new Cookies().get("isEventWorker")
  );
  const [isEventManager, setIsEventManager] = React.useState(
    new Cookies().get("isEventManager")
  );
  const [isAdmin, setIsAdmin] = React.useState(new Cookies().get("isAdmin"));

  const handleLogin = (
    status: boolean,
    userName: string | undefined,
    userPicture: string | undefined,
    isEventWorker: boolean | undefined,
    isEventManager: boolean | undefined,
    isAdmin: boolean | undefined
  ) => {
    setIsLoggedIn(status);
    setLoggedInName(userName);
    setLoggedInPicture(userPicture);
    setIsEventWorker(isEventWorker);
    setIsEventManager(isEventManager);
    setIsAdmin(isAdmin);
  };

  //Only not logged in users
  function onlyPublicUserRoutes(): ReactNode {
    if (!isLoggedIn) {
      return (
        <Route
          path="/login"
          element={
            <div>
              <LoginPage handleLogin={handleLogin} />
            </div>
          }
        />
      );
    }
  }

  //User logged in
  function privateUserRoutes(): ReactNode {
    if (isLoggedIn) {
      return (
        <Route
          path="/private"
          element={
            <div>
              <PrivateUserPage />
            </div>
          }
        />
      );
    }
  }

  //User is Event Worker
  function eventWorkerRoutes(): ReactNode {
    if (isEventWorker) {
      return (
        <>
          <Route
            path="/event-worker"
            element={
              <div>
                <EventWorkerPage />
              </div>
            }
          />
        </>
      );
    }
  }

  //User is Event Manager
  function eventManagerRoutes(): ReactNode {
    if (isEventWorker) {
      return (
        <>
          <Route
            path="/event-manager"
            element={
              <div>
                <EventManagerPage />
              </div>
            }
          />
        </>
      );
    }
  }

  //User is Admin
  function adminRoutes(): ReactNode {
    if (isAdmin) {
      return (
        <>
          <Route
            path="/admin"
            element={
              <div>
                <AdminPage />
              </div>
            }
          />
        </>
      );
    }
  }

  return (
    <Router>
      <div className={"background_v1"} />
      <div id={NON_TOP_NAV_BAR_ID}>
        <TopNavBar
          isLoggedIn={isLoggedIn}
          loggedInName={loggedInName}
          loggedInPicture={loggedInPicture}
          isEventWorker={isEventWorker}
          isEventManager={isEventManager}
          isAdmin={isAdmin}
        />
        <Routes>
          <Route
            path="/"
            element={
              <div>
                <HomePage isLoggedIn={isLoggedIn} />
              </div>
            }
          ></Route>
          {privateUserRoutes()}
          {eventWorkerRoutes()}
          {eventManagerRoutes()}
          {adminRoutes()}
          {onlyPublicUserRoutes()}
          <Route
            path="*"
            element={
              <div>
                <LostPage />
              </div>
            }
          ></Route>
        </Routes>
      </div>
    </Router>
  );
}

export default App;
