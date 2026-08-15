import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { InspectionService } from '../../services/inspection.service';

@Component({
  selector: 'app-create-inspection',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-inspection.component.html',
  styleUrl: './create-inspection.component.css'
})
export class CreateInspectionComponent implements OnInit {
  inspectionForm!: FormGroup;
  defectTypes = [
    'Weave Defect',
    'Shade Variation',
    'Hole/Tear',
    'Count Deviation',
    'Other'
  ];
  submitting = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private inspectionService: InspectionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const today = new Date().toISOString().split('T')[0];
    this.inspectionForm = this.fb.group({
      inspectionDate: [today, [Validators.required]],
      machineLineId: ['', [Validators.required]],
      defectType: ['', [Validators.required]],
      severity: ['', [Validators.required]],
      remarks: ['']
    });
  }

  isFieldInvalid(field: string): boolean {
    const control = this.inspectionForm.get(field);
    return !!(control && control.invalid && (control.dirty || control.touched || this.submitting));
  }

  setMachine(machine: string): void {
    this.inspectionForm.patchValue({ machineLineId: machine });
  }

  setSeverity(severity: string): void {
    this.inspectionForm.patchValue({ severity });
  }

  resetForm(): void {
    const today = new Date().toISOString().split('T')[0];
    this.inspectionForm.reset({
      inspectionDate: today,
      machineLineId: '',
      defectType: '',
      severity: '',
      remarks: ''
    });
    this.successMessage = '';
    this.errorMessage = '';
  }

  onSubmit(): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (this.inspectionForm.invalid) {
      this.inspectionForm.markAllAsTouched();
      this.errorMessage = 'Please complete all required fields correctly.';
      return;
    }

    this.submitting = true;
    this.inspectionService.createInspection(this.inspectionForm.value).subscribe({
      next: (res) => {
        this.submitting = false;
        this.successMessage = `Inspection #${res.id} created successfully with Open status! Redirecting to inspection list...`;
        setTimeout(() => {
          this.router.navigate(['/inspections']);
        }, 1200);
      },
      error: (err) => {
        this.submitting = false;
        this.errorMessage = err.error?.message || 'Failed to create inspection. Please try again.';
      }
    });
  }
}
