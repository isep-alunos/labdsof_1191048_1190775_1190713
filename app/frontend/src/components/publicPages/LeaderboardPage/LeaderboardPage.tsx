import React, { useState, useEffect } from "react";
import styles from "./LeaderboardPage.module.css";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button,
} from "@mui/material";
import HttpService from "../../../utils/http";
import { criticality, leaderboardEntryDto, leaderboardResponse } from "../../../utils/types";
import { useAlert } from "../../../utils/alerts/AlertContext";

const LeaderboardPage: React.FC = () => {
  const [leaderboard, setLeaderboard] = useState<leaderboardResponse | null>(null);
  const [activeFilter, setActiveFilter] = useState<"points" | "issues">("points");
  const [isLoading, setIsLoading] = useState(true);
  const [errorShown, setErrorShown] = useState(false);
  const alert = useAlert();

  useEffect(() => {
    const fetchLeaderboard = async () => {
      try {
        const http = new HttpService();
        const response = await http.getPublic<leaderboardResponse>("/public/leaderboard");
        if (response.code === 200 && response.body) {
          setLeaderboard(response.body);
        } else {
          throw new Error("Failed to fetch leaderboard data.");
        }
      } catch (error: any) {
        console.error("Error fetching leaderboard data:", error);
        if (!errorShown) {
          alert.addAlert({
            message:
              "Something went wrong while fetching the leaderboard data. Please try again later.",
            criticality: criticality.ERROR,
          });
          setErrorShown(true);
        }
      } finally {
        setIsLoading(false);
      }
    };

    fetchLeaderboard();
  }, [alert, errorShown]);

  const handleFilterChange = (filter: "points" | "issues") => {
    setActiveFilter(filter);
  };

  const renderTableRows = (entries: leaderboardEntryDto[]) => {
    return entries.map((entry, index) => (
      <TableRow key={index}>
        <TableCell>{index + 1}</TableCell>
        <TableCell>{entry.name}</TableCell>
        <TableCell>{entry.count}</TableCell>
      </TableRow>
    ));
  };

  const renderLeaderboardTable = () => {
    if (!leaderboard) return null;

    const entries =
      activeFilter === "points"
        ? leaderboard.leaderboardPointsAccumulated
        : leaderboard.leaderboardReportedIssuesResolved;

    return (
      <TableContainer component={Paper} className={styles.tableContainer}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell style={{ width: "10%" }}>Rank</TableCell>
              <TableCell style={{ width: "60%" }}>Name</TableCell>
              <TableCell style={{ width: "30%" }}>
                {activeFilter === "points" ? "Points Accumulated" : "Issues Resolved"}
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>{renderTableRows(entries)}</TableBody>
        </Table>
      </TableContainer>
    );
  };

  return (
    <div className={styles.LeaderboardPage}>
      <h1>Leaderboard</h1>
      <div className={styles.filterButtons}>
        <Button
          variant={activeFilter === "points" ? "contained" : "outlined"}
          onClick={() => handleFilterChange("points")}
        >
          Points Accumulated
        </Button>
        <Button
          variant={activeFilter === "issues" ? "contained" : "outlined"}
          onClick={() => handleFilterChange("issues")}
        >
          Issues Resolved
        </Button>
      </div>
      {isLoading ? (
        <p>Loading leaderboard...</p>
      ) : leaderboard ? (
        renderLeaderboardTable()
      ) : (
        <p>No leaderboard data available.</p>
      )}
    </div>
  );
};

export default LeaderboardPage;
