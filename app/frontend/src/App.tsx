import HomePage from "./components/publicPages/HomePage/HomePage";
import AdminPage from "./components/adminPages/AdminPage/AdminPage";
import LoginPage from "./components/publicPages/LoginPage/LoginPage";
import LostPage from "./components/publicPages/LostPage/LostPage";
import TopNavBar from "./components/publicPages/TopNavBar/TopNavBar";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Cookies from "universal-cookie";
import ApproveUsersPage from "./components/adminPages/AdminPage/ApproveUsersPage/ApproveUsersPage";

import React, { ReactNode } from "react";
import EventWorkerPage from "./components/eventWorkerPages/EventWorkerPage/EventWorkerPage";
import PrivateUserPage from "./components/privatePages/PrivateUserPage/PrivateUserPage";
import EventManagerPage from "./components/eventManagerPages/EventManagerPage";
import { AlertProvider } from "./utils/alerts/AlertContext";
import CreateEvent from "./components/eventManagerPages/CreateEventPage/CreateEvent";
import { ROUTES } from "./routes";
import EventIssuesPage from "./components/publicPages/HomePage/EventIssuesPage/EventIssuesPage";
import CreateIssuePage from "./components/publicPages/HomePage/CreateIssuePage/CreateIssuePage";
import EventPage from "./components/eventWorkerPages/EventPage/EventPage";

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
    token: string | undefined,
    refreshToken: string | undefined,
    userName: string | undefined,
    userEmail: string | undefined,
    userPicture: string | undefined,
    isEventWorker: boolean | undefined,
    isEventManager: boolean | undefined,
    isAdmin: boolean | undefined,
    expireDate: Date | undefined
  ) => {
    setIsLoggedIn(status);
    setLoggedInName(userName);
    setLoggedInPicture(userPicture);
    setIsEventWorker(isEventWorker);
    setIsEventManager(isEventManager);
    setIsAdmin(isAdmin);
    const cookies = new Cookies();
    cookies.set("token", token, { path: "/" });
    cookies.set("refreshToken", refreshToken, { path: "/" });
    cookies.set("name", userName, { path: "/" });
    cookies.set("email", userEmail, { path: "/" });
    cookies.set("picture", userPicture, { path: "/" });
    cookies.set("isEventWorker", isEventWorker, { path: "/" });
    cookies.set("isEventManager", isEventManager, { path: "/" });
    cookies.set("isAdmin", isAdmin, { path: "/" });
    cookies.set("expireDate", expireDate, { path: "/" });
  };

  //Only not logged in users
  function onlyPublicUserRoutes(): ReactNode {
    if (!isLoggedIn) {
      return (
        <Route
          path={ROUTES.LOGIN}
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
        <>
          <Route
            path={ROUTES.PRIVATE_USER}
            element={
              <div>
                <PrivateUserPage />
              </div>
            }
          />
          <Route
            path={ROUTES.EVENT_ISSUES}
            element={
              <div>
                <EventIssuesPage />
              </div>
            }
          ></Route>

          <Route
            path={ROUTES.CREATE_ISSUE}
            element={
              <div>
                <CreateIssuePage />
              </div>
            }
          ></Route>
        </>
      );
    }
  }

  //User is Event Worker
  function eventWorkerRoutes(): ReactNode {
    if (isEventWorker) {
      return (
        <>
          <Route
            path={ROUTES.EVENT_WORKER}
            element={
              <div>
                <EventWorkerPage />
              </div>
            }
          />
          <Route
            path={ROUTES.EVENT_WORKER_EVENT_PAGE}
            element={
              <div>
                <EventPage />
              </div>
            }
          />
        </>
      );
    }
  }

  //User is Event Manager
  function eventManagerRoutes(): ReactNode {
    if (isEventManager) {
      return (
        <>
          <Route
            path={ROUTES.EVENT_MANAGER}
            element={
              <div>
                <EventManagerPage />
              </div>
            }
          />
          <Route
            path={ROUTES.CREATE_EVENT}
            element={
              <div>
                <CreateEvent />
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
            path={ROUTES.ADMIN}
            element={
              <div>
                <AdminPage />
              </div>
            }
          />
          <Route
            path={ROUTES.APPROVE_USERS}
            element={
              <div>
                <ApproveUsersPage />
              </div>
            }
          />
        </>
      );
    }
  }

  return (
    <AlertProvider>
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
          <div>
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
                path={ROUTES.LOST}
                element={
                  <div>
                    <LostPage />
                  </div>
                }
              ></Route>
            </Routes>
          </div>
        </div>
      </Router>
    </AlertProvider>
  );
}

export default App;
