package za.co.wethinkcode.ridehub.web;

import za.co.wethinkcode.ridehub.bikes.Bike;
import za.co.wethinkcode.ridehub.bikes.ElectricBike;
import za.co.wethinkcode.ridehub.bikes.StandardBike;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BikeRepository {

    private final Map<String, Bike> bikes = new LinkedHashMap<>();

    public BikeRepository() {
        add(new StandardBike("SB-001"));

        Bike alreadyRented = new StandardBike("SB-002");
        alreadyRented.markUnavailable();
        add(alreadyRented);

        add(new ElectricBike("EB-001", 80));
    }

    private void add(Bike bike) {
        bikes.put(bike.getSerialNo(), bike);
    }

    public List<Bike> findAll() {
        return new ArrayList<>(bikes.values());
    }

    public Optional<Bike> findBySerialNo(String serialNo) {
        return Optional.ofNullable(bikes.get(serialNo));
    }
}
