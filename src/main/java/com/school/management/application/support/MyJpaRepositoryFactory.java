package com.school.management.application.support;

import com.school.management.application.fragments.DynamicEntityGraphImpl;
import com.school.management.application.repositories.BaseRepository;
import com.school.management.application.repositories.DynamicEntityGraph;
import com.school.management.application.repositories.MyJpaSpecificationExecutor;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFragment;
import org.springframework.data.repository.core.support.RepositoryComposition.RepositoryFragments;

import java.io.Serializable;

public class MyJpaRepositoryFactory extends JpaRepositoryFactory {
    private EntityManager em;

    public MyJpaRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
        this.em = entityManager;
    }
    @Override
    protected RepositoryFragments getRepositoryFragments(RepositoryMetadata metadata) {
        RepositoryFragments fragments = super.getRepositoryFragments(metadata);

        if (DynamicEntityGraph.class.isAssignableFrom(metadata.getRepositoryInterface())) {
            JpaEntityInformation<?, Serializable> entityInformation = this.getEntityInformation(metadata.getDomainType());
            fragments = fragments.append(RepositoryFragment.implemented(DynamicEntityGraph.class, new DynamicEntityGraphImpl(entityInformation, em)));
        }

        return fragments;
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        if (MyJpaSpecificationExecutor.class.isAssignableFrom(metadata.getRepositoryInterface()) || BaseRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {
            return MySimpleJpaRepository.class;
        }
        return super.getRepositoryBaseClass(metadata);
     }

//    @Override
//    protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
//        JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(information.getDomainType());
//        return new DynamicEntityGraphImpl(entityInformation, em);
//    }


}
