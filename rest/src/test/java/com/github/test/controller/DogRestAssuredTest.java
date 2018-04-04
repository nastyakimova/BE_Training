package com.github.test.controller;

import com.github.test.dao.DogDao;
import com.github.test.model.Dog;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class DogRestAssuredTest {
    @Autowired
    private DogDao dogDao;

    private static final String URL = "http://localhost:8080/dog";

    @BeforeMethod
    private void init() {
        Dog dogTom = new Dog.DogBuilder().name("Tom")
                .birthDate(LocalDate.of(2010, 10, 25))
                .weight(10.1)
                .height(12.5).build();
        Dog dogSam = new Dog.DogBuilder().name("Sam")
                .birthDate(LocalDate.of(2011, 11, 5))
                .weight(12.1)
                .height(12.3).build();
        Dog dogJack = new Dog.DogBuilder().name("Jack")
                .birthDate(LocalDate.of(2009, 9, 15))
                .weight(9.1)
                .height(10.5).build();
        dogDao.createDog(dogTom);
        dogDao.createDog(dogSam);
        dogDao.createDog(dogJack);
    }

    private Dog newDog = new Dog.DogBuilder().name("Scooby").height(10.2)
            .weight(153).birthDate(LocalDate.of(2005, 12, 25))
            .build();
    private Dog currentDog = given().when().get(URL).jsonPath().getObject("[1]", Dog.class);

    @Test
    public void shouldGetDogByIdTest() {
        Dog dog = given().when().get(URL).jsonPath().getObject("[1]", Dog.class);
        given().accept(ContentType.JSON).when().get(URL + "/" + dog.getId()).then().statusCode(HttpStatus.OK.value());
    }

    @Test
    void shouldGetAllDogsTest() {
        given().accept(ContentType.JSON).when().get(URL).then().statusCode(HttpStatus.OK.value())
                .assertThat().body("id", hasSize(3));
    }

    @Test
    void shouldCreateNewDogTest() {
        given().body(newDog).accept(ContentType.JSON).contentType(ContentType.JSON)
                .post(URL).then().statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void shouldUpdateDogTest() {
        currentDog.setName("Scooby");
        given().contentType(ContentType.JSON).body(currentDog).put(URL + "/" + currentDog.getId()).then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("name", equalTo("Scooby"));
    }

    @Test
    void shouldDeleteExistingDog() {
        given().delete(URL + "/" + currentDog.getId()).then().statusCode(HttpStatus.NO_CONTENT.value());
        given().delete(URL + "/" + currentDog.getId()).then().statusCode(HttpStatus.NOT_FOUND.value());
    }
}
