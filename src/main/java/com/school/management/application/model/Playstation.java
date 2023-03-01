package com.school.management.application.model;

import com.school.management.application.support.BaseEntity;
import com.school.management.application.utils.PlaystationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "playstations")
@Getter
@Setter
public class Playstation extends BaseEntity implements Serializable {
    @Column(name = "name")
    @NotNull
    private String name;

    @Min(0)
    @Max(1)
    @Column(name = "smoke")
    private Integer smoke;

    @Min(0)
    @Max(1)
    @Column(name = "matu")
    private Integer matu;

    @Min(0)
    @Max(1)
    @Column(name = "cleaning")
    private Integer cleaning;

    @Min(0)
    @Max(1)
    @Column(name = "learning")
    private Integer learning;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private PlaystationType type;

    @Column(name = "date", columnDefinition = "DATE")
    private ZonedDateTime date;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
