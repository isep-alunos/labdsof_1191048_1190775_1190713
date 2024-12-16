import styles from "./AlertComponent.module.css";
import React from "react";
import { Snackbar, Alert, Button } from "@mui/material";
import { criticality, messageDto } from "../types";

type AlertComponentProps = {
  alerts: messageDto[];
  removeAlert: (index: number) => void;
};

const AlertComponent: React.FC<AlertComponentProps> = ({
  alerts,
  removeAlert,
}) => {
  function mapCriticalityToSeverity(
    crit: criticality
  ): "error" | "warning" | "info" | "success" {
    switch (crit.toString()) {
      case "ERROR":
        return "error";
      case "WARNING":
        return "warning";
      case "INFO":
        return "info";
      case "SUCCESS":
        return "success";
    }
    return "info";
  }

  return (
    <div className={styles.AlertComponent}>
      {alerts.map((alert, index) => (
        <Snackbar
          key={index}
          open={true}
          autoHideDuration={30000}
          anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
          style={{ bottom: `${index * 60 + 20}px` }} // Adjust the position for stacking
        >
          <Alert
            severity={mapCriticalityToSeverity(alert.criticality)}
            action={
              <Button
                className={styles.button}
                color="inherit"
                size="small"
                onClick={() => removeAlert(index)}
              >
                Close
              </Button>
            }
          >
            {alert.message}
          </Alert>
        </Snackbar>
      ))}
    </div>
  );
};

export default AlertComponent;
