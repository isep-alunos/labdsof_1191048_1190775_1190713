type httpResponse<T> = {
  code: number;
  body?: T;
};

type tokenResponse = {
  waiting_for_response: boolean;
  success: boolean;
  token?: string;
  refreshToken?: string;
  name?: string;
  email?: string;
  picture?: string;
  isNewUser?: boolean;
  isEventWorker?: boolean;
  isEventManager?: boolean;
  isAdmin?: boolean;
  expireDate?: Date;
  isWaitingForApprovalForEventWorker?: boolean;
  isWaitingForApprovalForEventManager?: boolean;
};

type authUrlResponse = {
  authURL: string;
};

enum criticality {
  SUCCESS,
  INFO,
  WARNING,
  ERROR,
}

type messageDto = {
  message: string;
  criticality: criticality;
};

type registerResponse = {
  success: boolean;
  messages?: messageDto[];
};

type markPresenceResponse = {
  success: boolean;
  messages?: messageDto[];
};

type markPresenceRequest = {
  name: string;
  latitude: number;
  longitude: number;
};

type roleRequestDto = {
  name: string;
  email: string;
  role: string;
  justification: string;
};

type listRoleRequestsRequest = {
  requests: roleRequestDto[];
};

type eventDto = {
  name: string;
  description: string;
  startDate: string;
  endDate: string;
  maxParticipants: number;
  eventWebsite: string;
  location: {
    latitude: number;
    longitude: number;
    street: string;
    number: number;
    postalCode: string;
  };
  eventWorkerNames: string[];
};

enum issueStatus {
  PENDING = "PENDING",
  IN_PROGRESS = "IN_PROGRESS",
  RESOLVED = "RESOLVED",
  REJECTED = "REJECTED",
}

interface issueLocation {
  location: string; // e.g., "Main Street 123, City"
}

interface issueStatusUpdate {
  id: string;
  updateTime: string; // ISO date string, e.g., "2024-11-21T14:30:00Z"
  description: string; // e.g., "Reported to the authorities"
  status: issueStatus; // Status of the issue
}

interface issue {
  id: string; // Unique identifier for the issue
  creationDate: string; // ISO date string, e.g., "2024-11-21T10:15:00Z"
  title: string; // Short description of the issue, e.g., "Broken streetlight"
  description: string; // Detailed explanation of the issue
  issueStatusUpdateList: issueStatusUpdate[]; // History of status updates
  location: issueLocation; // Location details for the issue
}


export {
  httpResponse,
  tokenResponse,
  authUrlResponse,
  registerResponse,
  messageDto,
  criticality,
  roleRequestDto,
  listRoleRequestsRequest,
  eventDto,
  markPresenceRequest,
  markPresenceResponse,
  issue,
  issueLocation,
  issueStatus,
  issueStatusUpdate
};
