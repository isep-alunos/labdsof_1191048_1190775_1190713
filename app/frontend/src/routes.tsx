export const ROUTES = {
    HOME: "/",
    LOGIN: "/login",
    PRIVATE_USER: "/private",
    EVENT_WORKER: "/event-worker",
    EVENT_WORKER_EVENT_PAGE: "/event-worker/:eventName",
    EVENT_MANAGER_EVENT_PAGE: "/event-manager/event/:eventName",
    EVENT_MANAGER: "/event-manager",
    CREATE_EVENT: "/event-manager/create-event",
    ADMIN: "/admin",
    APPROVE_USERS: "/admin/approve-users",
    EVENT_ISSUES: "/events/:eventName/issues",
    CREATE_ISSUE: "/events/:eventName/report-issue",
    LEADERBOARD: "/leaderboard",
    LOST: "*"
};