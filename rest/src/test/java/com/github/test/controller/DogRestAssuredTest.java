package com.github.test.controller;

import com.github.test.dao.DogDao;
import com.github.test.model.Dog;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class DogRestAssuredTest {
    private DogDao dogDao;

    private Dog currentDog;

    @BeforeMethod
    private void init() {
        RestAssured.basePath = "http://localhost:8080/dog";
        Dog dogTom = new Dog("Tom", LocalDate.of(2010, 5, 3), 20.1, 15.4);
        Dog dogSam = new Dog("Sam", LocalDate.of(2009, 5, 3), 20.1, 15.4);
        Dog dogJack = new Dog("Jack", LocalDate.of(2011, 5, 3), 20.1, 15.4);
        currentDog = given().when().get().jsonPath().getObject("[1]", Dog.class);
        dogDao.createDog(dogTom);
        dogDao.createDog(dogSam);
        dogDao.createDog(dogJack);
    }

    @Test
    public void shouldGetDogByIdTest() {
        Dog dog = given().when().get().jsonPath().getObject("[1]", Dog.class);
        given().accept(ContentType.JSON).when().get( "/" + dog.getId()).then().statusCode(HttpStatus.OK.value());
    }

    @Test
    void shouldGetAllDogsTest() {
        given().accept(ContentType.JSON).when().get().then().statusCode(HttpStatus.OK.value())
                .assertThat().body("id", hasSize(3));
    }

    /*@Test
    void shouldCreateNewDogTest() {
        given().body(newDog).accept(ContentType.JSON).contentType(ContentType.JSON)
                .post(URL).then().statusCode(HttpStatus.CREATED.value());
    }*/

    @Test
    void shouldUpdateDogTest() {
        currentDog.setName("Scooby");
        given().contentType(ContentType.JSON).body(currentDog).put(  "/" + currentDog.getId()).then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("name", equalTo("Scooby"));
    }

    @Test
    void shouldDeleteExistingDog() {
        given().delete(  "/" + currentDog.getId()).then().statusCode(HttpStatus.NO_CONTENT.value());
        given().delete(  "/" + currentDog.getId()).then().statusCode(HttpStatus.NOT_FOUND.value());
    }
}
