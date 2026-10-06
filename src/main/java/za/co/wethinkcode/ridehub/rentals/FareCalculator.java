package za.co.wethinkcode.ridehub.rentals;

public class FareCalculator {

    public double calculateFare(String bikeType, int minutes, boolean isMember) {
        double fare;
        if (bikeType.equals("STANDARD")) {
            fare = 5.0 + minutes * 0.75;
            if (isMember) {
                fare = fare - fare * 0.10;
            }
        } else if (bikeType.equals("ELECTRIC")) {
            fare = 10.0 + minutes * 1.5;
            if (isMember) {
                fare = fare - fare * 0.10;
            }
        } else if (bikeType.equals("CARGO")) {
            fare = 8.0 + minutes * 1.25;
            if (isMember) {
                fare = fare - fare * 0.10;
            }
        } else {
            throw new IllegalArgumentException("Unknown bike type: " + bikeType);
        }
        return fare;
    }
}
