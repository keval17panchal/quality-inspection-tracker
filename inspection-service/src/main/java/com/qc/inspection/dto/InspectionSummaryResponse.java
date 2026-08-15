package com.qc.inspection.dto;

import java.util.Map;

public class InspectionSummaryResponse {

    private StatusSummary open;
    private StatusSummary resolved;

    public InspectionSummaryResponse() {}

    public InspectionSummaryResponse(StatusSummary open, StatusSummary resolved) {
        this.open = open;
        this.resolved = resolved;
    }

    public StatusSummary getOpen() { return open; }
    public void setOpen(StatusSummary open) { this.open = open; }

    public StatusSummary getResolved() { return resolved; }
    public void setResolved(StatusSummary resolved) { this.resolved = resolved; }

    public static class StatusSummary {
        private long critical;
        private long major;
        private long minor;
        private long total;

        public StatusSummary() {}

        public StatusSummary(long critical, long major, long minor) {
            this.critical = critical;
            this.major = major;
            this.minor = minor;
            this.total = critical + major + minor;
        }

        public long getCritical() { return critical; }
        public void setCritical(long critical) { this.critical = critical; }

        public long getMajor() { return major; }
        public void setMajor(long major) { this.major = major; }

        public long getMinor() { return minor; }
        public void setMinor(long minor) { this.minor = minor; }

        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
    }
}
