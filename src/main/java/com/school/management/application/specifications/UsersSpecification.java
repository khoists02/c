package com.school.management.application.specifications;

import com.school.management.application.criteria.PlaystationsSearchCriteria;
import com.school.management.application.criteria.UsersSearchCriteria;
import com.school.management.application.model.Playstation;
import com.school.management.application.model.User;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UsersSpecification {
    public static Specification<User> searchUserQuery(final UsersSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<Predicate>();

            if (criteria.getKeyword() != null) {
                Expression<String> searchLikeExpression = criteriaBuilder.lower(criteriaBuilder.literal("%" + criteria.getKeyword() + "%"));
                predicates.add(criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), searchLikeExpression)));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
