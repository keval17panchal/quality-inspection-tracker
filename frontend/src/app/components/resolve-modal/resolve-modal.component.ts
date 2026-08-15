import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InspectionService } from '../../services/inspection.service';
import { Inspection } from '../../models/inspection.model';

@Component({
  selector: 'app-resolve-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './resolve-modal.component.html',
  styleUrl: './resolve-modal.component.css'
})
export class ResolveModalComponent {
  @Input() inspection!: Inspection;
  @Output() close = new EventEmitter<void>();
  @Output() resolved = new EventEmitter<Inspection>();

  resolutionNote = '';
  submitting = false;
  errorMessage = '';

  constructor(private inspectionService: InspectionService) {}

  onClose(): void {
    this.close.emit();
  }

  onResolve(): void {
    if (!this.resolutionNote.trim()) {
      this.errorMessage = 'Resolution note is required.';
      return;
    }

    this.submitting = true;
    this.errorMessage = '';

    this.inspectionService.resolveInspection(this.inspection.id, { resolutionNote: this.resolutionNote }).subscribe({
      next: (updatedInspection) => {
        this.submitting = false;
        this.resolved.emit(updatedInspection);
      },
      error: (err) => {
        this.submitting = false;
        this.errorMessage = err.error?.message || 'Failed to resolve inspection. Please try again.';
      }
    });
  }
}
