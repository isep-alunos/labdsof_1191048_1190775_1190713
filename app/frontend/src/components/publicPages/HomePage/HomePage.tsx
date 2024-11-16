import React from "react";
import styles from "./HomePage.module.css";
import { FiChevronDown } from "react-icons/fi";
import HomePageContent from "./HomePageContent/HomePageContent";

const HOME_PAGE_BODY_ID = "HomePageBodyID";

type MyProps = { isLoggedIn: boolean };
type MyState = { isLoggedIn: boolean };
class HomePage extends React.Component<MyProps, MyState> {

  constructor(props: MyProps | Readonly<MyProps>) {
    super(props);
    this.state = {
      isLoggedIn: props.isLoggedIn
    };
  }

  componentDidMount() {}

  componentWillUnmount() {}

  scrollToBody() {
    const element = document.getElementById(HOME_PAGE_BODY_ID);
    if (element) element.scrollIntoView({ block: "start", behavior: "smooth" });
  }

  render() {
    if (window.location.search !== "") {
      window.location.search = "";
    }
    return (
      <div className={styles.HomePage}>
        <div className={styles.HomePageBackgroundImage}>
          <img src={"imgs/home_page.jpg"} alt="tras dos montes" />
          <div className="background_fade_v2_to_v1" />
        </div>
        <div className={styles.HomePageHelloWord}>
          <h1>PortoEventos.</h1>
          <h2>
            Todos os eventos do Porto num só lugar.
            <p>Encontra o teu evento e participa!</p>
          </h2>
          <FiChevronDown onClick={this.scrollToBody} />
        </div>
        <div className={styles.HomePageContentMarker} id={HOME_PAGE_BODY_ID} />
        <div className={styles.HomePageNonHello}>
          <div className={styles.HomePageContent + " border_v1"}>
            <HomePageContent isLoggedIn= { this.state.isLoggedIn }/>
          </div>
        </div>
      </div>
    );
  }
}

export default HomePage;
