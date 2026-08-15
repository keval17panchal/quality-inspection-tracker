import { describe, it, expect, beforeEach } from 'vitest';
import { InspectionService } from './inspection.service';
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';

describe('InspectionService', () => {
  let service: InspectionService;
  let httpClientSpy: { get: any; post: any; patch: any };

  beforeEach(() => {
    httpClientSpy = {
      get: () => of({}),
      post: () => of({}),
      patch: () => of({})
    };
    service = new InspectionService(httpClientSpy as unknown as HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call getInspections with appropriate params', () => {
    const mockData = { content: [], totalElements: 0 };
    httpClientSpy.get = () => of(mockData);

    service.getInspections({ severity: 'CRITICAL', status: 'OPEN' }).subscribe((res) => {
      expect(res).toEqual(mockData);
    });
  });

  it('should call createInspection', () => {
    const request = {
      inspectionDate: '2026-08-12',
      machineLineId: 'LINE-01',
      defectType: 'Weave Defect',
      severity: 'Critical'
    };
    const mockCreated = { id: 1, ...request, status: 'Open' };
    httpClientSpy.post = () => of(mockCreated);

    service.createInspection(request).subscribe((res) => {
      expect(res).toEqual(mockCreated);
    });
  });

  it('should call resolveInspection', () => {
    const resolveReq = { resolutionNote: 'Loom adjusted' };
    const mockResolved = { id: 1, status: 'Resolved', resolutionNote: 'Loom adjusted' };
    httpClientSpy.patch = () => of(mockResolved);

    service.resolveInspection(1, resolveReq).subscribe((res) => {
      expect(res.status).toBe('Resolved');
    });
  });

  it('should call getSummary', () => {
    const mockSummary = {
      open: { critical: 3, major: 8, minor: 12, total: 23 },
      resolved: { critical: 5, major: 14, minor: 18, total: 37 }
    };
    httpClientSpy.get = () => of(mockSummary);

    service.getSummary().subscribe((res) => {
      expect(res.open.total).toBe(23);
      expect(res.resolved.total).toBe(37);
    });
  });
});
