import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./LostPage.module.css";

const LostPage = () => {
  const navigate = useNavigate();

  useEffect(() => {
    navigate("/");
  }, [navigate]);

  return <div className={styles.LostPage}>Redirecting...</div>;
};

export default LostPage;
