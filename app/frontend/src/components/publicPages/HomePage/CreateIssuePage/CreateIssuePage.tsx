import React, { useState, useEffect } from "react";
import styles from "./CreateIssuePage.module.css";
import HttpService from "../../../../utils/http";
import { useAlert } from "../../../../utils/alerts/AlertContext"; // Import the useAlert hook
import {criticality, messageDto} from "../../../../utils/types";
import {useNavigate, useParams} from "react-router-dom";

type issueWorker = { id: string; name: string; email: string };

const CreateIssuePage: React.FC = () => {
    const [formState, setFormState] = useState({
        title: "",
        description: "",
        isSubmitting: false,
        location : "",
    });

    const { eventName } = useParams<{ eventName: string }>();

    const alert = useAlert(); // Use the useAlert hook
    const navigate = useNavigate(); // Hook for navigation

    const sendAlert = (message: messageDto) => {
        alert.addAlert(message);
    };

    useEffect(() => {
        //fetchAvailableissueWorkers();
    }, []);


    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormState((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };


    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        const {
            title,
            description,
            location,
        } = formState;

        const issueData = {
            title : title,
            description: description,
            eventName: eventName,
            location: location
        };

        setFormState((prevState) => ({
            ...prevState,
            isSubmitting: true,
        }));


        const http = new HttpService();
        http.postPrivate<messageDto>("/private/create-issue", issueData)
            .then((data) => {
                if (data.code === 201) {
                    sendAlert(data.body!);
                    navigate("/events/" + eventName + "/issues");
                } else  if (data.code === 200) {
                    //open popup
                } else {

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
        <div className={styles.CreateIssuePage}>
            <h1>{eventName ? `${eventName}: Report Issue` : "Report Issue"}</h1>
            <form id="create-issue-form" onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="title">Title</label>
                    <input
                        type="text"
                        name="title"
                        placeholder="Enter issue title"
                        value={formState.title}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="description">Description</label>
                    <textarea
                        name="description"
                        placeholder="Enter issue description"
                        value={formState.description}
                        onChange={handleInputChange}
                        required
                    />
                </div>

                <div>
                    <label htmlFor="location">Location</label>
                    <input
                        type="text"
                        name="location"
                        placeholder="Enter issue location"
                        value={formState.location}
                        onChange={handleInputChange}
                        required
                    />
                </div>

                <button type="submit" className="background_v2" disabled={formState.isSubmitting}>
                    {formState.isSubmitting ? "Submitting..." : "Create issue"}
                </button>
            </form>
        </div>
    );
};

export default CreateIssuePage;
