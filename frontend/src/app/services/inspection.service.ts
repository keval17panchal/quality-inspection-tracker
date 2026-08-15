import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  Inspection,
  CreateInspectionRequest,
  ResolveInspectionRequest,
  SapWebhookRequest,
  InspectionSummary,
  PageResponse,
  InspectionFilters
} from '../models/inspection.model';

@Injectable({
  providedIn: 'root'
})
export class InspectionService {
  private apiUrl = '/api';

  constructor(private http: HttpClient) {}

  getInspections(filters: InspectionFilters = {}): Observable<PageResponse<Inspection>> {
    let params = new HttpParams();

    if (filters.severity && filters.severity !== 'ALL') {
      params = params.set('severity', filters.severity);
    }
    if (filters.status && filters.status !== 'ALL') {
      params = params.set('status', filters.status);
    }
    if (filters.fromDate) {
      params = params.set('fromDate', filters.fromDate);
    }
    if (filters.toDate) {
      params = params.set('toDate', filters.toDate);
    }
    if (filters.machineLineId && filters.machineLineId.trim() !== '') {
      params = params.set('machineLineId', filters.machineLineId.trim());
    }
    if (filters.page !== undefined) {
      params = params.set('page', filters.page.toString());
    }
    if (filters.size !== undefined) {
      params = params.set('size', filters.size.toString());
    }
    if (filters.sortBy) {
      params = params.set('sortBy', filters.sortBy);
    }
    if (filters.sortDir) {
      params = params.set('sortDir', filters.sortDir);
    }

    return this.http.get<PageResponse<Inspection>>(`${this.apiUrl}/inspections`, { params });
  }

  getInspectionById(id: number): Observable<Inspection> {
    return this.http.get<Inspection>(`${this.apiUrl}/inspections/${id}`);
  }

  createInspection(request: CreateInspectionRequest): Observable<Inspection> {
    return this.http.post<Inspection>(`${this.apiUrl}/inspections`, request);
  }

  updateInspection(id: number, request: Partial<CreateInspectionRequest>): Observable<Inspection> {
    return this.http.put<Inspection>(`${this.apiUrl}/inspections/${id}`, request);
  }

  deleteInspection(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/inspections/${id}`);
  }

  resolveInspection(id: number, request: ResolveInspectionRequest): Observable<Inspection> {
    return this.http.patch<Inspection>(`${this.apiUrl}/inspections/${id}/resolve`, request);
  }

  getSummary(): Observable<InspectionSummary> {
    return this.http.get<InspectionSummary>(`${this.apiUrl}/summary`);
  }

  sendSapWebhook(request: SapWebhookRequest): Observable<Inspection> {
    return this.http.post<Inspection>(`${this.apiUrl}/sap-webhook`, request);
  }
}
