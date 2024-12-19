import React, {useState} from "react";
import styles from "./CreateIssuePage.module.css";
import HttpService from "../../../../utils/http";
import {useAlert} from "../../../../utils/alerts/AlertContext";
import {createIssueDto, clarificationDto, messageDto} from "../../../../utils/types";
import {useNavigate, useParams} from "react-router-dom";
import {
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Button,
    List,
    ListItem,
    ListItemText,
} from "@mui/material";

const CreateIssuePage: React.FC = () => {
    const [formState, setFormState] = useState({
        title: "",
        description: "",
        isSubmitting: false,
        location: "",
    });

    const [currentPage, setCurrentPage] = useState(1);
    const [openDialog, setOpenDialog] = useState(false);
    const [similarIssues, setSimilarIssues] = useState<{ id: string; title: string; description: string }[]>([]);
    const [questions, setQuestions] = useState<string[]>([]);
    const [extraInfo, setExtraInfo] = useState<{ [key: string]: string }>({});

    const {eventName} = useParams<{ eventName: string }>();
    const alert = useAlert();
    const navigate = useNavigate();

    const sendAlert = (message: messageDto) => {
        alert.addAlert({message: message.message, criticality: message.criticality});
    };

    const handleExtraInfoChange = (index: number, value: string) => {
        setExtraInfo((prevState) => ({
            ...prevState,
            [index]: value,
        }));
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const {name, value} = e.target;
        setFormState((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleDialogClose = () => {
        setOpenDialog(false);
    };

    const handleProceedWithForce = () => {
        handleClarification();
        handleDialogClose();
    };

    const handleSubmit = (e?: React.FormEvent) => {
        if (e) e.preventDefault();


        const {title, description, location} = formState;

        const issueData = {
            title,
            description,
            eventName,
            location,
            extraInfo,
            questions
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
                    navigate(`/events/${eventName}/issues`);
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

    const handleClarification = () => {
        setCurrentPage(2);


        const {title, description, location} = formState;

        const issueData = {
            title,
            description,
            eventName,
            location
        };

        setFormState((prevState) => ({
            ...prevState,
            isSubmitting: true,
        }));

        const http = new HttpService();
        http.postPrivate<clarificationDto>("/private/clarification-questions", issueData)
            .then((data) => {
                if (data.code === 200 && data.body?.questions) {
                    console.log(data.body.questions)
                    setQuestions(data.body.questions);
                } else {
                    // TODO
                    // sendAlert(data.body!);
                }
            })
            .finally(() => {
                setFormState((prevState) => ({
                    ...prevState,
                    isSubmitting: false,
                }));
            });

    }

    const handleNextPage = (e?: React.FormEvent) => {


        if (e) e.preventDefault();

        const {title, description, location} = formState;

        const issueData = {
            title,
            description,
            eventName,
            location
        };

        setFormState((prevState) => ({
            ...prevState,
            isSubmitting: true,
        }));

        const http = new HttpService();
        http.postPrivate<createIssueDto>("/private/analyse-issue", issueData)
            .then((data) => {
                if (data.code === 201) {
                    //sendAlert(data.body!);
                    //navigate(`/events/${eventName}/issues`);
                    handleClarification();
                } else if (data.code === 200 && data.body?.similar) {
                    setSimilarIssues(data.body.issues);
                    setOpenDialog(true);
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
        <div className={styles.CreateIssuePage}>
            <h1>{eventName ? `${eventName}: Report Issue` : "Report Issue"}</h1>

            {currentPage === 1 && (
                <form id="create-issue-page-1" onSubmit={handleNextPage}>
                    <div>
                        <label htmlFor="title">
                            Title
                            <input
                                type="text"
                                name="title"
                                placeholder="Enter issue title"
                                value={formState.title}
                                onChange={handleInputChange}
                                required
                                maxLength={50}
                            />
                            Note: Maximum of 50 characters
                        </label>
                    </div>
                    <div>
                        <label htmlFor="description">
                            Description
                            <textarea
                                name="description"
                                placeholder="Enter issue description"
                                value={formState.description}
                                onChange={handleInputChange}
                                required
                                autoCapitalize="sentences"
                                autoCorrect="on"
                                wrap="hard"
                                maxLength={500}
                            />
                            Note: Maximum of 500 characters
                        </label>
                    </div>
                    <div>
                        <label htmlFor="location">
                            Location
                            <input
                                type="text"
                                name="location"
                                placeholder="Enter issue location"
                                value={formState.location}
                                onChange={handleInputChange}
                                required
                                maxLength={100}
                            />
                            Note: Maximum of 100 characters
                        </label>
                    </div>
                    <button type="submit" className="background_v2" disabled={formState.isSubmitting}>
                        {formState.isSubmitting ? "Next..." : "Next"}
                    </button>
                </form>
            )}

            {currentPage === 2 && (
                <form id="create-issue-page-2" onSubmit={handleSubmit}>
                    <div>
                        {questions.map((q: string, index: number) => (
                            <label key={index} htmlFor={`extraInfo-${index}`}>
                                {q}
                                <textarea
                                    name={`extraInfo-${index}`}
                                    placeholder="Optional"
                                    value={extraInfo[index] || ""} // Default to an empty string if no value exists
                                    onChange={(e) => handleExtraInfoChange(index, e.target.value)} // Update specific question
                                    maxLength={300}
                                />
                                <p>Note: Maximum of 300 characters</p>
                            </label>
                        ))}

                        {questions.length === 0 ? "Loading..." : ""}

                    </div>
                    <div style={{display: "flex", gap: "10px"}}>
                        {questions.length > 0 && (
                            <button type="submit" className="background_v2" disabled={formState.isSubmitting}>
                                {formState.isSubmitting ? "Submitting..." : "Submit"}
                            </button>)}

                    </div>
                </form>
            )}

            <Dialog open={openDialog} onClose={handleDialogClose}>
                <DialogTitle>Similar Issues Found</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        The following similar issues were found. Do you want to proceed with creating this issue?
                    </DialogContentText>
                    <List>
                        {similarIssues.map((issue) => (
                            <ListItem key={issue.id}>
                                <ListItemText primary={issue.title + ": " + issue.description}/>
                            </ListItem>
                        ))}
                    </List>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDialogClose} color="secondary">
                        Cancel
                    </Button>
                    <Button onClick={handleProceedWithForce} color="primary" autoFocus>
                        Proceed Anyway
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
};

export default CreateIssuePage;
