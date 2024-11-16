type tokenResponse = {
  success: boolean;
  token?: string;
  refreshToken?: string;
  name?: string;
  picture?: string;
  isEventWorker?: boolean;
  isEventManager?: boolean;
  isAdmin?: boolean;
  expireDate?: Date;
};

type viewReservasResponse = {
  hotelName: string;
  user: userResponse;
  quarto: quartoResponse;
  dataInicio: string;
  dataFim: string;
  estadoReserva: string;
};

type userResponse = {
  email: string;
  nome: string;
};

type quartoResponse = {
  number: number;
  floor: number;
  area?: number;
  numBeds: number;
  bedType: string;
  roomType: string; //tabela
  state: string;
};

type disponibilidadeResponse = {
  start: Date;
  end: Date;
  hotels: hoteisInfoResponse[];
};

type hoteisInfoResponse = {
  name: string;
  address: string;
  phone?: string;
  rooms: quartoResponse[];
};

type httpResponse<T> = {
  code: number;
  body?: T;
};

type authUrlResponse = {
  authURL: string;
};

type createReservation = {
  hotelName: string;
  roomFloor: number;
  roomNumber: number;
  startDate: string;
  endDate: string;
};
