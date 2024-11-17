import React from "react";
import styles from "./PrivateUserPage.module.css";

type MyProps = {};
type MyState = {};

class PrivateUserPage extends React.Component<MyProps, MyState> {
  constructor(props: MyProps) {
    super(props);
    this.state = {};
  }

  componentDidMount() {}

  componentWillUnmount() {}

  render() {
    return (
      <div className={styles.PrivateUserPage}>
        <h2>TODO: Private User Page</h2>
      </div>
    );
  }
}

export default PrivateUserPage;
