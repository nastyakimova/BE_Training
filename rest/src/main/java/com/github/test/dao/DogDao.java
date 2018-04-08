package com.github.test.dao;

import com.github.test.model.Dog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DogDao {
    private static Map<String, Dog> DOGS = new ConcurrentHashMap<>();

    public Dog createDog(Dog dog) {
        DOGS.put(dog.getId(), dog);
        return dog;
    }

    public Dog getDog(String id) {
        return DOGS.get(id);
    }

    public List<Dog> getAllDogs() {
        return new ArrayList<>(DOGS.values());
    }

    public Dog updateDog(Dog dog) {
        DOGS.put(dog.getId(), dog);
        return dog;
    }

    public boolean deleteDog(String id) {
        return DOGS.remove(id) == null;
    }
}
