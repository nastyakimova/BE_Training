package com.github.test.service;

import com.github.test.dao.DogDao;
import com.github.test.model.Dog;

import java.util.List;

public class DogService {
    private DogDao dogDao;

    public DogService(DogDao dogDao) {
        this.dogDao = dogDao;
    }

    public List<Dog> findAllDogs() {
        return dogDao.getAllDogs();
    }

    public Dog findById(String dogId) {
        return dogDao.getDogById(dogId);
    }

    public Dog saveDog(Dog dog) {
        return dogDao.createDog(dog);
    }

    public Dog updateDog(String dogId, Dog dog) {
        dog.setId(dogId);
        return dogDao.updateDog(dog);
    }

    public boolean deleteDog(String dogId) {
        return dogDao.deleteDog(dogId);
    }

}
