import React from "react";
import { Snackbar, Alert } from "@mui/material";
import { criticality, messageDto } from "../types";

type AlertComponentProps = {
  alerts: messageDto[];
};

const AlertComponent: React.FC<AlertComponentProps> = ({ alerts }) => {
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
    <>
      {alerts.map((alert, index) => (
        <Snackbar
          key={index}
          open={true}
          autoHideDuration={30000}
          anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
          style={{ bottom: `${index * 60 + 20}px` }} // Adjust the position for stacking
        >
          <Alert severity={mapCriticalityToSeverity(alert.criticality)}>
            {alert.message}
          </Alert>
        </Snackbar>
      ))}
    </>
  );
};

export default AlertComponent;
