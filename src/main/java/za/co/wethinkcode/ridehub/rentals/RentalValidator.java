package za.co.wethinkcode.ridehub.rentals;

import za.co.wethinkcode.ridehub.bikes.Bike;

public class RentalValidator {

    private static final double MINIMUM_CREDIT = 20.0;

    public String checkEligibility(Rider rider, Bike bike) {
        String result;
        if (rider != null) {
            if (rider.isVerified()) {
                if (!rider.isBanned()) {
                    if (bike != null) {
                        if (bike.isAvailable()) {
                            if (rider.getCreditBalance() >= MINIMUM_CREDIT) {
                                result = "OK";
                            } else {
                                result = "INSUFFICIENT_CREDIT";
                            }
                        } else {
                            result = "BIKE_UNAVAILABLE";
                        }
                    } else {
                        result = "NO_BIKE";
                    }
                } else {
                    result = "RIDER_BANNED";
                }
            } else {
                result = "RIDER_NOT_VERIFIED";
            }
        } else {
            result = "NO_RIDER";
        }
        return result;
    }
}
