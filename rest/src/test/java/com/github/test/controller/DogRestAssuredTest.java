package com.github.test.controller;

import com.github.test.model.Dog;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.github.test.TestUtils.createInvalidDog;
import static com.github.test.TestUtils.createRandomDog;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class DogRestAssuredTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.basePath = "/dog";
    }

    @Test
    public void shouldGetDogByIdTest() {
        final Dog dog = given().when().get().jsonPath().getObject("[1]", Dog.class);
        given().accept(ContentType.JSON).when().get("/" + dog.getId()).then().statusCode(HttpStatus.OK.value());
    }

    @Test
    void shouldGetAllDogsTest() {
        given().accept(ContentType.JSON).when().get().then().statusCode(HttpStatus.OK.value())
                .assertThat().body("id", hasSize(3));
    }

    @Test
    void shouldCreateDogTest() {
        Dog randomDog = createRandomDog();
        Response response = given().contentType(ContentType.JSON).body(randomDog).post();
        response.then().statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);
        assertReflectionEquals(response.as(Dog.class), randomDog);
    }

    @Test
    void shouldUpdateDogTest() {
        Dog currentDog = given().when().get().jsonPath().getObject("[1]", Dog.class);
        currentDog.setName("Scooby");
        Response response = given().contentType(ContentType.JSON).body(currentDog).put("/" + currentDog.getId());
        response.then().statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);
        assertReflectionEquals(response.as(Dog.class), currentDog);
    }


    @Test
    void shouldDeleteExistingDog() {
        Dog currentDog = given().when().get("/").jsonPath().getObject("[1]", Dog.class);
        given().delete("/" + currentDog.getId()).then().statusCode(HttpStatus.NO_CONTENT.value());
        given().delete("/" + currentDog.getId()).then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldFailOnValidation() {
        Dog invalidDog = createInvalidDog();
        given().contentType(ContentType.JSON).body(invalidDog).post()
                .then().statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
