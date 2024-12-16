import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import HttpService from "../../../utils/http";
import { eventDto, messageDto } from "../../../utils/types";
import styles from "./EventManagerEventPage.module.css";
import { LoadScript, GoogleMap, Marker } from "@react-google-maps/api";
import { useAlert } from "../../../utils/alerts/AlertContext";

interface EventManagerEventPageProps {
  eventName?: string;
}

const EventManagerEventPage: React.FC<EventManagerEventPageProps> = ({
  eventName: propEventName,
}) => {
  const { eventName: urlEventName } = useParams<{ eventName: string }>();
  const eventName = propEventName || urlEventName;
  const [event, setEvent] = useState<eventDto | null>(null);
  const [eventMap, setEventMap] = useState<string | null>(null);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [fileError, setFileError] = useState<string | null>(null);
  const alert = useAlert();

  const fetchEventMap = async () => {
    try {
      const http = new HttpService();
      const response = await http.getPublicImage(
        `/public/events/${eventName}/map`
      );
      if (response.code === 404) {
        setEventMap(null);
      } else if (response.body) {
        setEventMap(URL.createObjectURL(response.body));
      }
    } catch (error) {
      console.error("Error fetching event map:", error);
    }
  };

  useEffect(() => {
    const fetchEventDetails = async () => {
      try {
        const http = new HttpService();
        const response = await http.getPublic<eventDto>(
          `/public/events/${eventName}`
        );
        if (response.body) {
          setEvent(response.body);
        }
      } catch (error) {
        console.error("Error fetching event details:", error);
      }
    };

    if (eventName) {
      fetchEventDetails();
      fetchEventMap();
    }
  }, [eventName]);

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files && event.target.files[0];
    if (file) {
      if (!["image/jpeg", "image/png"].includes(file.type)) {
        setFileError("Only JPEG and PNG files are allowed.");
        setSelectedFile(null);
      } else if (file.size > 10 * 1024 * 1024) {
        setFileError("File size must be less than 10MB.");
        setSelectedFile(null);
      } else {
        setFileError(null);
        setSelectedFile(file);
      }
    }
  };

  const handleUpload = async () => {
    if (selectedFile) {
      const formData = new FormData();
      formData.append("eventName", eventName?.toString() || "");
      formData.append("image", selectedFile);
      try {
        const http = new HttpService();
        const responseServer = await http.postPrivate<messageDto>(
          `/event-manager/event/image`,
          formData,
          "multipart/form-data"
        );
        if (responseServer.body) {
          alert.addAlert(responseServer.body);
        }
        fetchEventMap();
      } catch (error) {
        console.error("Error uploading map:", error);
      }
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
    <div className={styles.EventManagerEventPage}>
      <h1>Event Management: {eventName}</h1>
      {event ? (
        <div className={styles.EventDetails}>
          <div className={styles.DetailRow}>
            <span className={styles.Label}>
              <strong>Name:</strong>
            </span>
            <span className={styles.Value}>{event.name}</span>
          </div>
          <div className={styles.DetailRow}>
            <span className={styles.Label}>
              <strong>Description:</strong>
            </span>
            <span className={styles.Value}>{event.description}</span>
          </div>
          <div className={styles.DetailRow}>
            <span className={styles.Label}>
              <strong>Start Date:</strong>
            </span>
            <span className={styles.Value}>{event.startDate}</span>
          </div>
          <div className={styles.DetailRow}>
            <span className={styles.Label}>
              <strong>End Date:</strong>
            </span>
            <span className={styles.Value}>{event.endDate}</span>
          </div>
          <div className={styles.DetailRow}>
            <span className={styles.Label}>
              <strong>Max Participants:</strong>
            </span>
            <span className={styles.Value}>{event.maxParticipants}</span>
          </div>
          <div className={styles.DetailRow}>
            <span className={styles.Label}>
              <strong>Event Website:</strong>
            </span>
            <span className={styles.Value}>
              <a
                href={event.eventWebsite}
                target="_blank"
                rel="noopener noreferrer"
              >
                {event.eventWebsite}
              </a>
            </span>
          </div>
          <div className={styles.DetailRow}>
            <span className={styles.Label}>
              <strong>Location:</strong>
            </span>
            <span className={styles.Value}>
              {event.location.street}, {event.location.number},{" "}
              {event.location.postalCode}
            </span>
          </div>
          <div className={styles.DetailRow}>
            <span className={styles.Label}>
              <strong>Coordinates:</strong>
            </span>
            <span className={styles.Value}>
              {event.location.latitude}, {event.location.longitude}
            </span>
          </div>
          <div className={styles.DetailRow}>
            <span className={styles.Label}>
              <strong>Event Workers:</strong>
            </span>
            <span className={styles.Value}>
              {event.eventWorkerNames.join(", ")}
            </span>
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
              <p>No map available. You can upload one below.</p>
            )}
            <input
              type="file"
              accept="image/jpeg, image/png"
              onChange={handleFileChange}
            />
            {fileError && <p className={styles.FileError}>{fileError}</p>}
            <button onClick={handleUpload} disabled={!selectedFile}>
              Upload Map
            </button>
          </div>
        </div>
      ) : (
        <p>Loading event details...</p>
      )}
    </div>
  );
};

export default EventManagerEventPage;
