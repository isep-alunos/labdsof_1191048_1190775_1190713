import React, { useState } from "react";
import { Dialog, DialogTitle, DialogContent, DialogActions, Button } from "@mui/material";
import { LoadScript, GoogleMap, Marker } from "@react-google-maps/api";
import { criticality, eventDto, markPresenceRequest, markPresenceResponse } from "../../../../utils/types";
import HttpService from "../../../../utils/http";
import { useAlert } from "../../../../utils/alerts/AlertContext";
import { useNavigate } from "react-router-dom";

const EventDialog: React.FC<{
  event: eventDto | null;
  open: boolean;
  onClose: () => void;
  isLoggedIn: boolean;
}> = ({ event, open, onClose, isLoggedIn }) => {
  const [isButtonDisabled, setButtonDisabled] = useState(false);
  const alert = useAlert();
  const navigate = useNavigate();

  const handleMarkPresence = async () => {
    if (!isLoggedIn) {
      alert.addAlert({
        message: "You must be logged to mark presence at event.",
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
          <>
            <p><strong>Name:</strong> {event.name}</p>
            <p><strong>Description:</strong> {event.description}</p>
            <p><strong>Start Date:</strong> {new Date(event.startDate).toLocaleString()}</p>
            <p><strong>End Date:</strong> {new Date(event.endDate).toLocaleString()}</p>
            <p><strong>Max Participants:</strong> {event.maxParticipants}</p>
            <p><strong>Location:</strong> {`${event.location.street}, ${event.location.number}, ${event.location.postalCode}`}</p>
            <p><strong>Event Workers:</strong> {event.eventWorkerNames.join(", ")}</p>
            <p><a href={event.eventWebsite} target="_blank" rel="noopener noreferrer">Event Website</a></p>
            <LoadScript googleMapsApiKey="AIzaSyBRjYJ-6-HMhU-_s9GOuT5t9B4cfJKY6KY">
              <GoogleMap
                mapContainerStyle={mapContainerStyle}
                center={position}
                zoom={13}
              >
                <Marker position={position} />
              </GoogleMap>
            </LoadScript>
          </>
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
        <Button onClick={handleIssuesClick} color="secondary" variant="outlined">
          Issues
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default EventDialog;
