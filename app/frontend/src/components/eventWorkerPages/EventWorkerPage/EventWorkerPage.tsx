import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./EventWorkerPage.module.css";
import HttpService from "../../../utils/http";
import { getIssuesWorkerResponse } from "../../../utils/types";
import Cookies from "universal-cookie";

const EventWorkerPage: React.FC = () => {
  const [events, setEvents] = useState<getIssuesWorkerResponse>([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchEvents = async () => {
      const http = new HttpService();
      const response = await http.getPrivate<getIssuesWorkerResponse>(
        "/event-worker/issues"
      );
      if (response.body) {
        setEvents(response.body);
      }
    };
    fetchEvents();
  }, []);

  const handleEventClick = (eventName: string) => {
    navigate(`/event-worker/${encodeURIComponent(eventName)}`);
  };

  return (
    <div className={styles.EventWorkerPage}>
      <h1>Events</h1>
      <div className={styles.EventsContainer}>
        {events.map((event) => {
          const cookies = new Cookies();
          const workerName = cookies.get("name");
          const pendingIssuesCount = event.issues.filter((issue) => {
            const lastStatus =
              issue.issueStatusUpdateList[
                issue.issueStatusUpdateList.length - 1
              ].status;
            return (
              !issue.eventWorkerAssigned ||
              (issue.eventWorkerAssigned === workerName &&
                (lastStatus === "PENDING" || lastStatus === "IN_PROGRESS"))
            );
          }).length;
          return (
            <div
              key={event.event.name}
              className={styles.EventCard}
              onClick={() => handleEventClick(event.event.name)}
            >
              <div
                className={`${styles.PendingIssuesCount} ${
                  pendingIssuesCount > 0
                    ? styles.PendingIssuesCountRed
                    : styles.PendingIssuesCountGray
                }`}
              >
                {pendingIssuesCount}
              </div>
              <h2>{event.event.name}</h2>
              <p>{event.event.description}</p>
              <p>
                <strong>Start Date:</strong>{" "}
                {new Date(event.event.startDate).toLocaleDateString()}
              </p>
              <p>
                <strong>End Date:</strong>{" "}
                {new Date(event.event.endDate).toLocaleDateString()}
              </p>
              <p>
                <strong>Location:</strong> {event.event.location.street},{" "}
                {event.event.location.number}, {event.event.location.postalCode}
              </p>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default EventWorkerPage;
