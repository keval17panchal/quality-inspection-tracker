export enum DefectType {
  WEAVE_DEFECT = 'Weave Defect',
  SHADE_VARIATION = 'Shade Variation',
  HOLE_TEAR = 'Hole/Tear',
  COUNT_DEVIATION = 'Count Deviation',
  OTHER = 'Other'
}

export enum Severity {
  CRITICAL = 'Critical',
  MAJOR = 'Major',
  MINOR = 'Minor'
}

export enum InspectionStatus {
  OPEN = 'Open',
  RESOLVED = 'Resolved'
}

export interface Inspection {
  id: number;
  inspectionDate: string;
  machineLineId: string;
  defectType: DefectType | string;
  severity: Severity | string;
  remarks?: string;
  status: InspectionStatus | string;
  resolutionNote?: string;
  resolvedAt?: string;
  source?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateInspectionRequest {
  inspectionDate: string;
  machineLineId: string;
  defectType: string;
  severity: string;
  remarks?: string;
}

export interface UpdateInspectionRequest {
  inspectionDate: string;
  machineLineId: string;
  defectType: string;
  severity: string;
  remarks?: string;
}

export interface ResolveInspectionRequest {
  resolutionNote: string;
}

export interface SapWebhookRequest {
  inspectionDate: string;
  machineLineId: string;
  defectType: string;
  severity: string;
  remarks?: string;
  source?: string;
}

export interface StatusSummary {
  critical: number;
  major: number;
  minor: number;
  total: number;
}

export interface InspectionSummary {
  open: StatusSummary;
  resolved: StatusSummary;
}

export interface PageResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

export interface InspectionFilters {
  severity?: string;
  status?: string;
  fromDate?: string;
  toDate?: string;
  machineLineId?: string;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDir?: string;
}
