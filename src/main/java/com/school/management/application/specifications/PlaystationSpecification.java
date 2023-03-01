package com.school.management.application.specifications;

import com.school.management.application.criteria.PlaystationsSearchCriteria;
import com.school.management.application.model.Playstation;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PlaystationSpecification {
    public static Specification<Playstation> playstationQuery(final PlaystationsSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<Predicate>();

            if (criteria.getKeyword() != null) {
                Expression<String> searchLikeExpression = criteriaBuilder.lower(criteriaBuilder.literal("%" + criteria.getKeyword() + "%"));
                predicates.add(criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchLikeExpression)));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
