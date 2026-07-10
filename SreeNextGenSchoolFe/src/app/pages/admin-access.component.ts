import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { finalize, timeout } from 'rxjs';
import { AuthService } from '../core/auth.service';

@Component({
  standalone: true,
  selector: 'app-admin-access',
  imports: [FormsModule, RouterLink],
  templateUrl: './admin-access.component.html',
  styleUrl: './admin-access.component.scss',
})
export class AdminAccessComponent {
  username = '';
  password = '';
  passwordVisible = false;
  loading = false;
  errorMessage = '';

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
  ) {}

  get canLogin(): boolean {
    return !!this.username.trim() && !!this.password && !this.loading;
  }

  updateUsername(): void {
    this.errorMessage = '';
  }

  updatePassword(): void {
    this.errorMessage = '';
  }

  togglePassword(): void {
    this.passwordVisible = !this.passwordVisible;
  }

  handleEnter(event: Event): void {
    event.preventDefault();
    this.login();
  }

  login(): void {
    if (!this.canLogin) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') ?? '/admin/requests';

    this.authService.login({ username: this.username.trim(), password: this.password }).pipe(
      timeout(10000),
      finalize(() => {
        this.loading = false;
      }),
    ).subscribe({
      next: () => {
        this.router.navigateByUrl(returnUrl);
      },
      error: () => {
        this.errorMessage = 'Invalid credentials.';
      },
    });
  }
}
