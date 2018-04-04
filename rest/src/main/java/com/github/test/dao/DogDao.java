package com.github.test.dao;

import com.github.test.model.Dog;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DogDao {
    public static Map<String, Dog> DOGS = new ConcurrentHashMap<>();

    public Dog createDog(Dog dog) {
        return DOGS.put(dog.getId(), dog);
    }

    public Dog getDog(String id) {
        return DOGS.get(id);
    }

    public List<Dog> getAllDogs() {
        return new ArrayList<>(DOGS.values());
    }

    public Dog updateDog(String id, Dog dog) {
        return DOGS.put(id, dog);
    }

    public boolean deleteDog(String id) {
        return DOGS.remove(id) == null;
    }
}
