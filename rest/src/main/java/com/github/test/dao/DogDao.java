package com.github.test.dao;

import com.github.test.model.Dog;

import java.util.List;

public interface DogDao {
    Dog createDog(Dog dog);

    Dog getDogById(String id);

    List<Dog> getAllDogs();

    Dog updateDog(Dog dog);

    boolean deleteDog(String id);
}
