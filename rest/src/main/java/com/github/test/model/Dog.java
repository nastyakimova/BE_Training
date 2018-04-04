package com.github.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

@Data
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class Dog {
    private final String id = UUID.randomUUID().toString();
    private String name;
    private LocalDate birthDate;
    private double weight;
    private double height;

    private Dog(DogBuilder builder) {
        name = builder.name;
        birthDate = builder.birthDate;
        weight = builder.weight;
        height = builder.height;
    }

    public static class DogBuilder {
        private String name;
        private LocalDate birthDate;
        private double weight;
        private double height;

        public DogBuilder() {
        }

        public DogBuilder name(@Size(min = 1, max = 100, message = "Dog's name must be between 1 and 100 characters") String name) {
            this.name = name;
            return this;
        }

        public DogBuilder birthDate(@Past @NotNull LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public DogBuilder weight(@NotNull @Min(1) double weight) {
            this.weight = weight;
            return this;
        }

        public DogBuilder height(@NotNull @Min(1) double height) {
            this.height = height;
            return this;
        }

        public Dog build() {
            return new Dog(this);
        }
    }
}
