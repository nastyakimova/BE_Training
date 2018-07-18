package com.github.test.service;

import com.github.test.dao.DogDao;
import com.github.test.model.Dog;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class DogService {
    private DogDao dogDao;

    public DogService(DogDao dogDao) {
        this.dogDao = dogDao;
    }

    @Transactional
    public List<Dog> findAllDogs() {
        return dogDao.getAllDogs();
    }

    @Transactional
    public Dog findById(String dogId) {
        return dogDao.getDogById(dogId);
    }

    @Transactional
    public Dog saveDog(Dog dog) {
        return dogDao.createDog(dog);
    }

    @Transactional
    public Dog updateDog(String dogId, Dog dog) {
        dog.setId(dogId);
        return dogDao.updateDog(dog);
    }

    @Transactional
    public boolean deleteDog(String dogId) {
        return dogDao.deleteDog(dogId);
    }

}
