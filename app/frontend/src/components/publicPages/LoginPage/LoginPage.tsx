import React, { useEffect, useRef } from "react";
import styles from "./LoginPage.module.css";
import { useNavigate, useSearchParams } from "react-router-dom";
import HttpService from "../../../utils/http";
import RegisterComponent from "./RegisterComponent/RegisterComponent";
import { criticality, messageDto, tokenResponse } from "../../../utils/types";
import { useAlert } from "../../../utils/alerts/AlertContext";

// Destructure handleLogin from props
const LoginPage = ({
  handleLogin,
}: {
  handleLogin: (
    status: boolean,
    token: string | undefined,
    refreshToken: string | undefined,
    userName: string | undefined,
    userEmail: string | undefined,
    userPicture: string | undefined,
    isEventWorker: boolean | undefined,
    isEventManager: boolean | undefined,
    isAdmin: boolean | undefined,
    expireDate: Date | undefined
  ) => void;
}) => {
  const navigate = useNavigate();
  const [queryParameters] = useSearchParams();
  const initialLoad = useRef(true); // useRef to track the initial load
  const [tokenResponse, setTokenResponse] = React.useState<tokenResponse>({
    waiting_for_response: true,
    success: false,
  });
  const alert = useAlert(); // Use the useAlert hook

  const sendAlert = (message: messageDto) => {
    alert.addAlert(message);
  };
  function homePage() {
    navigate("/");
  }

  useEffect(() => {
    if (initialLoad.current) {
      initialLoad.current = false; // Mark as loaded after the first execution
      const fetchToken = async () => {
        const code = queryParameters.get("code");
        if (!code) {
          sendAlert({
            message:
              "Something went wrong during the login, please retry. " +
              "If the error continues, please contact us.",
            criticality: criticality.ERROR,
          });
          navigate("/");
        }
        const http = new HttpService();
        const tokenResponse2 = await http.getToken(code ?? "");
        setTokenResponse(tokenResponse2); // Update state with tokenResponse
        if (tokenResponse2.success && !tokenResponse2.isNewUser) {
          if (
            !tokenResponse2.isWaitingForApprovalForEventWorker &&
            !tokenResponse2.isWaitingForApprovalForEventManager
          ) {
            handleLogin(
              true,
              tokenResponse2.token,
              tokenResponse2.refreshToken,
              tokenResponse2.name,
              tokenResponse2.email,
              tokenResponse2.picture,
              tokenResponse2.isEventWorker,
              tokenResponse2.isEventManager,
              tokenResponse2.isAdmin,
              tokenResponse2.expireDate
            );
          } else {
            sendAlert({
              message:
                "You are waiting for approval to be an event worker and/or event manager. Your account will be unlocked after that process.",
              criticality: criticality.INFO,
            });
          }
          navigate("/");
        }
        if (!tokenResponse2.success) {
          sendAlert({
            message:
              "Something went wrong during the login, please retry. " +
              "If the error continues, please contact us.",
            criticality: criticality.ERROR,
          });
          navigate("/");
        }
      };

      fetchToken();
    }
  }, [handleLogin, navigate, queryParameters, setTokenResponse]); // Keep queryParameters to react to changes, but guard with initialLoad

  return (
    <div className={styles.LoginPage}>
      {tokenResponse.isNewUser && (
        <RegisterComponent
          tokenResponse={tokenResponse}
          handleLogin={handleLogin}
          homePage={homePage}
        />
      )}
    </div>
  );
};

export default LoginPage;
