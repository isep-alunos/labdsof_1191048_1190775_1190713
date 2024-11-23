import React, {useState, useEffect} from "react";
import styles from "./CreateIssuePage.module.css";
import HttpService from "../../../../utils/http";
import {useAlert} from "../../../../utils/alerts/AlertContext";
import {criticality, createIssueDto} from "../../../../utils/types";
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

type issueWorker = { id: string; name: string; email: string };

const CreateIssuePage: React.FC = () => {
    const [formState, setFormState] = useState({
        title: "",
        description: "",
        isSubmitting: false,
        location: "",
    });

    const [openDialog, setOpenDialog] = useState(false);
    const [similarIssues, setSimilarIssues] = useState<{ id: string; title: string, description: string }[]>([]);

    const {eventName} = useParams<{ eventName: string }>();
    const alert = useAlert();
    const navigate = useNavigate();

    const sendAlert = (message: createIssueDto) => {
        alert.addAlert({message: message.message, criticality: message.criticality});
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
        handleSubmit(undefined, true); // Pass 'true' to force submission
        handleDialogClose();
    };

    const handleSubmit = (e?: React.FormEvent, force = false) => {
        if (e) e.preventDefault();

        const {title, description, location} = formState;

        const issueData = {
            title,
            description,
            eventName,
            location,
            ...(force && {force: true}), // Add 'force' flag if requested
        };

        setFormState((prevState) => ({
            ...prevState,
            isSubmitting: true,
        }));

        const http = new HttpService();
        http.postPrivate<createIssueDto>("/private/create-issue", issueData)
            .then((data) => {
                if (data.code === 201) {
                    sendAlert(data.body!);
                    navigate(`/events/${eventName}/issues`);
                } else if (data.code === 200 && data.body?.similar) {
                    // Open dialog with similar issues
                    console.log(data.body.issues)
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

            {/* Material-UI Dialog */}
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
