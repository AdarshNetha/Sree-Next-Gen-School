import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../core/api.service';
import { AuthService } from '../core/auth.service';
import { ParentRequestRecord, RequestStatus } from '../core/api.models';
import { finalize } from 'rxjs';

type RequestStatusFilter = RequestStatus | 'ALL';

const REQUEST_STATUS_OPTIONS: RequestStatus[] = [
  'REQUESTED_FOR_ADMISSION',
  'CONFIRM_ADMISSION',
  'CONTACTED_PARENTS',
  'WAITING_FOR_RESPONSE',
  'CONFIRMED',
];

const REQUEST_STATUS_FILTER_OPTIONS: RequestStatusFilter[] = ['ALL', ...REQUEST_STATUS_OPTIONS];

@Component({
  standalone: true,
  selector: 'app-admin-requests',
  imports: [FormsModule],
  templateUrl: './admin-requests.component.html',
  styleUrl: './admin-requests.component.scss',
})
export class AdminRequestsComponent implements OnInit {
  loading = false;
  savingId: number | null = null;
  errorMessage = '';
  requests: ParentRequestRecord[] = [];
  selectedStatusFilter: RequestStatusFilter = 'ALL';
  currentPage = 1;
  readonly pageSize = 5;
  readonly requestStatusOptions = REQUEST_STATUS_OPTIONS;
  readonly requestStatusFilterOptions = REQUEST_STATUS_FILTER_OPTIONS;

  constructor(
    private readonly apiService: ApiService,
    private readonly authService: AuthService,
    private readonly router: Router,
    private readonly changeDetectorRef: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.loadRequests();
  }

  loadRequests(): void {
    this.loading = true;
    this.errorMessage = '';
    this.changeDetectorRef.detectChanges();

    this.apiService
      .getAdminRequests()
      .pipe(
        finalize(() => {
          this.loading = false;
          this.changeDetectorRef.detectChanges();
        }),
      )
      .subscribe({
        next: (response) => {
          this.requests = response.data ?? [];
          this.currentPage = 1;
          this.changeDetectorRef.detectChanges();
        },
        error: () => {
          this.errorMessage = 'Unable to load admin requests. Please log in again.';
          this.changeDetectorRef.detectChanges();
        },
      });
  }

  get filteredRequests(): ParentRequestRecord[] {
    if (this.selectedStatusFilter === 'ALL') {
      return this.requests;
    }

    return this.requests.filter((request) => request.status === this.selectedStatusFilter);
  }

  get totalPages(): number {
    return Math.max(1, Math.ceil(this.filteredRequests.length / this.pageSize));
  }

  get paginatedRequests(): ParentRequestRecord[] {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    return this.filteredRequests.slice(startIndex, startIndex + this.pageSize);
  }

  get pageSummary(): string {
    const totalRequests = this.filteredRequests.length;

    if (!totalRequests) {
      return '0 requests';
    }

    const startItem = (this.currentPage - 1) * this.pageSize + 1;
    const endItem = Math.min(this.currentPage * this.pageSize, totalRequests);
    return `${startItem}-${endItem} of ${totalRequests}`;
  }

  onStatusFilterChange(nextStatus: string): void {
    this.selectedStatusFilter = nextStatus as RequestStatusFilter;
    this.currentPage = 1;
  }

  previousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage -= 1;
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage += 1;
    }
  }

  updateStatus(request: ParentRequestRecord, nextStatusValue: string): void {
    const nextStatus = this.requestStatusOptions.find((status) => status === nextStatusValue);

    if (!nextStatus || request.status === nextStatus) {
      return;
    }

    this.savingId = request.id;
    this.errorMessage = '';

    this.apiService
      .updateAdminRequest(request.id, {
        studentName: request.studentName,
        parentName: request.parentName,
        phoneWhatsApp: request.phoneWhatsApp,
        classApplyingFor: request.classApplyingFor,
        message: request.message,
        additionalChildren: request.additionalChildren,
        status: nextStatus,
      })
      .pipe(
        finalize(() => {
          this.savingId = null;
          this.changeDetectorRef.detectChanges();
        }),
      )
      .subscribe({
        next: (response) => {
          const updatedRequest = response.data;
          this.requests = this.requests.map((item) =>
            item.id === updatedRequest.id ? updatedRequest : item,
          );
          this.currentPage = Math.min(this.currentPage, this.totalPages);
          this.changeDetectorRef.detectChanges();
        },
        error: () => {
          this.errorMessage = 'Unable to update request status. Please try again.';
          this.changeDetectorRef.detectChanges();
        },
      });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/admin/login');
  }
}