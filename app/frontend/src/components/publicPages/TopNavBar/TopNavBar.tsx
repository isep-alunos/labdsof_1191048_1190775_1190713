import React, { useEffect, useState } from "react";
import Cookies from "universal-cookie";
import styles from "./TopNavBar.module.css";
import { Link, useLocation } from "react-router-dom";
import HttpService from "../../../utils/http";
import { authUrlResponse } from "../../../utils/types";

const NAV_MAX_HEIGHT_PX: number = 106;
const NAV_MIN_HEIGHT_PX = 56;
const NAV_SPEED = 10;

type MyProps = {
  isLoggedIn: boolean;
  loggedInName: string;
  loggedInPicture: string;
  isEventWorker: boolean;
  isEventManager: boolean;
  isAdmin: boolean;
};

const TopNavBar: React.FC<MyProps> = (props) => {
  const [dynamicSize, setDynamicSize] = useState(NAV_MAX_HEIGHT_PX);
  const [authURL, setAuthURL] = useState<string | null>(null);
  const location = useLocation();
  let timerID: NodeJS.Timer | undefined;
  let isOnTop = true;

  const decrementSize = () => {
    setDynamicSize((prevSize) => {
      if (prevSize > NAV_MIN_HEIGHT_PX) return prevSize - 2;
      clearInterval(timerID);
      return prevSize;
    });
  };

  const incrementSize = () => {
    setDynamicSize((prevSize) => {
      if (prevSize < NAV_MAX_HEIGHT_PX) return prevSize + 2;
      clearInterval(timerID);
      return prevSize;
    });
  };

  useEffect(() => {
    document.body.classList.add("topnavbar");

    const handleScroll = () => {
      if ((window.pageYOffset === 0) !== isOnTop) {
        isOnTop = window.pageYOffset === 0;
        if (isOnTop) {
          clearInterval(timerID);
          timerID = setInterval(incrementSize, NAV_SPEED);
        } else {
          clearInterval(timerID);
          timerID = setInterval(decrementSize, NAV_SPEED);
        }
      }
    };

    window.addEventListener("scroll", handleScroll);

    let http = new HttpService();
    http.getPublic<authUrlResponse>("/auth/url").then((data) => {
      if (data.body) setAuthURL(data.body.authURL);
    });

    return () => {
      document.body.classList.remove("topnavbar");
      window.removeEventListener("scroll", handleScroll);
      clearInterval(timerID);
    };
  }, []);

  const handleLogin = () => {
    console.log("Login process started. Setting cookies");
    console.log("Redirecting to the authURL:", authURL);
    if (authURL) {
      window.location.href = authURL;
    }
  };

  const handleLogout = () => {
    console.log("Logout process started");
    const cookies = new Cookies();
    console.log("Clearing cookies");
    cookies.remove("token");
    cookies.remove("refreshToken");
    cookies.remove("name");
    cookies.remove("picture");
    cookies.remove("isEventWorker");
    cookies.remove("isEventManager");
    cookies.remove("isAdmin");

    let http = new HttpService();
    http
      .getPrivate("/auth/logout?refreshToken=" + cookies.get("refreshToken"))
      .then((data) => {
        if (data.code === 204) {
          console.log("Server logout successful!");
        } else {
          console.log(data.body);
        }
      })
      .finally(() => {
        window.location.href = "/";
      });
  };

  const isLoginPage = location.pathname === "/login";

  let privateOptions = null;
  let eventWorkerOptions = null;
  let eventManagerOptions = null;
  let adminOptions = null;
  let loginOption;

  if (!isLoginPage && props.isLoggedIn) {
    privateOptions = (
      <Link to="/private">
        <div className="color_v1 hover">Área Pessoal</div>
      </Link>
    );
    if (props.isEventWorker) {
      eventWorkerOptions = (
        <Link to="/event-worker">
          <div className="color_v1 hover">Event Worker</div>
        </Link>
      );
    }
    if (props.isEventManager) {
      eventManagerOptions = (
        <Link to="/event-manager">
          <div className="color_v1 hover">Event Manager</div>
        </Link>
      );
    }
    if (props.isAdmin) {
      adminOptions = (
        <Link to="/admin">
          <div className="color_v1 hover">Admin</div>
        </Link>
      );
    }

    loginOption = (
      <div className={styles.TopNavBarUserInfo}>
        <div>
          <h3 className="color_v1">{props.loggedInName}</h3>
          <h4 onClick={handleLogout} className="color_v1 hover">
            Logout
          </h4>
        </div>
        <img
          src={props.loggedInPicture}
          alt={props.loggedInName}
          height={dynamicSize * 0.6}
        />
      </div>
    );
  } else if (!isLoginPage) {
    loginOption = (
      <div onClick={handleLogin} className="color_v1 hover">
        Login
      </div>
    );
  }

  return (
    <div
      className={
        dynamicSize === NAV_MAX_HEIGHT_PX
          ? "background_fade_v2_low " + styles.TopNavBar
          : "background_v2 " + styles.TopNavBar
      }
      style={{ height: dynamicSize + "px" }}
    >
      <Link to="/">
        <div
          className={styles.TopNavBarLogo}
          style={{ height: dynamicSize + "px" }}
        >
          <img
            src={"../imgs/logo.png"}
            height={dynamicSize}
            alt="Porto Eventos logo"
          />
          <div>
            <h2 style={{ fontSize: dynamicSize / 4 + "px" }}>PortoEventos.</h2>
            <h4 style={{ fontSize: dynamicSize / 4 + "px" }}>
              Issue Reporting
            </h4>
          </div>
        </div>
      </Link>
      {!isLoginPage && (
        <div
          className={styles.TopNavBarContent}
          style={{ height: dynamicSize + "px" }}
        >
          {privateOptions}
          {eventWorkerOptions}
          {eventManagerOptions}
          {adminOptions}
          {loginOption}
        </div>
      )}
    </div>
  );
};

export default TopNavBar;
