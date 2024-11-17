import React from "react";
import styles from "./AdminPage.module.css";
import { Link } from "react-router-dom";

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
        <h1>Página de Administração</h1>
        <div className={styles.AdminMenu}>
          <div className={styles.AdminMenuItem}>
            <h2>Aprovar Utilizadores</h2>
            <p>
              Sub-página onde o administrador pode aprovar todos os utilizadores que solicitaram um papel de gestor de eventos ou trabalhador de eventos.
            </p>
            <Link to="/admin/approve-users">
              <button>Aprovar Utilizadores</button>
            </Link>
          </div>
          {/* Adicione mais itens de menu conforme necessário */}
        </div>
      </div>
    );
  }
}

export default AdminPage;
