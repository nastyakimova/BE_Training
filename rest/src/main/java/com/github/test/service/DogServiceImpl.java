package com.github.test.service;

import com.github.test.dao.DogDao;
import com.github.test.model.Dog;

import java.util.List;

public class DogServiceImpl implements DogService {
    private DogDao dogDao;

    public DogServiceImpl() {
    }

    public DogServiceImpl(DogDao dogDao) {
        this.dogDao = dogDao;
    }

    @Override
    public List<Dog> findAllDogs() {
        return dogDao.getAllDogs();
    }

    @Override
    public Dog findById(String dogId) {
        return dogDao.getDogById(dogId);
    }

    @Override
    public Dog saveDog(Dog dog) {
        return dogDao.createDog(dog);
    }

    @Override
    public Dog updateDog(String dogId, Dog dog) {
        dog.setId(dogId);
        return dogDao.updateDog(dog);
    }

    @Override
    public boolean deleteDog(String dogId) {
        return dogDao.deleteDog(dogId);
    }

}
