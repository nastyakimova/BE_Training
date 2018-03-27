package com.github.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

@Data
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Dog {
    private String id = UUID.randomUUID().toString();
    @Size(min = 1, max = 100, message = "Dog's name must be between 1 and 100 characters")
    private String name;
    @Past
    @NotNull
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate birthDate;

    @NotNull
    @Min(1)
    private double weight;
    @NotNull
    @Min(1)
    private double height;
}
