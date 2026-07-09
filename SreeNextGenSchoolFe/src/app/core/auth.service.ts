import { inject, Injectable, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { tap } from 'rxjs';
import { ApiService } from './api.service';
import { LoginRequest } from './api.models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiService = inject(ApiService);
  private readonly platformId = inject(PLATFORM_ID);

  private readonly tokenKey = 'sngs_admin_token';

  private get isBrowser(): boolean {
    return isPlatformBrowser(this.platformId);
  }

  login(payload: LoginRequest) {
    return this.apiService.login(payload).pipe(
      tap((response) => {
        if (response?.data?.token && this.isBrowser) {
          localStorage.setItem(this.tokenKey, response.data.token);
        }
      }),
    );
  }

  logout(): void {
    if (this.isBrowser) {
      localStorage.removeItem(this.tokenKey);
    }
  }

  getToken(): string | null {
    if (!this.isBrowser) {
      return null;
    }

    return localStorage.getItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}