package com.github.test.controller;

import com.github.test.model.Dog;
import com.github.test.service.DogService;
import lombok.extern.slf4j.Slf4j;
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

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class DogController {
    private DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @GetMapping("/dog/{id}")
    public ResponseEntity<Dog> getDog(@PathVariable("id") String dogId) {
        log.info("Getting dog with id " + dogId);
        Dog dog = dogService.findById(dogId);
        if (dog == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dog, HttpStatus.OK);
    }

    @GetMapping("/dog")
    public ResponseEntity<List<Dog>> getAllDogs() {
        log.info("Getting all dogs");
        return new ResponseEntity<>(dogService.findAllDogs(), HttpStatus.OK);
    }

    @PostMapping(path = "/dog",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dog> createDog(final @Valid @RequestBody Dog dog) {
        log.info("Creating new dog: " + dog.toString());
        if (dogService.findById(dog.getId()) != null) {
            log.info("Dog " + dog.toString() + " already exist");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(dogService.saveDog(dog), HttpStatus.CREATED);
    }

    @PutMapping(path = "/dog/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dog> updateDog(@PathVariable("id") String dogId, final @Valid @RequestBody Dog dog) {
        log.info("Updating dog with id " + dogId);
        Dog updatedDog = dogService.updateDog(dogId, dog);
        if (updatedDog == null) {
            log.info("Dog with id " + dogId + " doesn't exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedDog, HttpStatus.OK);
    }

    @DeleteMapping("/dog/{id}")
    public ResponseEntity<Dog> deleteDog(@PathVariable("id") String dogId) {
        log.info("Deleting dog with id " + dogId);
        if (!dogService.deleteDog(dogId)) {
            log.info("Dog with id " + dogId + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }


    }
}
