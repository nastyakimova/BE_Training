package com.github.test;

import com.github.test.model.Dog;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

import static io.qala.datagen.RandomShortApi.alphanumeric;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class DogApiValidationTest {
    private Validator validator;

    private final String VALID_NAME = "Bobby";
    private final LocalDate VALID_DATE = LocalDate.now().minus(Period.ofDays(30));
    private final double VALID_WEIGHT = 10.0;
    private final double VALID_HEIGHT = 10.0;

    @BeforeClass
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldCheckViolatedEmptyName() {
        Dog emptyNameDog = new Dog(null, VALID_DATE, VALID_WEIGHT, VALID_HEIGHT);
        Set<ConstraintViolation<Dog>> violations = validator.validate(emptyNameDog);
        assertThat(violations, hasSize(1));
        assertThat(violations.iterator().next().getMessage(), is("must not be null"));
    }

    @Test
    public void shouldCheckViolatedNameMoreThan100Symbols() {
        Dog longNameDog = new Dog(alphanumeric(101), VALID_DATE, VALID_WEIGHT, VALID_HEIGHT);
        Set<ConstraintViolation<Dog>> violations = validator.validate(longNameDog);
        assertThat(violations, hasSize(1));
        assertThat(violations.iterator().next().getMessage(), is("Dog's name must be between 1 and 100 characters"));
    }

    @Test
    public void shouldCheckBirthDateViolation() {
        Dog futureBirthdateDog = new Dog(VALID_NAME, LocalDate.now(), VALID_WEIGHT, VALID_HEIGHT);
        Set<ConstraintViolation<Dog>> violations = validator.validate(futureBirthdateDog);
        assertThat(violations, hasSize(1));
        assertThat(violations.iterator().next().getMessage(), is("Date of birth can't be in future or now"));
    }

    @Test
    public void shouldCheckWeightViolation() {
        Dog weightlessDog = new Dog(VALID_NAME, VALID_DATE, 0, VALID_HEIGHT);
        Set<ConstraintViolation<Dog>> violations = validator.validate(weightlessDog);
        assertThat(violations, hasSize(1));
        assertThat(violations.iterator().next().getMessage(), is("Dog's weight must be greater than 0"));
    }

    @Test
    public void shouldCheckHeightViolation() {
        Dog zeroHeightDog = new Dog(VALID_NAME, VALID_DATE, VALID_WEIGHT, 0);
        Set<ConstraintViolation<Dog>> violations = validator.validate(zeroHeightDog);
        assertThat(violations, hasSize(1));
        assertThat(violations.iterator().next().getMessage(), is("Dog's height must be greater than 0"));
    }
}
