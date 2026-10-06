package za.co.wethinkcode.ridehub;

import za.co.wethinkcode.ridehub.bikes.ElectricBike;
import za.co.wethinkcode.ridehub.bikes.Fleet;
import za.co.wethinkcode.ridehub.bikes.StandardBike;
import za.co.wethinkcode.ridehub.web.BikeRepository;
import za.co.wethinkcode.ridehub.web.RideHubApp;

public class Main {

    public static void main(String[] args) {
        Fleet fleet = new Fleet();
        fleet.add(new StandardBike("SB-001"));
        fleet.add(new ElectricBike("EB-001", 80));

        fleet.getBikes().forEach(System.out::println);
        System.out.println("A 10 minute trip on every bike would cost " + fleet.totalCostFor(10));

        if (args.length > 0 && args[0].equals("--serve")) {
            new RideHubApp(new BikeRepository()).create().start(7070);
            System.out.println("RideHub is running on http://localhost:7070");
        }
    }
}
