package com.school.management.application.api.auth;

import com.school.management.AuthenticatedUserProto;
import com.school.management.application.exceptions.BadRequestException;
import com.school.management.application.model.User;
import com.school.management.application.repositories.UsersRepository;
import com.school.management.application.utils.UserUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/authenticatedUser")
public class AuthenticatedUserController {
    @Autowired
    public UsersRepository usersRepository;

    @Autowired
    public ModelMapper mapper;

    @GetMapping()
    public AuthenticatedUserProto.AuthenticatedUserResponse authenticatedUserDetails() {
        UUID userId = UserUtils.getAuthenticatedUserId();
        User user = usersRepository.findById(userId).orElseThrow(BadRequestException::new);

        AuthenticatedUserProto.AuthenticatedUserResponse.Builder builder = AuthenticatedUserProto.AuthenticatedUserResponse.newBuilder()
                .setEmail(user.getEmail())
                .setUsername(user.getUsername())
                .setOwner(user.getOwner());
        return builder.build();
    }
}
