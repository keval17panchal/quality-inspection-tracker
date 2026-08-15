import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InspectionService } from '../../services/inspection.service';

@Component({
  selector: 'app-sap-mock',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './sap-mock.component.html',
  styleUrl: './sap-mock.component.css'
})
export class SapMockComponent implements OnInit {
  sapForm!: FormGroup;
  submitting = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private inspectionService: InspectionService
  ) {}

  ngOnInit(): void {
    const today = new Date().toISOString().split('T')[0];
    this.sapForm = this.fb.group({
      inspectionDate: [today, [Validators.required]],
      machineLineId: ['SAP-LINE-01', [Validators.required]],
      defectType: ['Shade Variation', [Validators.required]],
      severity: ['Major', [Validators.required]],
      source: ['SAP_ERP_PROD_1'],
      remarks: ['Automated quality defect triggered by SAP S/4HANA Quality Management module.']
    });
  }

  onSubmit(): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (this.sapForm.invalid) {
      this.errorMessage = 'Please complete all required SAP fields.';
      return;
    }

    this.submitting = true;
    this.inspectionService.sendSapWebhook(this.sapForm.value).subscribe({
      next: (res) => {
        this.submitting = false;
        this.successMessage = `SAP Webhook processed! Created Inspection #${res.id} for Machine ${res.machineLineId} with status Open.`;
      },
      error: (err) => {
        this.submitting = false;
        this.errorMessage = err.error?.message || 'Failed to trigger SAP webhook.';
      }
    });
  }
}
