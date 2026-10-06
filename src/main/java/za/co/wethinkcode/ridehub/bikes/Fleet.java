package za.co.wethinkcode.ridehub.bikes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fleet {

    private final List<Bike> bikes = new ArrayList<>();

    public void add(Bike bike) {
        bikes.add(bike);
    }

    public List<Bike> getBikes() {
        return Collections.unmodifiableList(bikes);
    }

    public double totalCostFor(int minutes) {
        double total = 0;
        for (Bike bike : bikes) {
            total += bike.tripCost(minutes);
        }
        return total;
    }
}
