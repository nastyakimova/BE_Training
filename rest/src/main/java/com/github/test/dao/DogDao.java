package com.github.test.dao;

import com.github.test.model.Dog;

import java.util.List;

public interface DogDao {
    Dog createDog(Dog dog);

    Dog getDog(String id);

    List<Dog> getAllDogs();

    Dog updateDog(String id, Dog dog);

    boolean deleteDog(String id);
}
