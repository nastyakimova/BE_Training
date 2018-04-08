package com.github.test.controller;

import com.github.test.model.Dog;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class DogRestAssuredTest {

    private final Dog newDog = new Dog("Rex", LocalDate.of(2001, 12, 1), 12.5, 14.1);

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    public void shouldGetDogByIdTest() {
        final Dog dog = given().when().get("/dog").jsonPath().getObject("[1]", Dog.class);
        given().accept(ContentType.JSON).when().get("/dog/" + dog.getId()).then().statusCode(HttpStatus.OK.value());
    }

    @Test
    void shouldGetAllDogsTest() {
        given().accept(ContentType.JSON).when().get("/dog").then().statusCode(HttpStatus.OK.value())
                .assertThat().body("id", hasSize(3));
    }

    @Test
    void shouldCreateDogTest() {
        Response response = given().contentType(ContentType.JSON).body(newDog).post("/dog");
        response.then().statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);
        assertReflectionEquals(response.as(Dog.class), newDog);
    }

    @Test
    void shouldUpdateDogTest() {
        Dog currentDog = given().when().get("/dog").jsonPath().getObject("[1]", Dog.class);
        currentDog.setName("Scooby");
        Response response = given().contentType(ContentType.JSON).body(currentDog).put("/dog/" + currentDog.getId());
        response.then().statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);
        assertReflectionEquals(response.as(Dog.class), currentDog);
    }


    @Test
    void shouldDeleteExistingDog() {
        Dog currentDog = given().when().get("/dog").jsonPath().getObject("[1]", Dog.class);
        given().delete("/dog/" + currentDog.getId()).then().statusCode(HttpStatus.NO_CONTENT.value());
        given().delete("/dog/" + currentDog.getId()).then().statusCode(HttpStatus.NOT_FOUND.value());
    }
}
