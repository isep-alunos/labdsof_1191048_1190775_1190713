import React from "react";
import styles from "./AdminPage.module.css";
import { Link } from "react-router-dom";
import {ROUTES} from "../../../routes";

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
        <h1>Admin Page</h1>
        <div className={styles.AdminMenu}>
          <div className={styles.AdminMenuItem}>
            <h2>Approve Users</h2>
            <p>
              Sub-page where the administrator can approve all users who have requested an event manager or event worker role.
            </p>
            <Link to={ROUTES.APPROVE_USERS}>
              <button>Approve Users</button>
            </Link>
          </div>
          {/* Adicione mais itens de menu conforme necessário */}
        </div>
      </div>
    );
  }
}

export default AdminPage;
