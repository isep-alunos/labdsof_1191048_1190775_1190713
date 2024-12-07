import React, { useEffect, useState } from "react";
import styles from "./PrivateUserPage.module.css";
import HttpService from "../../../utils/http";
import { userProfileResponse, eventDto } from "../../../utils/types";
import Cookies from "universal-cookie";
import InfoIcon from "@mui/icons-material/InfoOutlined";
import { IconButton } from "@mui/material";

const PrivateUserPage: React.FC = () => {
  const [profile, setProfile] = useState<userProfileResponse | null>(null);
  const [showPopup, setShowPopup] = useState(false);
  const [isEnglish, setIsEnglish] = useState(false);
  const cookies = new Cookies();

  useEffect(() => {
    const fetchProfile = async () => {
      const http = new HttpService();
      const response = await http.getPrivate<userProfileResponse>(
        "/private/user/profile"
      );
      if (response.body) {
        setProfile(response.body);
      }
    };
    fetchProfile();
  }, []);

  const getTier = (points: number) => {
    if (points <= 10) return "Lower Tier: Rookie Reporter";
    if (points <= 30) return "Low-Mid Tier: Junior Guardian";
    if (points <= 50) return "Mid Tier: Community Protector";
    if (points <= 80) return "Mid-High Tier: Senior Sentinel";
    return "High Tier: Elite Defender";
  };

  const getProgressBarWidth = (points: number) => {
    if (points <= 10) return (points / 10) * 100;
    if (points <= 30) return ((points - 10) / 20) * 100;
    if (points <= 50) return ((points - 30) / 20) * 100;
    if (points <= 80) return ((points - 50) / 30) * 100;
    return 100;
  };

  const togglePopup = () => {
    setShowPopup(!showPopup);
  };

  const toggleLanguage = () => {
    setIsEnglish(!isEnglish);
  };

  if (!profile) {
    return <div>Loading...</div>;
  }

  return (
    <div className={styles.PrivateUserPage}>
      <h1>User Profile</h1>
      <div className={styles.ProfileInfo}>
        <img
          src={cookies.get("picture")}
          alt="User"
          className={styles.ProfileImage}
        />
        <h2>{profile.user.name}</h2>
        <p>{profile.user.email}</p>
        <div className={styles.PointsInfo}>
          <h3>Total Points: {profile.totalPointsAccumulated}</h3>
          <h3>Available Points: {profile.pointsAvailable}</h3>
          <div className={styles.ProgressBar}>
            <div
              className={styles.ProgressBarFill}
              style={{
                width: `${getProgressBarWidth(
                  profile.totalPointsAccumulated
                )}%`,
              }}
            >
              {profile.totalPointsAccumulated}p
            </div>
          </div>
          <div className={styles.PointsTier}>
            {getTier(profile.totalPointsAccumulated)}
          </div>
          <IconButton className={styles.PointsButton} onClick={togglePopup}>
            <InfoIcon />
          </IconButton>
        </div>
      </div>
      <div className={styles.EventsHistory}>
        <h3>Attended Events</h3>
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Description</th>
              <th>Start Date</th>
              <th>End Date</th>
              <th>Location</th>
            </tr>
          </thead>
          <tbody>
            {profile.attendedEvents.map((event: eventDto, index: number) => (
              <tr key={index}>
                <td>{event.name}</td>
                <td>{event.description}</td>
                <td>{new Date(event.startDate).toLocaleString()}</td>
                <td>{new Date(event.endDate).toLocaleString()}</td>
                <td>
                  {event.location.street}, {event.location.number},{" "}
                  {event.location.postalCode}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {showPopup && (
        <>
          <div className={styles.Overlay} onClick={togglePopup}></div>
          <div className={styles.PointsPopup}>
            <h1>{isEnglish ? "Points System" : "Sistema de pontos"}</h1>
            <h2>{isEnglish ? "Earn Points" : "Obter pontos"}</h2>
            <p>
              {isEnglish
                ? "Report a problem: +2 points"
                : "Reportar um problema: +2 pontos"}
            </p>
            <p>
              {isEnglish
                ? "Problem resolved: +3 points"
                : "O problema que reportou foi resolvido: +3 pontos"}
            </p>
            <p>
              {isEnglish
                ? "Receive a like: +1 point per like"
                : "Receber um like: +1 ponto por like"}
            </p>
            <p>
              {isEnglish
                ? "Praise Event Worker: +2 points (only available after the issue is resolved)"
                : "Elogiar um trabalhador do evento: +2 pontos (apenas disponível após o problema ser resolvido)"}
            </p>
            <h2>{isEnglish ? "Use Points" : "Usar os pontos"}</h2>
            <p>
              {isEnglish
                ? "Redeem 10 points for a discount"
                : "Descontar 10 pontos por desconto"}
            </p>
            <button className={styles.closeButton} onClick={togglePopup}>
              {isEnglish ? "Close" : "Fechar"}
            </button>
            <div className={styles.translateContainer}>
              <span>{isEnglish ? "Translate to:" : "Traduzir para:"}</span>
              <button
                className={styles.translateButton}
                onClick={toggleLanguage}
              >
                {isEnglish ? "Portuguese" : "English"}
              </button>
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default PrivateUserPage;
