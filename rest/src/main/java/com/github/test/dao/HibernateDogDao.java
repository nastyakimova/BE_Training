package com.github.test.dao;

import com.github.test.model.Dog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class HibernateDogDao implements DogDao {
    private SessionFactory sessionFactory;

    public HibernateDogDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Dog createDog(Dog dog) {
        getCurrentSession().persist(dog);
        return dog;
    }

    @Override
    public Dog getDogById(String id) {
        return getCurrentSession().get(Dog.class, id);
    }

    @Override
    public List<Dog> getAllDogs() {
        return getCurrentSession().createQuery("from Dog", Dog.class).list();
    }

    @Override
    public Dog updateDog(Dog dog) {
        if (getDogById(dog.getId()) == null) {
            return null;
        }
        getCurrentSession().merge(dog);
        return dog;
    }

    @Override
    public boolean deleteDog(String id) {
        final Dog dog = getDogById(id);
        if (dog == null) {
            return false;
        }
        getCurrentSession().delete(dog);
        return true;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
