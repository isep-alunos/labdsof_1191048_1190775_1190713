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

type createIssueDto = {
  message: string;
  criticality: criticality;
  similar: boolean;
  count: number;
  issues: any;
  created: boolean;
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
  location: string;
}

interface issueStatusUpdate {
  updateTime: string;
  description: string;
  status: issueStatus;
}

interface issue {
  id: string;
  creationDate: string;
  title: string;
  description: string;
  issueStatusUpdateList: issueStatusUpdate[];
  location: issueLocation;
  eventName: string;
  reactions: number;
  userReacted: boolean;
  userIsOwner: boolean;
}

type userProfileResponse = {
  user: {
    name: string;
    email: string;
  };
  attendedEvents: eventDto[];
  totalPointsAccumulated: number;
  pointsAvailable: number;
};

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
  issueStatusUpdate,
  createIssueDto,
  userProfileResponse,
};
