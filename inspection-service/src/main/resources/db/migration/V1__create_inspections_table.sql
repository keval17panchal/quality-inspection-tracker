CREATE TABLE IF NOT EXISTS inspections (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    inspection_date DATE NOT NULL,
    machine_line_id VARCHAR(100) NOT NULL,
    defect_type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    remarks TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    resolution_note TEXT,
    resolved_at DATETIME,
    source VARCHAR(50) DEFAULT 'MANUAL',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_inspection_date (inspection_date),
    INDEX idx_machine_line_id (machine_line_id),
    INDEX idx_severity (severity),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
