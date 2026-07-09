import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import {
  ApiResponse,
  AuthResponse,
  LoginRequest,
  ParentRequestCreatePayload,
  ParentRequestRecord,
  ParentRequestUpdatePayload,
} from './api.models';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api';

  login(payload: LoginRequest) {
    return this.http.post<ApiResponse<AuthResponse>>(`${this.baseUrl}/auth/login`, payload);
  }

  submitParentRequest(payload: ParentRequestCreatePayload) {
    return this.http.post<ApiResponse<ParentRequestRecord>>(`${this.baseUrl}/enquiries`, payload);
  }

  getAdminRequests() {
    return this.http.get<ApiResponse<ParentRequestRecord[]>>(`${this.baseUrl}/admin/requests`);
  }

  updateAdminRequest(id: number, payload: ParentRequestUpdatePayload) {
    return this.http.put<ApiResponse<ParentRequestRecord>>(`${this.baseUrl}/admin/requests/${id}`, payload);
  }
}
