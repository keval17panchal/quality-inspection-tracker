package com.qc.inspection.repository;

import com.qc.inspection.entity.Inspection;
import com.qc.inspection.model.InspectionStatus;
import com.qc.inspection.model.Severity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InspectionSpecification {

    public static Specification<Inspection> filter(
            Severity severity,
            InspectionStatus status,
            LocalDate fromDate,
            LocalDate toDate,
            String machineLineId
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (severity != null) {
                predicates.add(criteriaBuilder.equal(root.get("severity"), severity));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (fromDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("inspectionDate"), fromDate));
            }

            if (toDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("inspectionDate"), toDate));
            }

            if (machineLineId != null && !machineLineId.trim().isEmpty()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("machineLineId")),
                                "%" + machineLineId.trim().toLowerCase() + "%"
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
