import React from "react";
import styles from "./AdminPage.module.css";

type MyProps = {};
type MyState = {};

class AdminPage extends React.Component<MyProps, MyState> {
  constructor(props: MyProps) {
    super(props);
    this.state = {};
  }

  componentDidMount() {}

  componentWillUnmount() {}

  render() {
    return (
      <div className={styles.AdminPage}>
        <h2>TODO: Admin Page</h2>
      </div>
    );
  }
}

export default AdminPage;
