package com.github.test.controller;

import com.github.test.model.Dog;
import com.github.test.service.DogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController("/")
public class DogController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DogService dogService;

    @GetMapping(value = "/dog/{id}")
    public ResponseEntity<Dog> getDog(@PathVariable("id") String dogId) {
        logger.info("Getting dog with id " + dogId);
        Dog dog = dogService.findById(dogId);
        if (dog == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dog, HttpStatus.OK);
    }

    @GetMapping(value = "/dog")
    public ResponseEntity<List<Dog>> getAllDogs() {
        logger.info("Getting all dogs");
        return new ResponseEntity<>(dogService.findAllDogs(), HttpStatus.OK);
    }

    @RequestMapping(value = "/dog", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dog> createDog(@Valid @RequestBody Dog dog) {
        logger.info("Creating new dog: " + dog.toString());
        if (dogService.findById(dog.getId()) == null) {
            logger.info("Dog " + dog.toString() + " already exist");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        dogService.saveDog(dog);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(path = "/dog/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dog> updateDog(@PathVariable("id") String dogId, @Valid @RequestBody Dog dog) {
        logger.info("Updating dog with id " + dogId);
        Dog currentDog = dogService.findById(dogId);
        if (currentDog == null) {
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
