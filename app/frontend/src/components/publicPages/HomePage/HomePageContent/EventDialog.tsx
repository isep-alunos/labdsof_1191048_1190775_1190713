import React, { useState, useEffect } from "react";
import { Dialog, DialogTitle, DialogContent, DialogActions, Button } from "@mui/material";
import { LoadScript, GoogleMap, Marker } from "@react-google-maps/api";
import { criticality, eventDto, markPresenceRequest, markPresenceResponse } from "../../../../utils/types";
import HttpService from "../../../../utils/http";
import { useAlert } from "../../../../utils/alerts/AlertContext";
import { useNavigate } from "react-router-dom";
import styles from "./EventDialog.module.css";

const EventDialog: React.FC<{
  event: eventDto | null;
  open: boolean;
  onClose: () => void;
  isLoggedIn: boolean;
}> = ({ event, open, onClose, isLoggedIn }) => {
  const [isButtonDisabled, setButtonDisabled] = useState(false);
  const [isIssuesButtonDisabled, setIssuesButtonDisabled] = useState(false);
  const [eventMap, setEventMap] = useState<string | null>(null);
  const alert = useAlert();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchEventMap = async () => {
      if (event) {
        try {
          const http = new HttpService();
          const response = await http.getPublicImage(`/public/events/${event.name}/map`);
          if (response.code === 404) {
            setEventMap(null);
          } else if (response.body) {
            setEventMap(URL.createObjectURL(response.body));
          }
        } catch (error) {
          console.error("Error fetching event map:", error);
        }
      }
    };

    fetchEventMap();
  }, [event]);

  const handleMarkPresence = async () => {
    if (!isLoggedIn) {
      alert.addAlert({
        message: "You must be logged in to mark presence at event.",
        criticality: criticality.ERROR,
      });
      setButtonDisabled(true);
      return;
    }

    if (!navigator.geolocation) {
      alert.addAlert({
        message: "Geolocation is not supported by your browser.",
        criticality: criticality.ERROR,
      });
      return;
    }

    navigator.geolocation.getCurrentPosition(
      async (position) => {
        const userLocation = {
          lat: position.coords.latitude,
          lng: position.coords.longitude,
        };

        console.log(userLocation);

        const body: markPresenceRequest = {
          name: event?.name ?? "",
          latitude: userLocation.lat,
          longitude: userLocation.lng,
        };

        const http = new HttpService();
        const response = await http.putPrivate<markPresenceResponse>(
          "/private/markPresence",
          body
        );
        if (response.body) {
          for (let message of response.body.messages ?? []) {
            alert.addAlert(message);
          }
          if (response.body.success) {
            alert.addAlert({
              message: "Successfully marked presence at event.",
              criticality: criticality.SUCCESS,
            });
          }
        }
      },
      (error) => {
        alert.addAlert({
          message: `Failed to get location: ${error.message}`,
          criticality: criticality.ERROR,
        });
      }
    );
  };

  const handleIssuesClick = () => {
    if (!isLoggedIn) {
      alert.addAlert({
        message: "You must be logged in to to see the event's issues.",
        criticality: criticality.ERROR,
      });
      setIssuesButtonDisabled(true);
      return;
    }

    if (event) {
      navigate(`/events/${encodeURIComponent(event.name)}/issues`);
    }
  };

  const mapContainerStyle = { height: "300px", width: "100%" };
  const position = event?.location
    ? {
        lat: event.location.latitude,
        lng: event.location.longitude,
      }
    : { lat: 0, lng: 0 };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>Event Details</DialogTitle>
      <DialogContent>
        {event ? (
          <div className={styles.EventDetails}>
            <div className={styles.DetailRow}>
              <span className={styles.Label}><strong>Name:</strong></span>
              <span className={styles.Value}>{event.name}</span>
            </div>
            <div className={styles.DetailRow}>
              <span className={styles.Label}><strong>Description:</strong></span>
              <span className={styles.Value}>{event.description}</span>
            </div>
            <div className={styles.DetailRow}>
              <span className={styles.Label}><strong>Start Date:</strong></span>
              <span className={styles.Value}>{new Date(event.startDate).toLocaleString()}</span>
            </div>
            <div className={styles.DetailRow}>
              <span className={styles.Label}><strong>End Date:</strong></span>
              <span className={styles.Value}>{new Date(event.endDate).toLocaleString()}</span>
            </div>
            <div className={styles.DetailRow}>
              <span className={styles.Label}><strong>Max Participants:</strong></span>
              <span className={styles.Value}>{event.maxParticipants}</span>
            </div>
            <div className={styles.DetailRow}>
              <span className={styles.Label}><strong>Event Website:</strong></span>
              <span className={styles.Value}>
                <a href={event.eventWebsite} target="_blank" rel="noopener noreferrer">
                  {event.eventWebsite}
                </a>
              </span>
            </div>
            <div className={styles.DetailRow}>
              <span className={styles.Label}><strong>Location:</strong></span>
              <span className={styles.Value}>
                {event.location.street}, {event.location.number}, {event.location.postalCode}
              </span>
            </div>
            <div className={styles.DetailRow}>
              <span className={styles.Label}><strong>Coordinates:</strong></span>
              <span className={styles.Value}>
                {event.location.latitude}, {event.location.longitude}
              </span>
            </div>
            <div className={styles.DetailRow}>
              <span className={styles.Label}><strong>Event Workers:</strong></span>
              <span className={styles.Value}>{event.eventWorkerNames.join(", ")}</span>
            </div>
            <div className={styles.LocationMap}>
              <h2>Location Map</h2>
              <div className={styles.MapContainer}>
                <LoadScript googleMapsApiKey="AIzaSyBRjYJ-6-HMhU-_s9GOuT5t9B4cfJKY6KY">
                  <GoogleMap
                    mapContainerStyle={mapContainerStyle}
                    center={position}
                    zoom={13}
                  >
                    <Marker position={position} />
                  </GoogleMap>
                </LoadScript>
              </div>
            </div>
            <div className={styles.EventMap}>
              <h2>Event Map</h2>
              {eventMap ? (
                <img src={eventMap} alt="Event Map" className={styles.MapImage} />
              ) : (
                <p>No map available.</p>
              )}
            </div>
          </div>
        ) : (
          <p>No event selected.</p>
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="primary">
          Close
        </Button>
        <Button onClick={handleMarkPresence} disabled={isButtonDisabled} color="primary" variant="contained">
          Mark Presence
        </Button>
        <Button onClick={handleIssuesClick} disabled={isIssuesButtonDisabled} color="secondary" variant="outlined">
          Issues
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default EventDialog;
