package com.github.test.dao;

import com.github.test.model.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.List;

import static com.github.test.TestUtils.createInvalidDog;
import static com.github.test.TestUtils.createRandomDog;
import static io.qala.datagen.RandomShortApi.alphanumeric;
import static org.testng.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@Test
@ContextConfiguration("classpath:/application-context.xml")
@ActiveProfiles("h2")
public class JdbcDogDaoTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private DogDao dogDao;

    @Test
    public void shouldGetTheSameDogAsWasSaved() {
        Dog dog = createRandomDog();
        dogDao.createDog(dog);
        Dog createdDog = dogDao.getDogById(dog.getId());
        assertReflectionEquals(createdDog, dog);
    }

    @Test
    public void shouldFindDogByIdCorrectly() {
        Dog dog = createRandomDog();
        dogDao.createDog(dog);
        List<Dog> allDogs = dogDao.getAllDogs();
        assertTrue(allDogs.contains(dog));
    }

    @Test
    public void shouldCheckConstraintAreValid() {
        Dog dog = createRandomDog();
        dog.setName(alphanumeric(100));
        Dog createdDog = dogDao.createDog(dog);
        assertReflectionEquals(createdDog, dog);
        assertTrue(dogDao.getAllDogs().contains(dog));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void shouldFailOnCreatingNotValidDog() {
        Dog invalidDog = createInvalidDog();
        dogDao.createDog(invalidDog);
    }

    @Test
    public void shouldFailOnSqlInjection() {
        Dog dog = createRandomDog();
        String sqlInjection = "\"' blah";
        dog.setName(sqlInjection);
        dog.setId(sqlInjection);
        assertReflectionEquals(dogDao.createDog(dog), dog);
    }

    @Test
    public void shouldCheckDogIsUpdatedCorrectly() {
        Dog dog = createRandomDog();
        dogDao.createDog(dog);
        Dog updatedDog = createRandomDog();
        updatedDog.setId(dog.getId());
        assertReflectionEquals(dogDao.updateDog(updatedDog), updatedDog);
        assertTrue(dogDao.getAllDogs().contains(updatedDog));
    }

    @Test
    public void shouldGetNothingOnUpdatingNotCreatedDog() {
        Dog dog = createRandomDog();
        assertEquals(dogDao.updateDog(dog), null);
        assertFalse(dogDao.getAllDogs().contains(dog));
    }

    @Test
    public void shouldGetTrueOnSuccessfulDeletion() {
        Dog dog = createRandomDog();
        dogDao.createDog(dog);
        assertTrue(dogDao.deleteDog(dog.getId()));
        assertFalse(dogDao.getAllDogs().contains(dog));
    }

    @Test
    public void shouldGetFalseOnNotCreatedDogDeletion() {
        Dog dog = createRandomDog();
        assertFalse(dogDao.getAllDogs().contains(dog));
        assertFalse(dogDao.deleteDog(dog.getId()));
    }

}
