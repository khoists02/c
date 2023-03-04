package com.school.management.application.support;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;
import java.util.UUID;

public interface RequestContext {

    @Nullable
    UUID getUserId();

    void setUserId(@Nullable UUID id);

    void clear();
}
