import React, { useEffect, useRef } from "react";
import styles from "./LoginPage.module.css";
import { useNavigate, useSearchParams } from "react-router-dom";
import HttpService from "../../../utils/http";
import RegisterComponent from "./RegisterComponent/RegisterComponent";
import { tokenResponse } from "../../../utils/types";

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
  function homePage() {
    navigate("/");
  }

  useEffect(() => {
    if (initialLoad.current) {
      initialLoad.current = false; // Mark as loaded after the first execution
      const fetchToken = async () => {
        const code = queryParameters.get("code");
        if (!code) {
          //navigate("/");
          //TODO: UNCOMENT THIS LINE AND ADD A POP UP MESSAGE
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
            //TODO: INFORM USER THAT HE IS WAITING FOR APPROVAL FOR EVENT WORKER AND/OR EVENT MANAGER
          }
          navigate("/");
        }
        if (!tokenResponse2.success) {
          // navigate("/");
          //TODO: UNCOMENT THIS LINE AND ADD A POP UP MESSAGE
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
      <div className="test">
        <h2>Token Response</h2>
        <button
          onClick={() => {
            console.log(tokenResponse);
            tokenResponse.isNewUser = true;
            setTokenResponse({
              success: true,
              waiting_for_response: false,
              isNewUser: true,
            });
          }}
        >
          Do something!
        </button>
        {tokenResponse && (
          <>
            <p>
              Waiting for response: {tokenResponse.waiting_for_response + ""}
            </p>
            <p>Success: {tokenResponse.success + ""}</p>
            <p>Token: {tokenResponse.token}</p>
            <p>Refresh Token: {tokenResponse.refreshToken}</p>
            <p>Name: {tokenResponse.name}</p>
            <p>Email: {tokenResponse.email}</p>
            <p>Picture: {tokenResponse.picture}</p>
            <p>Is New User: {tokenResponse.isNewUser + ""}</p>
            <p>Is Event Worker: {tokenResponse.isEventWorker + ""}</p>
            <p>Is Event Manager: {tokenResponse.isEventManager + ""}</p>
            <p>Is Admin: {tokenResponse.isAdmin + ""}</p>
            <p>Expire Date: {tokenResponse.expireDate + ""}</p>
          </>
        )}
      </div>
    </div>
  );
};

export default LoginPage;
