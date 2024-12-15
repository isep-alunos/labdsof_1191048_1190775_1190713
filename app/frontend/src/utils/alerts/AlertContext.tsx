import React, { createContext, useContext, useState, useCallback } from "react";
import { messageDto } from "../types";
import AlertComponent from "./AlertComponent";

type AlertContextType = {
  addAlert: (alert: messageDto) => void;
  removeAlert: (index: number) => void;
};

export const AlertContext = createContext<AlertContextType | undefined>(
  undefined
);

export const useAlert = () => {
  const context = useContext(AlertContext);
  if (!context) {
    throw new Error("useAlert must be used within an AlertProvider");
  }
  return context;
};

export const AlertProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [alerts, setAlerts] = useState<messageDto[]>([]);

  const addAlert = useCallback((alert: messageDto) => {
    setAlerts((prevAlerts) => [...prevAlerts, alert]);
    setTimeout(() => {
      setAlerts((prevAlerts) => prevAlerts.filter((a) => a !== alert));
    }, 30000);
  }, []);

  const removeAlert = useCallback((index: number) => {
    setAlerts((prevAlerts) => prevAlerts.filter((_, i) => i !== index));
  }, []);

  return (
    <AlertContext.Provider value={{ addAlert, removeAlert }}>
      {children}
      <AlertComponent alerts={alerts} removeAlert={removeAlert} />
    </AlertContext.Provider>
  );
};
