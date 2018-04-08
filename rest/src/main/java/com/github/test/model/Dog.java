package com.github.test.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Dog {
    private String id = UUID.randomUUID().toString();

    @Size(min = 1, max = 100, message = "Dog's name must be between 1 and 100 characters")
    private String name;

    @Past
    @NotNull
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Min(1)
    private double weight;

    @Min(1)
    private double height;

    public Dog(@Size(min = 1, max = 100, message = "Dog's name must be between 1 and 100 characters") String name,
               @Past @NotNull LocalDate birthDate, @NotNull @Min(1) double weight,
               @NotNull @Min(1) double height) {
        this.name = name;
        this.birthDate = birthDate;
        this.weight = weight;
        this.height = height;
    }
}
