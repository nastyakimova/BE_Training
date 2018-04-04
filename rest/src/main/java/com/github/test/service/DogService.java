package com.github.test.service;

import com.github.test.model.Dog;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DogService {

    List<Dog> findAllDogs();

    Dog findById(String dogId);

    Dog saveDog(Dog dog);

    Dog updateDog(String dogId,Dog dog);

    boolean deleteDog(String dogId);
}
