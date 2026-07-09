import { Component } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../core/auth.service';

@Component({
  standalone: true,
  selector: 'app-admin-login',
  imports: [ReactiveFormsModule],
  templateUrl: './admin-login.component.html',
  styleUrl: './admin-login.component.scss',
})
export class AdminLoginComponent {
  errorMessage = '';
  loading = false;

  form = new FormBuilder().group({
    username: ['admin', [Validators.required]],
    password: ['admin123', [Validators.required]],
  });

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
  ) {}

  login(): void {
    if (this.form.invalid) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    const payload = this.form.getRawValue();
    const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') ?? '/admin/requests';

    this.authService.login({ username: payload.username ?? '', password: payload.password ?? '' }).subscribe({
      next: () => {
        this.router.navigateByUrl(returnUrl);
      },
      error: () => {
        this.errorMessage = 'Invalid admin username or password.';
        this.loading = false;
      },
      complete: () => {
        this.loading = false;
      },
    });
  }
}