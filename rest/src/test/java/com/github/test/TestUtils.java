package com.github.test;

import com.github.test.model.Dog;

import java.time.LocalDate;

import static io.qala.datagen.RandomDate.between;
import static io.qala.datagen.RandomDate.daysAgo;
import static io.qala.datagen.RandomDate.yearsAgo;
import static io.qala.datagen.RandomShortApi.alphanumeric;
import static io.qala.datagen.RandomShortApi.nullOr;
import static io.qala.datagen.RandomShortApi.positiveDouble;

public class TestUtils {
    public static Dog createRandomDog() {
        Dog dog = new Dog();
        dog.setName(alphanumeric(1, 100));
        dog.setBirthDate(nullOr(between(yearsAgo(20), daysAgo(1)).localDate()));
        dog.setWeight(positiveDouble());
        dog.setHeight(positiveDouble());
        return dog;
    }

    public static Dog createInvalidDog() {
        Dog dog = new Dog();
        dog.setName(alphanumeric(101));
        dog.setBirthDate( LocalDate.now().plusDays(10));
        dog.setWeight(0);
        dog.setHeight(0);
        return dog;
    }
}
