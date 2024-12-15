import React, { useState, useEffect } from "react";
import styles from "./CreateEvent.module.css";
import HttpService from "../../../utils/http";
import { useAlert } from "../../../utils/alerts/AlertContext"; // Import the useAlert hook
import LocationSelector from './LocationSelector';
import {criticality, messageDto} from "../../../utils/types";
import {useNavigate} from "react-router-dom";

type EventWorker = { id: string; name: string; email: string };

const CreateEvent: React.FC = () => {
    const [formState, setFormState] = useState({
        name: "",
        description: "",
        startDate: "",
        endDate: "",
        maxParticipants: 0,
        eventWebsite: "",
        latitude: 41.155296,
        longitude: -8.633501,
        street: "",
        number: 0,
        postalCode: "",
        eventWorkers: [] as string[],
        availableEventWorkers: [] as EventWorker[],
        isSubmitting: false,
    });

    const alert = useAlert(); // Use the useAlert hook
    const navigate = useNavigate(); // Hook for navigation

    const sendAlert = (message: messageDto) => {
        alert.addAlert(message);
    };

    useEffect(() => {
        fetchAvailableEventWorkers();
    }, []);

    const fetchAvailableEventWorkers = () => {
        const http = new HttpService();
        http.getPrivate<EventWorker[]>("/event-manager/get-event-workers")
            .then((data) => {
                setFormState((prevState) => ({
                    ...prevState,
                    availableEventWorkers: data.body || [],
                }));
            })
            .catch((error) => {
                console.error("Error fetching event workers:", error);
                sendAlert({
                    message: "Error fetching available event workers.",
                    criticality: criticality.ERROR,
                });
            });
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormState((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleNumberChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = parseInt(e.target.value, 10);
        setFormState((prevState) => ({
            ...prevState,
            maxParticipants: value,
        }));
    };

    const handleEventWorkerChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const selectedEventWorkers = Array.from(e.target.selectedOptions, (option) => option.value);
        setFormState((prevState) => ({
            ...prevState,
            eventWorkers: selectedEventWorkers,
        }));
    };

    const handleLocationChange = (latitude: number, longitude: number, street: string, number: number, postalCode: string) => {
        setFormState((prevState) => ({
            ...prevState,
            latitude,
            longitude,
            street,
            number,
            postalCode,
        }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        const {
            name,
            description,
            startDate,
            endDate,
            maxParticipants,
            eventWebsite,
            latitude,
            longitude,
            street,
            number,
            postalCode,
            eventWorkers,
        } = formState;

        const eventData = {
            name,
            description,
            startDate,
            endDate,
            maxParticipants,
            eventWebsite,
            latitude,
            longitude,
            street,
            number,
            postalCode,
            eventWorkers,
        };

        setFormState((prevState) => ({
            ...prevState,
            isSubmitting: true,
        }));


        const http = new HttpService();
        http.postPrivate<messageDto>("/event-manager/event", eventData)
            .then((data) => {
                if (data.code === 201) {
                    sendAlert(data.body!);
                    navigate(`/event-worker/event/${encodeURIComponent(name)}`);
                } else {
                    sendAlert(data.body!);
                }
            })
            .finally(() => {
                setFormState((prevState) => ({
                    ...prevState,
                    isSubmitting: false,
                }));
            });
    };

    return (
        <div className={styles.CreateEvent}>
            <h1>Create Event</h1>
            <form id="create-event-form" onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="name">Event Name</label>
                    <input
                        type="text"
                        name="name"
                        placeholder="Enter event name"
                        value={formState.name}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="description">Description</label>
                    <textarea
                        name="description"
                        placeholder="Enter event description"
                        value={formState.description}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="startDate">Start Date</label>
                    <input
                        type="datetime-local"
                        name="startDate"
                        value={formState.startDate}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="endDate">End Date</label>
                    <input
                        type="datetime-local"
                        name="endDate"
                        value={formState.endDate}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="maxParticipants">Max Participants</label>
                    <input
                        type="number"
                        name="maxParticipants"
                        value={formState.maxParticipants}
                        onChange={handleNumberChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="eventWebsite">Event Website</label>
                    <input
                        type="url"
                        name="eventWebsite"
                        placeholder="https://example.com"
                        value={formState.eventWebsite}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div>
                    <label>Location</label>
                    <LocationSelector
                        lat={formState.latitude}
                        lng={formState.longitude}
                        onLocationChange={handleLocationChange}
                    />
                </div>
                <div>
                    <label htmlFor="street">Street</label>
                    <input
                        type="text"
                        name="street"
                        placeholder="Enter street"
                        value={formState.street}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="number">Number</label>
                    <input
                        type="number"
                        name="number"
                        value={formState.number}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="postalCode">Postal Code</label>
                    <input
                        type="text"
                        name="postalCode"
                        placeholder="Enter postal code"
                        value={formState.postalCode}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="eventWorkers">Event Workers</label>
                    <select
                        multiple
                        name="eventWorkers"
                        value={formState.eventWorkers}
                        onChange={handleEventWorkerChange}
                        required
                    >
                        {formState.availableEventWorkers.map((worker) => (
                            <option key={worker.id} value={worker.id}>
                                {worker.name} | {worker.email}
                            </option>
                        ))}
                    </select>
                </div>
                <button type="submit" className="background_v2" disabled={formState.isSubmitting}>
                    {formState.isSubmitting ? "Submitting..." : "Create Event"}
                </button>
            </form>
        </div>
    );
};

export default CreateEvent;
