import React, { useState } from "react";
import { useAlert } from "../../../../utils/alerts/AlertContext"; // Import the useAlert hook
import {
  criticality,
  messageDto,
  registerResponse,
} from "../../../../utils/types";
import HttpService from "../../../../utils/http";
import styles from "./RegisterComponent.module.css";
import { tokenResponse } from "../../../../utils/types";

type MyProps = {
  tokenResponse: tokenResponse;
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
  homePage: () => void;
};

const RegisterComponent: React.FC<MyProps> = ({
  tokenResponse,
  handleLogin,
  homePage,
}) => {
  const [isEventWorker, setIsEventWorker] = useState(false);
  const [isEventManager, setIsEventManager] = useState(false);
  const [agreeToStoreData, setAgreeToStoreData] = useState(false);
  const [eventWorkerInfo, setEventWorkerInfo] = useState("");
  const [eventManagerInfo, setEventManagerInfo] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const alert = useAlert(); // Use the useAlert hook

  const sendAlert = (message: messageDto) => {
    alert.addAlert(message);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    const http = new HttpService();
    http
      .getPublic<registerResponse>(
        `/auth/register?email=${tokenResponse.email}&name=${tokenResponse.name}&isEventWorker=${isEventWorker}&eventWorkerInfo=${eventWorkerInfo}&isEventManager=${isEventManager}&eventManagerInfo=${eventManagerInfo}`
      )
      .then((data) => {
        console.log("Form submitted");
        console.log("response", data);
        if (data.body !== undefined) {
          if (data.code === 201) {
            handleLogin(
              true,
              tokenResponse.token,
              tokenResponse.refreshToken,
              tokenResponse.name,
              tokenResponse.email,
              tokenResponse.picture,
              isEventWorker,
              isEventManager,
              tokenResponse.isAdmin,
              tokenResponse.expireDate
            );
          }
          for (let message of data.body.messages ?? []) {
            console.log("Message:", message);
            sendAlert(message);
          }
        } else {
          sendAlert({
            message:
              "Something went wrong during the registration, please retry. If the error continues, please contact us.",
            criticality: criticality.ERROR,
          });
        }
      })
      .catch((error) => {
        console.error("Error submitting form:", error);
      });
    homePage();
  };

  return (
    <div className={styles.RegisterComponent}>
      <div>
        <div>
          <div>
            <h1>Registo de conta</h1>
            <form id="register-form" onSubmit={handleSubmit}>
              <div>
                <label htmlFor="name">Nome</label>
                <input
                  type="text"
                  name="name"
                  placeholder="If you're seeing this, something went wrong"
                  disabled={true}
                  defaultValue={tokenResponse.name}
                />
              </div>
              <div>
                <label htmlFor="email">Email</label>
                <input
                  type="email"
                  name="email"
                  placeholder="If you're seeing this, something went wrong"
                  disabled={true}
                  defaultValue={tokenResponse.email}
                />
              </div>
              <div>
                <label htmlFor="agreeToStoreData">
                  <input
                    type="checkbox"
                    name="agreeToStoreData"
                    disabled={isSubmitting}
                    checked={agreeToStoreData}
                    onChange={(e) => setAgreeToStoreData(e.target.checked)}
                    required
                  />
                  Aceita partilhar o seu nome e email connosco?
                </label>
              </div>
              <div>
                <label htmlFor="isEventWorker">
                  <input
                    type="checkbox"
                    name="isEventWorker"
                    disabled={isSubmitting}
                    checked={isEventWorker}
                    onChange={(e) => setIsEventWorker(e.target.checked)}
                  />
                  É trabalhador em algum evento?
                </label>
              </div>
              {isEventWorker && (
                <div className={styles.role_justification}>
                  <label htmlFor="justificationEventWorker">
                    Por favor, forneça a sua informação para que um Gestor de
                    Eventos possa verificar e aprovar a sua conta. Será
                    notificado quando a sua conta for criada para "
                    {tokenResponse.email}".
                    <textarea
                      name="justificationEventWorker"
                      disabled={!isEventWorker || isSubmitting}
                      required
                      value={eventWorkerInfo}
                      autoCapitalize="sentences"
                      autoCorrect="on"
                      wrap="hard"
                      form="register-form"
                      minLength={20}
                      onChange={(e) => setEventWorkerInfo(e.target.value)}
                    />
                    Nota: Minimo de 20 caracteres
                  </label>
                </div>
              )}
              <div>
                <label htmlFor="isEventManager">
                  <input
                    type="checkbox"
                    name="isEventManager"
                    checked={isEventManager}
                    disabled={isSubmitting}
                    onChange={(e) => setIsEventManager(e.target.checked)}
                  />
                  É um gestor de evento(s)?
                </label>
              </div>
              {isEventManager && (
                <div className={styles.role_justification}>
                  <label htmlFor="justificationEventManager">
                    Por favor, forneça a sua informação para que um
                    Administrador possa verificar e aprovar a sua conta. Será
                    notificado quando a sua conta for criada para "
                    {tokenResponse.email}".
                    <textarea
                      name="justificationEventManager"
                      disabled={!isEventManager || isSubmitting}
                      required
                      autoCapitalize="sentences"
                      autoCorrect="on"
                      wrap="hard"
                      form="register-form"
                      value={eventManagerInfo}
                      minLength={20}
                      onChange={(e) => setEventManagerInfo(e.target.value)}
                    />
                    Nota: Minimo de 20 caracteres
                  </label>
                </div>
              )}
              <button
                className="background_v2"
                type="submit"
                disabled={
                  !agreeToStoreData ||
                  isSubmitting ||
                  (isEventWorker && eventWorkerInfo.length < 20) ||
                  (isEventManager && eventManagerInfo.length < 20)
                }
              >
                Registar
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RegisterComponent;
