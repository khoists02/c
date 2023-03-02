package com.school.management.application.utils;

import com.school.management.AuthenticatedUserProto;
import com.school.management.application.model.User;
import com.school.management.application.repositories.UsersRepository;
import com.school.management.application.services.TransactionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.Callable;

@Component
public class UserUtils {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TransactionHandler transactionHandler;

    private static UserUtils self;

    public UserUtils () { self = this; }

    public static User getUser(UUID id) {
        try {
            return self.transactionHandler.runInReadOnlyTransaction((Callable<User>) ()-> self.usersRepository.getReferenceById(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    public static boolean hasAuthentication() {
//        return SecurityContextHolder.getContext().getAuthentication() instanceof AuthenticatedUserProto.AuthenticatedUser
//    }
}
