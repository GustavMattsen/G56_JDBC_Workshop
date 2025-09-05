package se.lexicon.dao;

import se.lexicon.model.City;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents the implementation of CityDao for interacting with the 'city' table in the database.
 */
public class CityDaoImpl implements CityDao {

    //Database credentials from environment variables
    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");
    private static final String DRIVER = System.getenv("DB_DRIVER");

    //Load the driver dynamically when class is loaded
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("❌ MySQL JDBC Driver not found!", e);
        }
    }

    //Helper method to get a connection
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Override
    public Optional<City> findById(int id) {
        String sql = "SELECT ID, Name, CountryCode, District, Population FROM city WHERE ID = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    City city = new City(
                            rs.getInt("ID"),
                            rs.getString("Name"),
                            rs.getString("CountryCode"),
                            rs.getString("District"),
                            rs.getInt("Population")
                    );
                    return Optional.of(city);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); //log the error (or replace with logger)
        }
        return Optional.empty();
    }

    @Override
    public List<City> findByCode(String code) {
        String sql = "SELECT ID, Name, CountryCode, District, Population FROM city WHERE CountryCode = ?";
        List<City> cities = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cities.add(new City(
                            rs.getInt("ID"),
                            rs.getString("Name"),
                            rs.getString("CountryCode"),
                            rs.getString("District"),
                            rs.getInt("Population")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }

    @Override
    public List<City> findByName(String name) {
        String sql = "SELECT ID, Name, CountryCode, District, Population FROM city WHERE Name LIKE ?";
        List<City> cities = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cities.add(new City(
                            rs.getInt("ID"),
                            rs.getString("Name"),
                            rs.getString("CountryCode"),
                            rs.getString("District"),
                            rs.getInt("Population")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }

    @Override
    public List<City> findAll() {
        String sql = "SELECT ID, Name, CountryCode, District, Population FROM city";
        List<City> cities = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                City city = new City(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getString("CountryCode"),
                        rs.getString("District"),
                        rs.getInt("Population")
                );
                cities.add(city);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }

    @Override
    public City save(City city) {
        String sql = "INSERT INTO city (Name, CountryCode, District, Population) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, city.getName());
            stmt.setString(2, city.getCountryCode());
            stmt.setString(3, city.getDistrict());
            stmt.setInt(4, city.getPopulation());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Insert failed, no rows affected.");
                return null;
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    return new City(newId, city.getName(), city.getCountryCode(), city.getDistrict(), city.getPopulation());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(City city) {

    }

    @Override
    public void deleteById(int id) {

    }
}
