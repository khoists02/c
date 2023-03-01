package com.school.management.application.repositories;

import com.school.management.application.support.BaseEntity;
import com.school.management.application.support.NoCountPageRequest;
import jakarta.persistence.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.stream.Stream;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID> extends MyJpaSpecificationExecutor<T>, DynamicEntityGraph<T, ID>, JpaRepository<T, ID> {
}
