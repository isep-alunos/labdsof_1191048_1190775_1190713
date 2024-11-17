import React from "react";
import styles from "./EventManagerPage.module.css";

type MyProps = {};
type MyState = {};

class EventManagerPage extends React.Component<MyProps, MyState> {
  constructor(props: MyProps) {
    super(props);
    this.state = {};
  }

  componentDidMount() {}

  componentWillUnmount() {}

  render() {
    return (
      <div className={styles.EventManagerPage}>
        <h2>TODO: Event Manager Page</h2>
      </div>
    );
  }
}

export default EventManagerPage;
