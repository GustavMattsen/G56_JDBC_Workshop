package se.lexicon;

import se.lexicon.dao.CityDao;
import se.lexicon.dao.CityDaoImpl;
import se.lexicon.model.City;

import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        CityDao cityDao = new CityDaoImpl();

        //Create a new city
        City newCity = new City(0, "Testville", "SWE", "Test District", 12345);
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
    }
}