export interface ApiResponse<T> {
  message: string;
  data: T;
  statusCode: number;
}

export interface AuthResponse {
  username: string;
  token: string;
  expiresAt: string;
  message: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface ParentRequestCreatePayload {
  studentName: string;
  parentName: string;
  phoneWhatsApp: string;
  classApplyingFor: string;
  message?: string;
  additionalChildren?: ChildApplication[];
}

export type RequestStatus =
  | 'REQUESTED_FOR_ADMISSION'
  | 'CONFIRM_ADMISSION'
  | 'CONTACTED_PARENTS'
  | 'WAITING_FOR_RESPONSE'
  | 'CONFIRMED';

export interface ParentRequestUpdatePayload {
  studentName: string;
  parentName: string;
  phoneWhatsApp: string;
  classApplyingFor: string;
  message?: string;
  additionalChildren?: ChildApplication[];
  status: RequestStatus;
}

export interface ChildApplication {
  studentName: string;
  classApplyingFor: string;
}

export interface ParentRequestRecord {
  id: number;
  studentName: string;
  parentName: string;
  phoneWhatsApp: string;
  classApplyingFor: string;
  message?: string;
  additionalChildren: ChildApplication[];
  status: RequestStatus;
  timestamp: string;
}