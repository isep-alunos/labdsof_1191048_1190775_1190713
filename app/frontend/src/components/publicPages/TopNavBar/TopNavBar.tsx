import React from "react";
import Cookies from "universal-cookie";
import styles from "./TopNavBar.module.css";
import { Link } from "react-router-dom";
import HttpService from "../../../utils/http";

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
type MyState = {
  dynamicSize: number;
  authURL: string | null;
  isLoggedIn: boolean;
  loggedInName: string;
  loggedInPicture: string;
  isEventWorker: boolean;
  isEventManager: boolean;
  isAdmin: boolean;
};
class TopNavBar extends React.Component<MyProps, MyState> {
  isOnTop: boolean;
  timerID: NodeJS.Timer | undefined;

  constructor(props: MyProps | Readonly<MyProps>) {
    super(props);
    this.state = {
      dynamicSize: NAV_MAX_HEIGHT_PX,
      authURL: null,
      isLoggedIn: props.isLoggedIn,
      loggedInName: props.loggedInName,
      loggedInPicture: props.loggedInPicture,
      isEventWorker: props.isEventWorker,
      isEventManager: props.isEventManager,
      isAdmin: props.isAdmin,
    };
    this.isOnTop = true;
  }

  decrementSize = () => {
    if (this.state.dynamicSize > NAV_MIN_HEIGHT_PX)
      this.setState({ dynamicSize: this.state.dynamicSize - 2 });
    else clearInterval(this.timerID);
  };
  incrementSize = () => {
    if (this.state.dynamicSize < NAV_MAX_HEIGHT_PX)
      this.setState({ dynamicSize: this.state.dynamicSize + 2 });
    else clearInterval(this.timerID);
  };

  componentDidMount() {
    document.body.classList.add("topnavbar");
    window.addEventListener("scroll", (event) => {
      if ((window.pageYOffset === 0) !== this.isOnTop) {
        this.isOnTop = window.pageYOffset === 0;
        if (this.isOnTop) {
          clearInterval(this.timerID);
          this.timerID = setInterval(() => this.incrementSize(), NAV_SPEED);
        } else {
          clearInterval(this.timerID);
          this.timerID = setInterval(() => this.decrementSize(), NAV_SPEED);
        }
      }
    });
    let http = new HttpService();
    http.get<authUrlResponse>("/auth/url").then((data) => {
      if (data.body)
        this.setState({
          authURL: data.body.authURL,
        }); // Save the authURL in the component's state
    });
  }

  componentWillUnmount() {
    document.body.classList.remove("topnavbar");
    window.onscroll = null;
    clearInterval(this.timerID);
  }

  handleLogin = () => {
    console.log("Login process started");
    console.log("Redirecting to the authURL:", this.state);
    // Redirect to the saved authURL when the login button is clicked
    if (this.state.authURL) {
      window.location.href = this.state.authURL; // Use window.location.href for redirection
    }
  };

  handleLogout = () => {
    console.log("Logout process started");
    const cookies = new Cookies();

    let http = new HttpService();
    http
      .getPrivate("/auth/logout?refreshToken=" + cookies.get("refreshToken"))
      .then((data) => {
        if (data.code === 204) {
          console.log("Server logout successfull!");
          console.log("Clearing cookies");
          cookies.remove("token"); // Remove the token cookie
          cookies.remove("refreshToken"); // Remove the refreshToken cookie
          cookies.remove("name"); // Remove the name cookie
          cookies.remove("picture"); // Remove the picture cookie
          cookies.remove("isEventWorker"); // Remove the isEventWorker cookie
          cookies.remove("isEventManager"); // Remove the isEventManager cookie
          cookies.remove("isAdmin"); // Remove the isAdmin cookie
          window.location.pathname = "/"; // redirect to home page
        } else {
          console.log(data.body);
        }
      });
  };

  render() {
    let privateOptions = null;
    let eventWorkerOptions = null;
    let eventManagerOptions = null;
    let adminOptions = null;
    let loginOption;
    if (this.props.isLoggedIn) {
      privateOptions = (
        <Link to="/private">
          <div className="color_v1 hover">Minhas Reservas</div>
        </Link>
      );
      if (this.props.isEventWorker)
        eventWorkerOptions = (
          <Link to="/event-worker">
            <div className="color_v1 hover">Event Worker</div>
          </Link>
        );
      if (this.props.isEventManager)
        eventManagerOptions = (
          <Link to="/event-manager">
            <div className="color_v1 hover">Event Manager</div>
          </Link>
        );
      if (this.props.isAdmin)
        adminOptions = (
          <Link to="/admin">
            <div className="color_v1 hover">Admin</div>
          </Link>
        );
      loginOption = (
        <div className={styles.TopNavBarUserInfo}>
          <div>
            <h3 className="color_v1">{this.props.loggedInName}</h3>
            <h4 onClick={this.handleLogout} className="color_v1 hover">
              Logout
            </h4>
          </div>
          <img
            src={this.props.loggedInPicture}
            alt={this.props.loggedInName}
            height={this.state.dynamicSize * 0.6}
          />
        </div>
      );
    } else {
      loginOption = (
        <div onClick={this.handleLogin} className="color_v1 hover">
          Login
        </div>
      );
    }

    return (
      <div
        className={
          this.state.dynamicSize === NAV_MAX_HEIGHT_PX
            ? "background_fade_v2_low " + styles.TopNavBar
            : "background_v2 " + styles.TopNavBar
        }
        style={{ height: this.state.dynamicSize + "px" }}
      >
        <Link to="/">
          <div
            className={styles.TopNavBarLogo}
            style={{ height: this.state.dynamicSize + "px" }}
          >
            <img
              src={"../imgs/logo.png"}
              height={this.state.dynamicSize}
              alt="zeHoteisLogo"
            />
            <div>
              <h2 style={{ fontSize: this.state.dynamicSize / 4 + "px" }}>
                PortoEventos.
              </h2>
              <h4 style={{ fontSize: this.state.dynamicSize / 4 + "px" }}>
                Todos os eventos do Porto num só lugar.
              </h4>
            </div>
          </div>
        </Link>
        <div
          className={styles.TopNavBarContent}
          style={{ height: this.state.dynamicSize + "px" }}
        >
          {privateOptions}
          {eventWorkerOptions}
          {eventManagerOptions}
          {adminOptions}
          {loginOption}
        </div>
      </div>
    );
  }
}

export default TopNavBar;
