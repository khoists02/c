package com.school.management.application.api.auth;

import com.school.management.AuthenticatedUserProto;
import com.school.management.application.repositories.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AuthenticatedUserController {
    @Autowired
    public UsersRepository usersRepository;

    @Autowired
    public ModelMapper mapper;

    public AuthenticatedUserProto.AuthenticatedUser authenticatedUser() {
        AuthenticatedUserProto.AuthenticatedUser data =  mapper.map(usersRepository.findById(UUID.fromString("abcdef")), AuthenticatedUserProto.AuthenticatedUser.class);

        return data;
    }
}
