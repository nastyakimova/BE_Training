package com.github.test.controller;

import com.github.test.model.Dog;
import com.github.test.service.DogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
public class DogController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @PostConstruct
    private void init() {
        for (Dog dog : getDogs()) {
            dogService.saveDog(dog);
        }
    }

    private List<Dog> getDogs() {
        Dog dogTom = new Dog("Tom", LocalDate.of(2010, 5, 3), 20.1, 15.4);
        Dog dogSam = new Dog("Sam", LocalDate.of(2009, 5, 3), 20.1, 15.4);
        Dog dogJack = new Dog("Jack", LocalDate.of(2011, 5, 3), 20.1, 15.4);
        return Arrays.asList(dogTom, dogSam, dogJack);
    }


    @GetMapping("/dog/{id}")
    public ResponseEntity<Dog> getDog(@PathVariable("id") String dogId) {
        logger.info("Getting dog with id " + dogId);
        Dog dog = dogService.findById(dogId);
        if (dog == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dog, HttpStatus.OK);
    }

    @GetMapping("/dog")
    public ResponseEntity<List<Dog>> getAllDogs() {
        logger.info("Getting all dogs");
        return new ResponseEntity<>(dogService.findAllDogs(), HttpStatus.OK);
    }

    @PostMapping(path = "/dog",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dog> createDog(final @Valid @RequestBody Dog dog) {
        logger.info("Creating new dog: " + dog.toString());
        if (dogService.findById(dog.getId()) != null) {
            logger.info("Dog " + dog.toString() + " already exist");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(dogService.saveDog(dog), HttpStatus.CREATED);
    }

    @PutMapping(path = "/dog/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dog> updateDog(@PathVariable("id") String dogId, final @Valid @RequestBody Dog dog) {
        logger.info("Updating dog with id " + dogId);
        if (dogService.findById(dogId) == null) {
            logger.info("Dog with id " + dogId + " doesn't exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dogService.updateDog(dogId, dog), HttpStatus.OK);
    }

    @DeleteMapping("/dog/{id}")
    public ResponseEntity<Dog> deleteDog(@PathVariable("id") String dogId) {
        logger.info("Deleting dog with id " + dogId);
        Dog currentDog = dogService.findById(dogId);
        if (currentDog == null) {
            logger.info("Dog with id " + dogId + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        dogService.deleteDog(dogId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
