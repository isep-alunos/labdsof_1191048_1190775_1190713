import React, { Component } from "react";
import styles from "./CreateEvent.module.css";
import HttpService from "../../../utils/http";
import {tokenResponse} from "../../../utils/types";
import LocationSelector from './LocationSelector';

type MyProps = {
};

type MyState = {
    name: string;
    description: string;
    startDate: string;
    endDate: string;
    maxParticipants: number;
    eventWebsite: string;
    latitude: number;
    longitude: number;
    street: string;
    number: number;
    postalCode: string;
    eventWorkers: string[];
    availableEventWorkers: { id: string; name: string }[];
    isSubmitting: boolean;
};

class CreateEvent extends Component<MyProps, MyState> {
    constructor(props: MyProps) {
        super(props);
        this.state = {
            name: "",
            description: "",
            startDate: "",
            endDate: "",
            maxParticipants: 0,
            eventWebsite: "",
            latitude: 0,
            longitude: 0,
            street: "",
            number: 0,
            postalCode: "",
            eventWorkers: [],
            availableEventWorkers: [],
            isSubmitting: false,
        };
    }

    componentDidMount() {
        // Fetch available event workers when component mounts
        //this.fetchAvailableEventWorkers();
    }

    fetchAvailableEventWorkers = () => {
        const http = new HttpService();
        http
            .getPublic<{ id: string; name: string }[]>("/event-workers")
            .then((data) => {
                this.setState({ availableEventWorkers: data.body || [] });
            })
            .catch((error) => {
                console.error("Error fetching event workers:", error);
            });
    };

    handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        this.setState({[name]: value} as unknown as Pick<MyState, keyof MyState>);
    };

    handleNumberChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = parseInt(e.target.value, 10);
        this.setState({ maxParticipants: value });
    };

    handleEventWorkerChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const selectedEventWorkers = Array.from(e.target.selectedOptions, (option) => option.value);
        this.setState({ eventWorkers: selectedEventWorkers });
    };

    handleLocationChange = (latitude: number, longitude: number, street: string, number: number, postalCode: string) => {
        // Update state with the selected latitude and longitude
        this.setState({ latitude, longitude, street, number, postalCode });
    };

    handleSubmit = (e: React.FormEvent) => {
      /*  e.preventDefault();
        const { name, description, startDate, endDate, maxParticipants, eventWebsite, latitude, longitude, street, number, postalCode, eventWorkers } = this.state;

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

        this.setState({ isSubmitting: true });

        const http = new HttpService();
        http
            .postPublic("/events", eventData)
            .then((data) => {
                console.log("Event created successfully", data);
                this.props.homePage();
            })
            .catch((error) => {
                console.error("Error creating event:", error);
            })
            .finally(() => {
                this.setState({ isSubmitting: false });
            });


       */
    };

    render() {
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
            availableEventWorkers,
            eventWorkers,
            isSubmitting,
        } = this.state;

        return (
            <div className={styles.CreateEvent}>
                <h1>Create Event</h1>
                <form id="create-event-form" onSubmit={this.handleSubmit}>
                    <div>
                        <label htmlFor="name">Event Name</label>
                        <input
                            type="text"
                            name="name"
                            placeholder="Enter event name"
                            value={name}
                            onChange={this.handleInputChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="description">Description</label>
                        <textarea
                            name="description"
                            placeholder="Enter event description"
                            value={description}
                            onChange={this.handleInputChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="startDate">Start Date</label>
                        <input
                            type="datetime-local"
                            name="startDate"
                            value={startDate}
                            onChange={this.handleInputChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="endDate">End Date</label>
                        <input
                            type="datetime-local"
                            name="endDate"
                            value={endDate}
                            onChange={this.handleInputChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="maxParticipants">Max Participants</label>
                        <input
                            type="number"
                            name="maxParticipants"
                            value={maxParticipants}
                            onChange={this.handleNumberChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="eventWebsite">Event Website</label>
                        <input
                            type="url"
                            name="eventWebsite"
                            placeholder="https://example.com"
                            value={eventWebsite}
                            onChange={this.handleInputChange}
                            required
                        />
                    </div>
                    <div>
                        <label>Location</label>
                        <LocationSelector
                            lat={latitude}
                            lng={longitude}
                            onLocationChange={this.handleLocationChange}
                        />
                    </div>
                    <div>
                        <label htmlFor="street">Street</label>
                        <input
                            type="text"
                            name="street"
                            placeholder="Enter street"
                            value={street}
                            onChange={this.handleInputChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="number">Number</label>
                        <input
                            type="number"
                            name="number"
                            value={number}
                            onChange={this.handleInputChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="postalCode">Postal Code</label>
                        <input
                            type="text"
                            name="postalCode"
                            placeholder="Enter postal code"
                            value={postalCode}
                            onChange={this.handleInputChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="eventWorkers">Event Workers</label>
                        <select
                            multiple
                            name="eventWorkers"
                            value={eventWorkers}
                            onChange={this.handleEventWorkerChange}
                            required
                        >
                            {availableEventWorkers.map((worker) => (
                                <option key={worker.id} value={worker.id}>
                                    {worker.name}
                                </option>
                            ))}
                        </select>
                    </div>
                    <button
                        type="submit"
                        className="background_v2"
                        disabled={isSubmitting}
                    >
                        {isSubmitting ? "Submitting..." : "Create Event"}
                    </button>
                </form>
            </div>
        );
    }
}

export default CreateEvent;
