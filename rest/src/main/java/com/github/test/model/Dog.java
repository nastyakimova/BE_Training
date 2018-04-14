package com.github.test.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Dog {
    private String id = UUID.randomUUID().toString();

    @NotNull
    @Size(min = 1, max = 100, message = "Dog's name must be between 1 and 100 characters")
    private String name;

    @Past(message = "Date of birth can't be in future or now")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @DecimalMin(value = "0", inclusive = false, message = "Dog's weight must be greater than 0")
    private double weight;

    @DecimalMin(value = "0", inclusive = false, message = "Dog's height must be greater than 0")
    private double height;

    public Dog(String name,
               LocalDate birthDate,
               double weight,
               double height) {
        this.name = name;
        this.birthDate = birthDate;
        this.weight = weight;
        this.height = height;
    }
}
