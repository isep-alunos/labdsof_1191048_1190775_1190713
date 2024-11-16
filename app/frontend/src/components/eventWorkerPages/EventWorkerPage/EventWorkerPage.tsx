import React from "react";
import styles from "./EventWorkerPage.module.css";

type MyProps = {};
type MyState = {};

class EventWorkerPage extends React.Component<MyProps, MyState> {
  constructor(props: MyProps) {
    super(props);
    this.state = {};
  }

  componentDidMount() {}

  componentWillUnmount() {}

  render() {
    return (
      <div className={styles.EventWorkerPage}>
        <h2>TODO: Event Worker Page</h2>
      </div>
    );
  }
}

export default EventWorkerPage;
