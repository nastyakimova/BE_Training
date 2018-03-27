package com.github.training.spring.ioc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String firstName;
    private String lastName;
    private Integer age;

    @Override
    public String toString() {
        return "User: " + getFirstName() + " " + getLastName() + ", " + getAge();
    }
}
