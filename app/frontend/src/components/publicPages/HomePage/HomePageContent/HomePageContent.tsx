import React, { useState, useEffect } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TableSortLabel,
  Paper,
} from "@mui/material";
import HttpService from "../../../../utils/http";
import { criticality, eventDto } from "../../../../utils/types";
import { visuallyHidden } from "@mui/utils";
import { useAlert } from "../../../../utils/alerts/AlertContext";
import styles from "./HomePageContent.module.css";
import EventDialog from "./EventDialog";

type MyProps = {
  isLoggedIn: boolean;
};

const HomePageContent: React.FC<MyProps> = ({ isLoggedIn }) => {
  const [events, setEvents] = useState<eventDto[]>([]);
  const [orderBy, setOrderBy] = useState<keyof eventDto>("name");
  const [order, setOrder] = useState<"asc" | "desc">("asc");
  const [errorShown, setErrorShown] = useState(false);

  const [isDialogOpen, setDialogOpen] = useState(false);
  const [selectedEvent, setSelectedEvent] = useState<any>(null);

  const handleRowClick = (event: any) => {
    setSelectedEvent(event);
    setDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
    setSelectedEvent(null);
  };

  const alert = useAlert();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const http = new HttpService();
        const data = await http.getPublic<eventDto[]>(`/public/events`);
        if (data.code >= 400) {
          throw new Error("Error fetching events.");
        }
        if (data.body) setEvents(data.body);
      } catch (error: any) {
        if (!errorShown) {
          alert.addAlert({
            message:
              "Something went wrong during the fetching of the events, please reload. If the error continues, please contact us.",
            criticality: criticality.ERROR,
          });
          setErrorShown(true);
        }
      }
    };

    fetchData();
  }, [alert]);

  const handleSort = (property: keyof eventDto) => {
    const isAsc = orderBy === property && order === "asc";
    setOrder(isAsc ? "desc" : "asc");
    setOrderBy(property);
  };

  const sortedEvents = [...events].sort((a, b) => {
    const valueA = a[orderBy];
    const valueB = b[orderBy];

    if (valueA < valueB) return order === "asc" ? -1 : 1;
    if (valueA > valueB) return order === "asc" ? 1 : -1;
    return 0;
  });

  return (
    <div>
      <TableContainer component={Paper} className={styles.tableContainer}>
        <Table className={styles.table}>
          <TableHead className={styles.tableHead}>
            <TableRow>
              <TableCell className={styles.tableHeader}>
                <TableSortLabel
                  active={orderBy === "name"}
                  direction={orderBy === "name" ? order : "asc"}
                  onClick={() => handleSort("name")}
                  className={styles.tableSortLabel}
                  sx={{
                    '&.MuiTableSortLabel-root': {
                        color: 'white',
                    },
                    '& .MuiTableSortLabel-icon': {
                        color: 'white !important',
                    },
                }}
                >
                  Name
                  {orderBy === "name" && (
                    <span style={visuallyHidden}>
                      {order === "desc" ? "sorted descending" : "sorted ascending"}
                    </span>
                  )}
                </TableSortLabel>
              </TableCell>
              <TableCell className={styles.tableHeader}>
                <TableSortLabel
                  active={orderBy === "description"}
                  direction={orderBy === "description" ? order : "asc"}
                  onClick={() => handleSort("description")}
                  className={styles.tableSortLabel}
                  sx={{
                    '&.MuiTableSortLabel-root': {
                        color: 'white',
                    },
                    '& .MuiTableSortLabel-icon': {
                        color: 'white !important',
                    },
                }}
                >
                  Description
                  {orderBy === "description" && (
                    <span style={visuallyHidden}>
                      {order === "desc" ? "sorted descending" : "sorted ascending"}
                    </span>
                  )}
                </TableSortLabel>
              </TableCell>
              <TableCell className={styles.tableHeader}>
                <TableSortLabel
                  active={orderBy === "startDate"}
                  direction={orderBy === "startDate" ? order : "asc"}
                  onClick={() => handleSort("startDate")}
                  className={styles.tableSortLabel}
                  sx={{
                    '&.MuiTableSortLabel-root': {
                        color: 'white',
                    },
                    '& .MuiTableSortLabel-icon': {
                        color: 'white !important',
                    },
                }}
                >
                  Start Date
                  {orderBy === "startDate" && (
                    <span style={visuallyHidden}>
                      {order === "desc" ? "sorted descending" : "sorted ascending"}
                    </span>
                  )}
                </TableSortLabel>
              </TableCell>
              <TableCell className={styles.tableHeader}>
                <TableSortLabel
                  active={orderBy === "endDate"}
                  direction={orderBy === "endDate" ? order : "asc"}
                  onClick={() => handleSort("endDate")}
                  className={styles.tableSortLabel}
                  sx={{
                    '&.MuiTableSortLabel-root': {
                        color: 'white',
                    },
                    '& .MuiTableSortLabel-icon': {
                        color: 'white !important',
                    },
                }}
                >
                  End Date
                  {orderBy === "endDate" && (
                    <span style={visuallyHidden}>
                      {order === "desc" ? "sorted descending" : "sorted ascending"}
                    </span>
                  )}
                </TableSortLabel>
              </TableCell>
              <TableCell className={styles.tableHeader}>
                <TableSortLabel
                  active={orderBy === "maxParticipants"}
                  direction={orderBy === "maxParticipants" ? order : "asc"}
                  onClick={() => handleSort("maxParticipants")}
                  className={styles.tableSortLabel}
                  sx={{
                    '&.MuiTableSortLabel-root': {
                        color: 'white',
                    },
                    '& .MuiTableSortLabel-icon': {
                        color: 'white !important',
                    },
                }}
                >
                  Max Participants
                  {orderBy === "maxParticipants" && (
                    <span style={visuallyHidden}>
                      {order === "desc" ? "sorted descending" : "sorted ascending"}
                    </span>
                  )}
                </TableSortLabel>
              </TableCell>
              <TableCell
                className={styles.tableHeader}
                sx={{
                  '&.MuiTableCell-root': {
                      color: 'white',
                  }
              }}>
                Location</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {sortedEvents.map((event, index) => (
              <TableRow key={event.name} className={styles.tableRow} onClick={() => handleRowClick(event)}>
                <TableCell className={styles.tableCell}>{event.name}</TableCell>
                <TableCell className={styles.tableCell}>{event.description}</TableCell>
                <TableCell className={styles.tableCell}>
                  {new Date(event.startDate).toLocaleString()}
                </TableCell>
                <TableCell className={styles.tableCell}>
                  {new Date(event.endDate).toLocaleString()}
                </TableCell>
                <TableCell className={styles.tableCell}>{event.maxParticipants}</TableCell>
                <TableCell className={styles.tableCell}>
                  {`${event.location.street}, ${event.location.number}, ${event.location.postalCode}`}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <EventDialog
        event={selectedEvent}
        open={isDialogOpen}
        onClose={handleCloseDialog}
        isLoggedIn={isLoggedIn} // Pass login status here
      />
      </div>
  );
};

export default HomePageContent;

// TODO: Remove after having good bootstrap of events
const mockupEvents : eventDto[] = [{
  name: "Evento BODA",
  description: "olaa",
  startDate: "2024-11-12T18:00:00",
  endDate: "2024-11-12T20:00:00",
  maxParticipants: 2,
  eventWebsite: "https://www.youtube.com/?gl=PT",
  location: {
    latitude: 10.0,
    longitude: 50.0,
    street: "Estrada da Bragada",
    number: 1160,
    postalCode: "4620-722",
  },
  eventWorkerNames: [],
},
{
  name: "Tech Conference 2024",
  description: "A tech conference discussing the future of AI.",
  startDate: "2024-11-20T10:00:00",
  endDate: "2024-11-20T17:00:00",
  maxParticipants: 200,
  eventWebsite: "https://techconference2024.com",
  location: {
    latitude: 37.7749,
    longitude: -122.4194,
    street: "Market Street",
    number: 123,
    postalCode: "94103",
  },
  eventWorkerNames: ["Alice", "Bob"],
},
{
  name: "Music Festival",
  description: "A live music festival featuring various artists.",
  startDate: "2024-11-18T16:00:00",
  endDate: "2024-11-18T23:00:00",
  maxParticipants: 5000,
  eventWebsite: "https://musicfestival.com",
  location: {
    latitude: 40.7128,
    longitude: -74.006,
    street: "Broadway",
    number: 456,
    postalCode: "10007",
  },
  eventWorkerNames: ["Charlie"],
},
{
  name: "Art Exhibition",
  description: "A showcase of modern and classical art.",
  startDate: "2024-11-15T09:00:00",
  endDate: "2024-11-15T18:00:00",
  maxParticipants: 100,
  eventWebsite: "https://artexhibition.com",
  location: {
    latitude: 48.8566,
    longitude: 2.3522,
    street: "Champs-Élysées",
    number: 789,
    postalCode: "75008",
  },
  eventWorkerNames: [],
},
{
  name: "Startup Pitch Night",
  description: "An event for startups to pitch their ideas to investors.",
  startDate: "2024-11-25T18:00:00",
  endDate: "2024-11-25T21:00:00",
  maxParticipants: 50,
  eventWebsite: "https://startuppitchnight.com",
  location: {
    latitude: 51.5074,
    longitude: -0.1278,
    street: "Baker Street",
    number: 221,
    postalCode: "NW1 6XE",
  },
  eventWorkerNames: ["David", "Eve"],
},
{
  name: "Coding Hackathon",
  description: "A 48-hour hackathon to build innovative software solutions.",
  startDate: "2024-11-22T09:00:00",
  endDate: "2024-11-24T17:00:00",
  maxParticipants: 300,
  eventWebsite: "https://codinghackathon.com",
  location: {
    latitude: 34.0522,
    longitude: -118.2437,
    street: "Sunset Boulevard",
    number: 900,
    postalCode: "90028",
  },
  eventWorkerNames: ["Frank", "Grace"],
}];