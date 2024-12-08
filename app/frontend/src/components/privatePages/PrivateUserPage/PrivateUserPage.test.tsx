import React from "react";
import { render, screen, waitFor } from "@testing-library/react";
import PrivateUserPage from "./PrivateUserPage";
import HttpService from "../../../utils/http";
import { userProfileResponse } from "../../../utils/types";
import Cookies from "universal-cookie";

jest.mock("../../../utils/http");
jest.mock("universal-cookie");

const mockProfile: userProfileResponse = {
  user: {
    name: "John Doe",
    email: "john.doe@example.com",
  },
  attendedEvents: [
    {
      name: "Event 1",
      description: "Description 1",
      startDate: "2024-11-12T18:00:00",
      endDate: "2024-11-12T20:00:00",
      maxParticipants: 100,
      eventWebsite: "https://example.com",
      location: {
        latitude: 10.0,
        longitude: 50.0,
        street: "Street 1",
        number: 123,
        postalCode: "12345",
      },
      eventWorkerNames: ["Worker 1"],
    },
  ],
  totalPointsAccumulated: 25,
  pointsAvailable: 20,
};

describe("PrivateUserPage", () => {
  beforeEach(() => {
    (HttpService.prototype.getPrivate as jest.Mock).mockResolvedValue({
      body: mockProfile,
    });
    (Cookies.prototype.get as jest.Mock).mockImplementation((key: string) => {
      if (key === "picture") return "https://example.com/picture.jpg";
      return null;
    });
  });

  test("renders user profile information", async () => {
    render(<PrivateUserPage />);

    await waitFor(() => {
      expect(screen.getByText("John Doe")).toBeInTheDocument();
      expect(screen.getByText("john.doe@example.com")).toBeInTheDocument();
      expect(screen.getByText("Total Points: 25")).toBeInTheDocument();
      expect(screen.getByText("Available Points: 20")).toBeInTheDocument();
    });
  });

  test("renders attended events", async () => {
    render(<PrivateUserPage />);

    await waitFor(() => {
      expect(screen.getByText("Event 1")).toBeInTheDocument();
      expect(screen.getByText("Description 1")).toBeInTheDocument();
      expect(screen.getByText("Street 1, 123, 12345")).toBeInTheDocument();
    });
  });
});
