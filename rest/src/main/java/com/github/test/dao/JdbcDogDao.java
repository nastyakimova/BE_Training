package com.github.test.dao;

import com.github.test.model.Dog;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcDogDao implements DogDao {
    private DataSource dataSource;

    public JdbcDogDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Dog createDog(Dog dog) {
        String insertQuery = "INSERT INTO DOGS(id, name, birthdate, weight, height) VALUES(?,?,?,?,?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(insertQuery)) {
            ps.setString(1, dog.getId());
            ps.setString(2, dog.getName());
            ps.setObject(3, dog.getBirthDate());
            ps.setDouble(4, dog.getWeight());
            ps.setDouble(5, dog.getHeight());
            return ps.executeUpdate() == 1 ? dog : null;
        } catch (SQLException e) {
            throw new RuntimeException("An error occured while creating dog: " + e.getMessage());
        }
    }

    @Override
    public Dog getDogById(String id) {
        String selectRowQuery = "select * from DOGS where id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectRowQuery)) {
            ps.setMaxRows(1);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            return (rs.next()) ? initDogFromResultSet(rs) : null;
        } catch (SQLException e) {
            throw new RuntimeException("An error occured while getting dog by id: " + e.getMessage());
        }

    }

    @Override
    public List<Dog> getAllDogs() {
        List<Dog> dogs = new ArrayList<>();
        String selectAllQuery = "select * from DOGS";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectAllQuery);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                dogs.add(initDogFromResultSet(rs));
            }
            return dogs;
        } catch (SQLException ex) {
            throw new RuntimeException("An error occured while getting list of all dogs: " + ex.getMessage());
        }
    }

    @Override
    public Dog updateDog(Dog dog) {
        String updateQuery = "UPDATE DOGS SET name = ?, birthdate= ?, weight= ?, height= ? "
                + "WHERE ID = ?;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(updateQuery)) {
            ps.setString(1, dog.getName());
            ps.setObject(2, dog.getBirthDate());
            ps.setDouble(3, dog.getWeight());
            ps.setDouble(4, dog.getHeight());
            ps.setString(5, dog.getId());

            return ps.executeUpdate() == 1 ? dog : null;
        } catch (SQLException e) {
            throw new RuntimeException("An error occured while updating dog: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteDog(String id) {
        String deleteQuery = "delete from DOGS where id= ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteQuery)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("An error occured while deleting dog: " + e.getMessage());
        }
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
}

