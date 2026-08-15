package com.qc.inspection.repository;

import com.qc.inspection.entity.Inspection;
import com.qc.inspection.model.InspectionStatus;
import com.qc.inspection.model.Severity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Long>, JpaSpecificationExecutor<Inspection> {

    @Query("SELECT i.status, i.severity, COUNT(i) FROM Inspection i GROUP BY i.status, i.severity")
    List<Object[]> countByStatusAndSeverityGrouped();

    long countByStatusAndSeverity(InspectionStatus status, Severity severity);
}
