import React, { createContext, useContext, useState, useCallback } from "react";
import { messageDto } from "../types";
import AlertComponent from "./AlertComponent";

type AlertContextType = {
  addAlert: (alert: messageDto) => void;
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

  return (
    <AlertContext.Provider value={{ addAlert }}>
      {children}
      <AlertComponent alerts={alerts} />
    </AlertContext.Provider>
  );
};
