import React, { useEffect, useRef } from "react";
import styles from "./LoginPage.module.css";
import { useNavigate, useSearchParams } from "react-router-dom";
import HttpService from "../../../utils/http";

// Destructure handleLogin from props
const LoginPage = ({
  handleLogin,
}: {
  handleLogin: (
    success: boolean,
    userName: string | undefined,
    userPicture: string | undefined,
    isEventWorker: boolean | undefined,
    isEventManager: boolean | undefined,
    isAdmin: boolean | undefined
  ) => void;
}) => {
  const navigate = useNavigate();
  const [queryParameters] = useSearchParams();
  const initialLoad = useRef(true); // useRef to track the initial load

  useEffect(() => {
    const loginSuccess = (
      userName: string | undefined,
      userPicture: string | undefined,
      isEventWorker: boolean | undefined,
      isEventManager: boolean | undefined,
      isAdmin: boolean | undefined
    ) => {
      // Call this function when login is successful
      handleLogin(
        true,
        userName,
        userPicture,
        isEventWorker,
        isEventManager,
        isAdmin
      );
    };
    if (initialLoad.current) {
      initialLoad.current = false; // Mark as loaded after the first execution
      const fetchToken = async () => {
        const code = queryParameters.get("code");
        const http = new HttpService();
        const tokenResponse = await http.getToken(code ?? "");
        if (tokenResponse.success) {
          loginSuccess(
            tokenResponse.name,
            tokenResponse.picture,
            tokenResponse.isEventWorker,
            tokenResponse.isEventManager,
            tokenResponse.isAdmin
          );
        }
        navigate("/");
      };

      fetchToken();
    }
  }, [handleLogin, navigate, queryParameters]); // Keep queryParameters to react to changes, but guard with initialLoad

  return (
    <div className={styles.LoginPage}>
      Caso fique preso nesta página, tente repetir o processo de login.
    </div>
  );
};

export default LoginPage;
