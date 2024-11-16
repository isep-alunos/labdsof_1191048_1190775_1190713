import React from "react";
import styles from "./HomePageContent.module.css";

type MyProps = { isLoggedIn: boolean };
type MyState = {};

class HomePageContent extends React.Component<MyProps, MyState> {
  constructor(props: MyProps) {
    super(props);
    this.state = {};
  }

  componentDidMount() {}

  componentWillUnmount() {}

  render() {
    return (
      <div className={styles.HomePageContent}>
        <h2>TODO: HomePage Content</h2>
      </div>
    );
  }
}

export default HomePageContent;
