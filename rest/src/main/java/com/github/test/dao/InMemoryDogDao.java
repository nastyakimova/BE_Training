package com.github.test.dao;

import com.github.test.model.Dog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDogDao implements DogDao {
    private static Map<String, Dog> DOGS = new ConcurrentHashMap<>();

    @Override
    public Dog createDog(Dog dog) {
        DOGS.put(dog.getId(), dog);
        return dog;
    }

    @Override
    public Dog getDogById(String id) {
        return DOGS.get(id);
    }

    @Override
    public List<Dog> getAllDogs() {
        return new ArrayList<>(DOGS.values());
    }

    @Override
    public Dog updateDog(Dog dog) {
        DOGS.put(dog.getId(), dog);
        return dog;
    }

    @Override
    public boolean deleteDog(String id) {
        return DOGS.remove(id) == null;
    }
}
