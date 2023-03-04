package com.school.management.application.api.auth;

import com.school.management.AuthenticatedUserProto;
import com.school.management.application.exceptions.ApplicationException;
import com.school.management.application.exceptions.BadRequestException;
import com.school.management.application.model.User;
import com.school.management.application.repositories.UsersRepository;
import com.school.management.application.services.AuthenticationService;
import com.school.management.application.utils.UserUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticateController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ModelMapper mapper;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void authenticate(@RequestBody AuthenticatedUserProto.AuthenticateRequest request) {
        try {
            authenticationService.authenticate(
                    AuthenticationService.AuthenticationContext.builder()
                            .username(request.getUsername())
                            .password(request.getPassword()).build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/register")
    public AuthenticatedUserProto.RegisterUserResponse register(@RequestBody AuthenticatedUserProto.RegisterUserRequest request) {
        if (!request.getPassword().equals(request.getComparePassword())) throw new BadRequestException("invalid");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setComparePassword(passwordEncoder.encode(request.getComparePassword()));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setOwner(true);
        User newUser = usersRepository.save(user);

        AuthenticatedUserProto.RegisterUserResponse.Builder builder = AuthenticatedUserProto.RegisterUserResponse.newBuilder()
                .setUsername(newUser.getUsername());

        return builder.build();

    }

}
