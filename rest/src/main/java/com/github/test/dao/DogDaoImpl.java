package com.github.test.dao;

import com.github.test.model.Dog;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class DogDaoImpl implements DogDao {
    public static Map<String, Dog> DOGS = new ConcurrentHashMap<>();

    @Override
    public Dog createDog(Dog dog) {
        return DOGS.put(dog.getId(), dog);
    }

    @Override
    public Dog getDog(String id) {
        return DOGS.get(id);
    }

    @Override
    public List<Dog> getAllDogs() {
        return new ArrayList<>(DOGS.values());
    }

    @Override
    public Dog updateDog(String id, Dog dog) {
        return DOGS.put(id, dog);
    }

    @Override
    public boolean deleteDog(String id) {
        return DOGS.remove(id) == null;
    }
}
