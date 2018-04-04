package com.github.test.service;

import com.github.test.dao.DogDao;
import com.github.test.model.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DogServiceImpl implements DogService {
    @Autowired
    private DogDao dogDao;

    @Override
    public List<Dog> findAllDogs() {
        return dogDao.getAllDogs();
    }

    @Override
    public Dog findById(String dogId) {
        return dogDao.getDog(dogId);
    }

    @Override
    public Dog saveDog(Dog dog) {
        return dogDao.createDog(dog);
    }

    @Override
    public Dog updateDog(String dogId, Dog dog) {
        return dogDao.updateDog(dogId, dog);
    }

    @Override
    public boolean deleteDog(String dogId) {
        return dogDao.deleteDog(dogId);
    }

}
