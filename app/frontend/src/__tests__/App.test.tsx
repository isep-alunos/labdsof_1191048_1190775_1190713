import React from "react";
import { render, screen } from "@testing-library/react";
import App from "../App";

describe("App Component", () => {
  it("renders the app correctly", () => {
    render(<App />);
    const linkElements = screen.getAllByText(/PortoEventos./);
    expect(linkElements.length).toBe(2); // "PortoEventos." is present in the main home page and in the header
    for (let i = 0; i < linkElements.length; i++) {
      expect(linkElements[i]).toBeInTheDocument();
    }
  });
});
