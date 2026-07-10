import { Component } from '@angular/core';
import { ChangeDetectorRef } from '@angular/core';
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../core/api.service';
import { ChildApplication, ParentRequestCreatePayload } from '../core/api.models';
import { firstValueFrom } from 'rxjs';

type BackendErrorResponse = {
  message?: string;
  error?: {
    message?: string;
  } | string;
};

@Component({
  standalone: true,
  selector: 'app-request-admission',
  imports: [ReactiveFormsModule],
  templateUrl: './request-admission.component.html',
  styleUrl: './request-admission.component.scss',
})
export class RequestAdmissionComponent {
  private readonly phoneWhatsAppPattern = /^[6-9]\d{9}$/;

  submitted = false;
  submitting = false;
  errorMessage = '';
  successMessage = '';

  form = new FormBuilder().group({
    studentName: ['', [Validators.required]],
    parentName: ['', [Validators.required]],
    phoneWhatsApp: ['', [Validators.required, Validators.pattern(this.phoneWhatsAppPattern)]],
    classApplyingFor: ['', [Validators.required]],
    message: [''],
    additionalChildren: new FormArray([]),
  });

  constructor(
    private readonly apiService: ApiService,
    private readonly changeDetectorRef: ChangeDetectorRef,
  ) {}

  get additionalChildren(): FormArray {
    return this.form.get('additionalChildren') as FormArray;
  }

  get phoneWhatsAppInvalid(): boolean {
    const control = this.form.controls.phoneWhatsApp;
    return control.invalid && (control.dirty || control.touched);
  }

  addNextChild(): void {
    this.additionalChildren.push(
      new FormBuilder().group({
        studentName: ['', [Validators.required]],
        classApplyingFor: ['', [Validators.required]],
      }),
    );
  }

  removeChild(index: number): void {
    this.additionalChildren.removeAt(index);
  }

  async submit(): Promise<void> {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting = true;
    this.errorMessage = '';
    this.successMessage = '';

    const rawValue = this.form.getRawValue() as {
      studentName: string;
      parentName: string;
      phoneWhatsApp: string;
      classApplyingFor: string;
      message: string | null;
      additionalChildren: Array<{
        studentName: string;
        classApplyingFor: string;
      }>;
    };

    const payload: ParentRequestCreatePayload = {
      studentName: rawValue.studentName,
      parentName: rawValue.parentName,
      phoneWhatsApp: rawValue.phoneWhatsApp,
      classApplyingFor: rawValue.classApplyingFor,
      message: rawValue.message ?? undefined,
      additionalChildren: rawValue.additionalChildren.map((child): ChildApplication => ({
        studentName: child.studentName,
        classApplyingFor: child.classApplyingFor,
      })),
    };

    try {
      await firstValueFrom(this.apiService.submitParentRequest(payload));
      this.submitted = true;
      this.successMessage = 'Request submitted successfully.';
      this.form.reset();
      this.additionalChildren.clear();
    } catch (error) {
      this.errorMessage = this.extractErrorMessage(error);
    } finally {
      this.submitting = false;
      this.changeDetectorRef.detectChanges();
    }
  }

  private extractErrorMessage(error: unknown): string {
    const fallbackMessage = 'Unable to submit request right now. Please try again.';
    if (!error || typeof error !== 'object') {
      return fallbackMessage;
    }

    const backendError = error as BackendErrorResponse;
    if (
      backendError.error &&
      typeof backendError.error === 'object' &&
      typeof backendError.error.message === 'string' &&
      backendError.error.message.trim()
    ) {
      return backendError.error.message;
    }

    if (typeof backendError.error === 'string' && backendError.error.trim()) {
      try {
        const parsedError = JSON.parse(backendError.error) as BackendErrorResponse;
        if (typeof parsedError.message === 'string' && parsedError.message.trim()) {
          return parsedError.message;
        }
      } catch {
        return backendError.error;
      }
    }

    if (typeof backendError.message === 'string' && backendError.message.trim()) {
      return backendError.message;
    }

    return fallbackMessage;
  }
}
