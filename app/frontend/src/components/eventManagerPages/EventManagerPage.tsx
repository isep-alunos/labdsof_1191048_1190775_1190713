import React from "react";
import styles from "./EventManagerPage.module.css";
import { Link } from "react-router-dom";
import { ROUTES } from "../../routes";
import { FaCalendarPlus } from "react-icons/fa"; // Import an icon from react-icons

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
                <Link to={ROUTES.CREATE_EVENT} className={styles.cardLink}>
                    <div className={styles.card}>
                        <FaCalendarPlus className={styles.icon} />
                        <h3>Create Event</h3>
                    </div>
                </Link>
            </div>
        );
    }
}

export default EventManagerPage;
