package se.lexicon;

import se.lexicon.dao.CityDao;
import se.lexicon.dao.CityDaoImpl;
import se.lexicon.model.City;

import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {

        System.out.println("DB_URL: " + System.getenv("DB_URL"));
        System.out.println("DB_USER: " + System.getenv("DB_USER"));
        System.out.println("DB_PASSWORD: " + System.getenv("DB_PASSWORD"));
        System.out.println("DB_DRIVER: " + System.getenv("DB_DRIVER"));
        
        CityDao cityDao = new CityDaoImpl();

        //Create (save) a new city
        City newCity = new City(0, "Testgränd", "SWE", "Testlän", 12345);
        City savedCity = cityDao.save(newCity);
        System.out.println("Saved City: " + savedCity);

        //Read (findById)
        if (savedCity != null) {
            Optional<City> foundCity = cityDao.findById(savedCity.getId());
            System.out.println("Found City by ID: " + foundCity.orElse(null));
        }

        //Read (findByName)
        List<City> citiesByName = cityDao.findByName("Testville");
        System.out.println("Cities found by name: " + citiesByName);

        //List (find) all cities
        List<City> allCities = cityDao.findAll();
        System.out.println("Total cities in database: " + allCities.size());
    }
}