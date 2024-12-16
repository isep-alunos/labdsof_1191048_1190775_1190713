import React, { useState, useEffect } from "react";
import styles from "./EventManagerPage.module.css";
import { Link } from "react-router-dom";
import { ROUTES } from "../../routes";
import { FaCalendarPlus } from "react-icons/fa";
import HttpService from "../../utils/http";
import { eventDto } from "../../utils/types";
import EventManagerEventPage from "./EventPage/EventManagerEventPage";
import { Divider } from "@mui/material";

const EventManagerPage: React.FC = () => {
  const [events, setEvents] = useState<eventDto[]>([]);
  const [selectedEvent, setSelectedEvent] = useState<string | null>(null);

  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const http = new HttpService();
        const response = await http.getPublic<eventDto[]>("/public/events");
        if (response.code === 200 && response.body) {
          setEvents(response.body);
        }
      } catch (error) {
        console.error("Error fetching events:", error);
      }
    };

    fetchEvents();
  }, []);

  const handleEventChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedEvent(event.target.value);
  };

  return (
    <div className={styles.EventManagerPage}>
      <h1>Event Manager Page</h1>
      <div className={styles.quickOptions}>
        <Link to={ROUTES.CREATE_EVENT} className={styles.cardLink}>
          <div className={styles.card}>
            <FaCalendarPlus className={styles.icon} />
            <h3>Create Event</h3>
          </div>
        </Link>
      </div>
      <Divider className={styles.divider} orientation="horizontal" flexItem />
      <div className={styles.EventWidget}>
        <h2>Event Details</h2>
        <select onChange={handleEventChange} value={selectedEvent || ""}>
          <option value="" disabled>
            Select an event
          </option>
          {events.map((event) => (
            <option key={event.name} value={event.name}>
              {event.name}
            </option>
          ))}
        </select>
        {selectedEvent && <EventManagerEventPage eventName={selectedEvent} />}
      </div>
    </div>
  );
};

export default EventManagerPage;
