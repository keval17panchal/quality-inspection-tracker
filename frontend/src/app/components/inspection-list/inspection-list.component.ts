import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { InspectionService } from '../../services/inspection.service';
import { AuthService } from '../../services/auth.service';
import { Inspection, InspectionFilters, PageResponse, UpdateInspectionRequest } from '../../models/inspection.model';
import { ResolveModalComponent } from '../resolve-modal/resolve-modal.component';

@Component({
  selector: 'app-inspection-list',
  standalone: true,
  imports: [CommonModule, FormsModule, ResolveModalComponent],
  templateUrl: './inspection-list.component.html',
  styleUrl: './inspection-list.component.css'
})
export class InspectionListComponent implements OnInit {
  pageData: PageResponse<Inspection> | null = null;
  loading = false;
  errorMessage = '';
  toastMessage = '';
  showFilters = false;

  filters: InspectionFilters = {
    severity: 'ALL',
    status: 'ALL',
    fromDate: '',
    toDate: '',
    machineLineId: '',
    page: 0,
    size: 10,
    sortBy: 'createdAt',
    sortDir: 'desc'
  };

  selectedInspectionForResolve: Inspection | null = null;

  // Edit modal state
  showEditModal = false;
  editingInspection: Inspection | null = null;
  editModel: UpdateInspectionRequest = {
    inspectionDate: '',
    machineLineId: '',
    defectType: 'Weave Defect',
    severity: 'Minor',
    remarks: ''
  };
  editError = '';
  updating = false;

  // Delete modal state
  showDeleteModal = false;
  deletingInspection: Inspection | null = null;
  deleteError = '';
  deleting = false;

  constructor(
    private inspectionService: InspectionService,
    public authService: AuthService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((queryParams) => {
      if (queryParams['status']) {
        this.filters.status = queryParams['status'];
        this.showFilters = true;
      }
      this.loadInspections();
    });
  }

  toggleFilters(): void {
    this.showFilters = !this.showFilters;
  }

  toggleSortDir(): void {
    this.filters.sortDir = this.filters.sortDir === 'asc' ? 'desc' : 'asc';
    this.applyFilters();
  }

  applyFilters(): void {
    this.filters.page = 0;
    this.loadInspections();
  }

  resetFilters(): void {
    this.filters = {
      severity: 'ALL',
      status: 'ALL',
      fromDate: '',
      toDate: '',
      machineLineId: '',
      page: 0,
      size: 10,
      sortBy: 'createdAt',
      sortDir: 'desc'
    };
    this.router.navigate([], { queryParams: {} });
    this.loadInspections();
  }

  loadInspections(): void {
    this.loading = true;
    this.errorMessage = '';

    this.inspectionService.getInspections(this.filters).subscribe({
      next: (data) => {
        this.pageData = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = 'Failed to load inspections. Please check backend connection.';
        this.cdr.detectChanges();
      }
    });
  }

  goToPage(page: number): void {
    this.filters.page = page;
    this.loadInspections();
  }

  getPagesArray(): number[] {
    if (!this.pageData) return [];
    const total = this.pageData.totalPages;
    const current = this.pageData.pageNumber;
    const pages: number[] = [];

    for (let i = Math.max(0, current - 2); i <= Math.min(total - 1, current + 2); i++) {
      pages.push(i);
    }
    return pages;
  }

  openResolveModal(inspection: Inspection): void {
    this.selectedInspectionForResolve = inspection;
  }

  closeResolveModal(): void {
    this.selectedInspectionForResolve = null;
  }

  onInspectionResolved(updated: Inspection): void {
    this.closeResolveModal();
    this.toastMessage = `Inspection #${updated.id} successfully resolved!`;
    setTimeout(() => {
      this.toastMessage = '';
    }, 4000);
    this.loadInspections();
  }

  // Edit actions
  openEditModal(inspection: Inspection): void {
    this.editingInspection = inspection;
    this.editModel = {
      inspectionDate: inspection.inspectionDate,
      machineLineId: inspection.machineLineId,
      defectType: inspection.defectType,
      severity: inspection.severity,
      remarks: inspection.remarks || ''
    };
    this.editError = '';
    this.showEditModal = true;
  }

  closeEditModal(): void {
    this.showEditModal = false;
    this.editingInspection = null;
    this.editError = '';
  }

  submitEdit(): void {
    if (!this.editingInspection?.id) return;
    if (!this.editModel.inspectionDate || !this.editModel.machineLineId || !this.editModel.defectType || !this.editModel.severity) {
      this.editError = 'Please fill all required fields.';
      return;
    }

    this.updating = true;
    this.editError = '';
    this.inspectionService.updateInspection(this.editingInspection.id, this.editModel).subscribe({
      next: (updated) => {
        this.updating = false;
        this.closeEditModal();
        this.toastMessage = `Inspection #${updated.id} successfully updated!`;
        setTimeout(() => {
          this.toastMessage = '';
        }, 4000);
        this.loadInspections();
      },
      error: (err) => {
        this.updating = false;
        this.editError = err.error?.message || 'Failed to update inspection.';
        this.cdr.detectChanges();
      }
    });
  }

  // Delete actions
  openDeleteModal(inspection: Inspection): void {
    this.deletingInspection = inspection;
    this.deleteError = '';
    this.showDeleteModal = true;
  }

  closeDeleteModal(): void {
    this.showDeleteModal = false;
    this.deletingInspection = null;
    this.deleteError = '';
  }

  confirmDelete(): void {
    if (!this.deletingInspection?.id) return;

    this.deleting = true;
    this.deleteError = '';
    this.inspectionService.deleteInspection(this.deletingInspection.id).subscribe({
      next: () => {
        const id = this.deletingInspection?.id;
        this.deleting = false;
        this.closeDeleteModal();
        this.toastMessage = `Inspection #${id} successfully deleted!`;
        setTimeout(() => {
          this.toastMessage = '';
        }, 4000);
        this.loadInspections();
      },
      error: (err) => {
        this.deleting = false;
        this.deleteError = err.error?.message || 'Failed to delete inspection.';
        this.cdr.detectChanges();
      }
    });
  }
}
