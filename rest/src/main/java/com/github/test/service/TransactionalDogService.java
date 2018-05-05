package com.github.test.service;

import com.github.test.dao.JdbcConnectionHolder;
import com.github.test.model.Dog;

import javax.annotation.PostConstruct;
import java.util.List;

public class TransactionalDogService {
    private DogService dogService;
    private JdbcConnectionHolder connectionHolder;

    public TransactionalDogService(DogService dogService, JdbcConnectionHolder connectionHolder) {
        this.dogService = dogService;
        this.connectionHolder = connectionHolder;
    }

    @PostConstruct
    public void shutdown() {
        connectionHolder.close();
    }

    public List<Dog> findAllDogs() {
        List<Dog> dogs;
        try {
            dogs = dogService.findAllDogs();
            connectionHolder.commit();
        } catch (Throwable e) {
            connectionHolder.rollback();
            throw new RuntimeException();
        }
        return dogs;
    }

    public Dog findById(String dogId) {
        Dog dog;
        try {
            dog = dogService.findById(dogId);
            connectionHolder.commit();
        } catch (Throwable e) {
            connectionHolder.rollback();
            throw new RuntimeException();
        }
        return dog;
    }

    public Dog saveDog(Dog dog) {
        Dog createdDog;
        try {
            createdDog = dogService.saveDog(dog);
            connectionHolder.commit();
        } catch (Throwable e) {
            connectionHolder.rollback();
            throw new RuntimeException();
        }
        return createdDog;
    }

    public Dog updateDog(String dogId, Dog dog) {
        Dog updatedDog;
        try {
            updatedDog = dogService.updateDog(dogId, dog);
            connectionHolder.commit();
        } catch (Throwable e) {
            connectionHolder.rollback();
            throw new RuntimeException();
        }
        return updatedDog;
    }

    public boolean deleteDog(String dogId) {
        boolean isDeleted;
        try {
            isDeleted = dogService.deleteDog(dogId);
            connectionHolder.commit();
        } catch (Throwable e) {
            connectionHolder.rollback();
            throw new RuntimeException();
        }
        return isDeleted;
    }
}
