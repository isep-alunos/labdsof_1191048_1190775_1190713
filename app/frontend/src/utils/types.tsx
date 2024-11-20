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

export {
  httpResponse,
  tokenResponse,
  authUrlResponse,
  registerResponse,
  messageDto,
  criticality,
  roleRequestDto,
  listRoleRequestsRequest,
  eventDto
};
