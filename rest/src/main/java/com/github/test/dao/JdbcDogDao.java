package com.github.test.dao;

import com.github.test.model.Dog;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class JdbcDogDao implements DogDao {
    private DataSource dataSource;

    public JdbcDogDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Dog createDog(Dog dog) {

        return null;
    }

    @Override
    public Dog getDog(String id) {
        return null;
    }

    @Override
    public List<Dog> getAllDogs() {
        List<Dog> dogs = new ArrayList<>();
        Connection connection = getDBConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from DOGS");
            while (rs.next()) {
                dogs.add(initDogFromResultSet(rs));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return dogs;
    }

    @Override
    public Dog updateDog(Dog dog) {
        return null;
    }

    @Override
    public boolean deleteDog(String id) {
        return false;
    }

    private Dog initDogFromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String name = rs.getString("name");
        LocalDate birthDate = rs.getDate("birthdate").toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        double weight = rs.getDouble("weight");
        double height = rs.getDouble("height");
        return new Dog(id, name, birthDate, weight, height);
    }

    private Connection getDBConnection() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}

