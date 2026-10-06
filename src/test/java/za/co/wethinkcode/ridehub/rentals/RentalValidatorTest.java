package za.co.wethinkcode.ridehub.rentals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.ridehub.bikes.Bike;
import za.co.wethinkcode.ridehub.bikes.StandardBike;

import static org.assertj.core.api.Assertions.assertThat;

class RentalValidatorTest {

    private RentalValidator validator;
    private Rider goodRider;
    private Bike bike;

    @BeforeEach
    void setUp() {
        validator = new RentalValidator();
        goodRider = new Rider("Thandiwe Nkosi", true, false, 50.0);
        bike = new StandardBike("SB-001");
    }

    @Test
    void ok_whenEverythingIsInOrder() {
        assertThat(validator.checkEligibility(goodRider, bike)).isEqualTo("OK");
    }

    @Test
    void noRider_whenRiderIsNull() {
        assertThat(validator.checkEligibility(null, bike)).isEqualTo("NO_RIDER");
    }

    @Test
    void riderNotVerified_whenRiderIsNotVerified() {
        Rider unverified = new Rider("Sipho Dlamini", false, false, 50.0);
        assertThat(validator.checkEligibility(unverified, bike)).isEqualTo("RIDER_NOT_VERIFIED");
    }

    @Test
    void riderBanned_whenRiderIsBanned() {
        Rider banned = new Rider("Naledi Mokoena", true, true, 50.0);
        assertThat(validator.checkEligibility(banned, bike)).isEqualTo("RIDER_BANNED");
    }

    @Test
    void noBike_whenBikeIsNull() {
        assertThat(validator.checkEligibility(goodRider, null)).isEqualTo("NO_BIKE");
    }

    @Test
    void bikeUnavailable_whenBikeIsAlreadyRented() {
        bike.markUnavailable();
        assertThat(validator.checkEligibility(goodRider, bike)).isEqualTo("BIKE_UNAVAILABLE");
    }

    @Test
    void insufficientCredit_whenBalanceIsBelowTheMinimum() {
        Rider broke = new Rider("Kagiso Molefe", true, false, 19.99);
        assertThat(validator.checkEligibility(broke, bike)).isEqualTo("INSUFFICIENT_CREDIT");
    }

    @Test
    void ok_whenBalanceIsExactlyTheMinimum() {
        Rider exact = new Rider("Kagiso Molefe", true, false, 20.0);
        assertThat(validator.checkEligibility(exact, bike)).isEqualTo("OK");
    }

    @Test
    void verificationIsCheckedBeforeTheBan() {
        Rider both = new Rider("Lerato Khumalo", false, true, 50.0);
        assertThat(validator.checkEligibility(both, bike)).isEqualTo("RIDER_NOT_VERIFIED");
    }

    @Test
    void riderProblemsAreReportedBeforeBikeProblems() {
        Rider banned = new Rider("Naledi Mokoena", true, true, 50.0);
        assertThat(validator.checkEligibility(banned, null)).isEqualTo("RIDER_BANNED");
    }

    @Test
    void missingBikeIsReportedBeforeCredit() {
        Rider broke = new Rider("Kagiso Molefe", true, false, 0.0);
        assertThat(validator.checkEligibility(broke, null)).isEqualTo("NO_BIKE");
    }

    @Test
    void unavailableBikeIsReportedBeforeCredit() {
        Rider broke = new Rider("Kagiso Molefe", true, false, 0.0);
        bike.markUnavailable();
        assertThat(validator.checkEligibility(broke, bike)).isEqualTo("BIKE_UNAVAILABLE");
    }
}
