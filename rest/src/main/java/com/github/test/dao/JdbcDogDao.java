package com.github.test.dao;

import com.github.test.model.Dog;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcDogDao implements DogDao {
    private DataSource dataSource;

    public JdbcDogDao() {
    }

    public JdbcDogDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    private void init() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String createTableQuery = "create table DOGS (\n" +
                    " id varchar(64) NOT NULL,\n" +
                    " name varchar(100) NOT NULL,\n" +
                    " birthdate DATE,\n" +
                    " weight DOUBLE PRECISION NOT NULL,\n" +
                    " height DOUBLE PRECISION NOT NULL,\n" +
                    " PRIMARY KEY(id))";
            statement.execute(createTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dog createDog(Dog dog) {
        Dog createdDog = null;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String insertQuery = String.format("INSERT INTO DOGS (id, name, birthdate, weight, height) " +
                            "VALUES('%s', '%s', %s, %f, %f);", dog.getId(), dog.getName(),
                    getDateAsString(dog.getBirthDate()), dog.getWeight(), dog.getHeight());
            createdDog = statement.executeUpdate(insertQuery) == 1 ? dog : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return createdDog;
    }

    @Override
    public Dog getDogById(String id) {
        Dog dog = null;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.setMaxRows(1);
            String selectRowQuery = "select * from DOGS where id= '" + id + "'";
            ResultSet rs = statement.executeQuery(selectRowQuery);
            dog = (rs.next()) ? initDogFromResultSet(rs) : null;
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dog;
    }

    @Override
    public List<Dog> getAllDogs() {
        List<Dog> dogs = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String selectAllQuery = "select * from DOGS";
            ResultSet rs = statement.executeQuery(selectAllQuery);
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
        Dog updatedDog = null;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String updateQuery = String.format("UPDATE DOGS SET name = '%s', birthdate= %s, weight= %f, height= %f " +
                            "WHERE ID = '%s';", dog.getName(), getDateAsString(dog.getBirthDate()), dog.getWeight(),
                    dog.getHeight(), dog.getId());
            updatedDog = statement.executeUpdate(updateQuery) == 1 ? dog : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedDog;
    }

    @Override
    public boolean deleteDog(String id) {
        boolean result = false;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String deleteQuery = "delete from DOGS where id='" + id + "'";
            result = statement.execute(deleteQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Dog initDogFromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String name = rs.getString("name");
        Date date = rs.getDate("birthdate");
        LocalDate birthDate = (date != null) ? date.toLocalDate() : null;
        double weight = rs.getDouble("weight");
        double height = rs.getDouble("height");
        return new Dog(id, name, birthDate, weight, height);
    }

    private String getDateAsString(LocalDate date) {
        return date != null ? "'" + date + "'" : "NULL";
    }
}

