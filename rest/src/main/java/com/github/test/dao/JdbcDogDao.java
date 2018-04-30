package com.github.test.dao;

import com.github.test.model.Dog;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcDogDao implements DogDao {
    private ConnectionHolder connectionHolder;

    public JdbcDogDao(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    @Override
    public Dog createDog(Dog dog) {
        int rowsCreated;
        String insertQuery = "INSERT INTO DOGS(id, name, birthdate, weight, height) VALUES(?,?,?,?,?)";
        try (Connection connection = connectionHolder.getConnection();
             PreparedStatement ps = connection.prepareStatement(insertQuery)) {
            ps.setString(1, dog.getId());
            ps.setString(2, dog.getName());
            ps.setObject(3, dog.getBirthDate());
            ps.setDouble(4, dog.getWeight());
            ps.setDouble(5, dog.getHeight());
            rowsCreated = ps.executeUpdate();
            connectionHolder.commit();
        } catch (SQLException e) {
            connectionHolder.rollback();
            throw new RuntimeException("An error occured while creating dog: " + e.getMessage());
        } finally {
            connectionHolder.close();
        }
        return rowsCreated == 1 ? dog : null;
    }

    @Override
    public Dog getDogById(String id) {
        Dog dog;
        String selectRowQuery = "select * from DOGS where id=?";
        try (Connection connection = connectionHolder.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectRowQuery)) {
            ps.setMaxRows(1);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            dog = (rs.next()) ? initDogFromResultSet(rs) : null;
            connectionHolder.commit();
        } catch (SQLException e) {
            connectionHolder.rollback();
            throw new RuntimeException("An error occured while getting dog by id: " + e.getMessage());
        } finally {
            connectionHolder.close();
        }
        return dog;
    }

    @Override
    public List<Dog> getAllDogs() {
        List<Dog> dogs = new ArrayList<>();
        String selectAllQuery = "select * from DOGS";
        try (Connection connection = connectionHolder.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectAllQuery);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                dogs.add(initDogFromResultSet(rs));
            }
            connectionHolder.commit();
        } catch (SQLException ex) {
            connectionHolder.rollback();
            throw new RuntimeException("An error occured while getting list of all dogs: " + ex.getMessage());
        } finally {
            connectionHolder.close();
        }
        return dogs;
    }

    @Override
    public Dog updateDog(Dog dog) {
        int rowsUpdated;
        String updateQuery = "UPDATE DOGS SET name = ?, birthdate= ?, weight= ?, height= ? "
                + "WHERE ID = ?;";
        try (Connection connection = connectionHolder.getConnection();
             PreparedStatement ps = connection.prepareStatement(updateQuery)) {
            ps.setString(1, dog.getName());
            ps.setObject(2, dog.getBirthDate());
            ps.setDouble(3, dog.getWeight());
            ps.setDouble(4, dog.getHeight());
            ps.setString(5, dog.getId());
            rowsUpdated = ps.executeUpdate();
            connectionHolder.commit();
        } catch (SQLException e) {
            connectionHolder.rollback();
            throw new RuntimeException("An error occured while updating dog: " + e.getMessage());
        } finally {
            connectionHolder.close();
        }
        return rowsUpdated == 1 ? dog : null;
    }

    @Override
    public boolean deleteDog(String id) {
        int rowsDeleted;
        String deleteQuery = "delete from DOGS where id= ?";
        try (Connection connection = connectionHolder.getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteQuery)) {
            ps.setString(1, id);
            rowsDeleted = ps.executeUpdate();
            connectionHolder.commit();
        } catch (SQLException e) {
            connectionHolder.rollback();
            throw new RuntimeException("An error occured while deleting dog: " + e.getMessage());
        } finally {
            connectionHolder.close();
        }
        return rowsDeleted > 0;
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

