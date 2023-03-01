package com.school.management.application.services;

import com.school.management.UserProto;
import com.school.management.application.criteria.PlaystationsSearchCriteria;
import com.school.management.application.criteria.UsersSearchCriteria;
import com.school.management.application.model.Playstation;
import com.school.management.application.model.User;
import com.school.management.application.repositories.UsersRepository;
import com.school.management.application.specifications.PlaystationSpecification;
import com.school.management.application.specifications.UsersSpecification;
import jakarta.persistence.EntityGraph;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    ModelMapper modelMapper;

    public Page<User> finAll(Pageable pageable, UsersSearchCriteria usersSearchCriteria) {
        EntityGraph<User> entityGraph = usersRepository.createEntityGraph();
        return usersRepository.findAll(UsersSpecification.searchUserQuery(usersSearchCriteria), pageable, entityGraph);
    }
}
