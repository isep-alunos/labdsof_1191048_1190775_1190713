import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom"; // Import useParams to retrieve the eventName from the URL
import styles from "./EventIssuesPage.module.css"; // Import the styles
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  SelectChangeEvent,
  Box,
} from "@mui/material";
import { issue, issueStatus } from "../../../../utils/types"; // Your Issue model types

// Mockup issues for demonstration purposes
const mockIssues: issue[] = [
  {
    id: "1",
    creationDate: new Date().toISOString(),
    title: "Lighting issue",
    description: "Streetlights are not working properly.",
    issueStatusUpdateList: [
      { id: "101", updateTime: new Date().toISOString(), description: "Reported", status: issueStatus.PENDING },
    ],
    location: { location: "Main Street" },
  },
  {
    id: "2",
    creationDate: new Date(new Date().getTime() - 86400000).toISOString(),
    title: "Garbage collection delay",
    description: "Garbage has not been collected for three days.",
    issueStatusUpdateList: [
      { id: "102", updateTime: new Date().toISOString(), description: "In progress", status: issueStatus.IN_PROGRESS },
    ],
    location: { location: "5th Avenue" },
  },
];

const EventIssuesPage: React.FC = () => {
  const { eventName } = useParams<{ eventName: string }>(); // Retrieve the eventName from the URL
  const [issues, setIssues] = useState<issue[]>(mockIssues);
  const [filterStatus, setFilterStatus] = useState<string>("ALL");
  const [sortOrder, setSortOrder] = useState<"asc" | "desc">("asc");
  const [selectedIssue, setSelectedIssue] = useState<issue | null>(null);

  const filteredIssues = issues.filter((issue) =>
    filterStatus === "ALL" ? true : issue.issueStatusUpdateList[0]?.status === filterStatus
  );
  const sortedIssues = filteredIssues.sort((a, b) =>
    sortOrder === "asc"
      ? new Date(a.creationDate).getTime() - new Date(b.creationDate).getTime()
      : new Date(b.creationDate).getTime() - new Date(a.creationDate).getTime()
  );

  const handleFilterChange = (event: SelectChangeEvent<string>) => {
    setFilterStatus(event.target.value as string);
  };

  const handleSortChange = (event: SelectChangeEvent<"asc" | "desc">) => {
    setSortOrder(event.target.value as "asc" | "desc");
  };

  const handleIssueClick = (issue: issue) => {
    setSelectedIssue(issue);
  };

  const handleDialogClose = () => {
    setSelectedIssue(null);
  };

  return (
    <div className={styles.EventIssuesPage}>
      <h1>{eventName ? `${eventName}'s Issues` : "Event Issues"}</h1>
      <div className={styles.Filters}>
      <Box
        className="Filters"
        display="flex"
        gap="20px"
        justifyContent="flex-start"
        alignItems="center"
        mb={2}
        sx={{ flexWrap: "wrap" }}
      >
        <FormControl
          className="FilterControl"
          sx={{ minWidth: 180, maxWidth: "100%" }}
          variant="outlined"
        >
          <InputLabel>Status</InputLabel>
          <Select
            value={filterStatus}
            onChange={handleFilterChange}
            label="Status"
            sx={{
              backgroundColor: "#f5f5f5",
              borderRadius: "4px",
              padding: "8px 12px",
              height: "40px", // Fixed height for alignment
            }}
          >
            <MenuItem value="ALL">All</MenuItem>
            <MenuItem value="PENDING">Pending</MenuItem>
            <MenuItem value="IN_PROGRESS">In Progress</MenuItem>
            <MenuItem value="RESOLVED">Resolved</MenuItem>
            <MenuItem value="REJECTED">Rejected</MenuItem>
          </Select>
        </FormControl>

        <FormControl
          className="FilterControl"
          sx={{ minWidth: 180, maxWidth: "100%" }}
          variant="outlined"
        >
          <InputLabel>Sort By</InputLabel>
          <Select
            value={sortOrder}
            onChange={handleSortChange}
            label="Sort By"
            sx={{
              backgroundColor: "#f5f5f5",
              borderRadius: "4px",
              padding: "8px 12px",
              height: "40px", // Fixed height for alignment
            }}
          >
            <MenuItem value="asc">Creation Date (Ascending)</MenuItem>
            <MenuItem value="desc">Creation Date (Descending)</MenuItem>
          </Select>
        </FormControl>
      </Box>
      </div>

      <table className={styles.IssuesTable}>
        <thead>
          <tr>
            <th>Title</th>
            <th>Status</th>
            <th>Creation Date</th>
          </tr>
        </thead>
        <tbody>
          {sortedIssues.map((issue) => (
            <tr
              key={issue.id}
              onClick={() => handleIssueClick(issue)}
              className={styles.TableRow}
            >
              <td>{issue.title}</td>
              <td>{issue.issueStatusUpdateList[0]?.status}</td>
              <td>{new Date(issue.creationDate).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {selectedIssue && (
        <Dialog open={true} onClose={handleDialogClose} fullWidth maxWidth="sm">
          <DialogTitle className={styles.DialogTitle}>Issue Details</DialogTitle>
          <DialogContent className={styles.DialogContent}>
            <p><strong>Title:</strong> {selectedIssue.title}</p>
            <p><strong>Description:</strong> {selectedIssue.description}</p>
            <p><strong>Location:</strong> {selectedIssue.location.location}</p>
            <h4>Status History:</h4>
            <ul>
              {selectedIssue.issueStatusUpdateList.map((update) => (
                <li key={update.id}>
                  {update.status} - {update.description} (
                  {new Date(update.updateTime).toLocaleString()})
                </li>
              ))}
            </ul>
          </DialogContent>
          <DialogActions className={styles.DialogActions}>
            <Button onClick={handleDialogClose}>Close</Button>
          </DialogActions>
        </Dialog>
      )}
    </div>
  );
};

export default EventIssuesPage;
