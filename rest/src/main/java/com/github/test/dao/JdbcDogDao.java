package com.github.test.dao;

import com.github.test.model.Dog;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class JdbcDogDao implements DogDao {
    private JdbcTemplate jdbcTemplate;
    private RowMapper<Dog> rowMapper = new DogRowMapper();

    public JdbcDogDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Dog createDog(Dog dog) {
        String insertQuery = "INSERT INTO DOGS(id, name, birthdate, weight, height) VALUES(?,?,?,?,?)";
        int rowsCreated = jdbcTemplate.update(insertQuery, ps -> {
            ps.setString(1, dog.getId());
            ps.setString(2, dog.getName());
            ps.setObject(3, dog.getBirthDate());
            ps.setDouble(4, dog.getWeight());
            ps.setDouble(5, dog.getHeight());
        });
        return rowsCreated == 1 ? dog : null;
    }

    @Override
    public Dog getDogById(String id) {
        String selectRowQuery = "SELECT * FROM DOGS WHERE id=?";
        List<Dog> result = jdbcTemplate.query(selectRowQuery, ps -> ps.setString(1, id), rowMapper);
        return (!result.isEmpty()) ? result.get(0) : null;
    }

    @Override
    public List<Dog> getAllDogs() {
        String selectAllQuery = "SELECT * FROM DOGS";
        return jdbcTemplate.query(selectAllQuery, rowMapper);
    }

    @Override
    public Dog updateDog(Dog dog) {
        String updateQuery = "UPDATE DOGS SET name = ?, birthdate= ?, weight= ?, height= ? "
                + "WHERE ID = ?;";
        int rowsUpdated = jdbcTemplate.update(updateQuery, ps -> {
            ps.setString(1, dog.getName());
            ps.setObject(2, dog.getBirthDate());
            ps.setDouble(3, dog.getWeight());
            ps.setDouble(4, dog.getHeight());
            ps.setString(5, dog.getId());
        });
        return rowsUpdated == 1 ? dog : null;
    }

    @Override
    public boolean deleteDog(String id) {
        String deleteQuery = "DELETE FROM DOGS WHERE id= ?";
        int rowsDeleted = jdbcTemplate.update(deleteQuery, ps -> ps.setString(1, id));
        return rowsDeleted > 0;
    }

    private static class DogRowMapper implements RowMapper<Dog> {
        @Nullable
        @Override
        public Dog mapRow(ResultSet rs, int rowNum) throws SQLException {
            String id = rs.getString("id");
            String name = rs.getString("name");
            Date date = rs.getDate("birthdate");
            LocalDate birthDate = (date != null) ? date.toLocalDate() : null;
            double weight = rs.getDouble("weight");
            double height = rs.getDouble("height");
            return new Dog(id, name, birthDate, weight, height);
        }
    }
}

