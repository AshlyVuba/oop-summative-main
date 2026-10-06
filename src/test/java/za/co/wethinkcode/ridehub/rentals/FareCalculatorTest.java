package za.co.wethinkcode.ridehub.rentals;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FareCalculatorTest {

    private final FareCalculator calculator = new FareCalculator();

    @Test
    void standardBike_tenMinutes() {
        assertThat(calculator.calculateFare("STANDARD", 10, false)).isCloseTo(12.50, within(0.001));
    }

    @Test
    void electricBike_tenMinutes() {
        assertThat(calculator.calculateFare("ELECTRIC", 10, false)).isCloseTo(25.00, within(0.001));
    }

    @Test
    void cargoBike_tenMinutes() {
        assertThat(calculator.calculateFare("CARGO", 10, false)).isCloseTo(20.50, within(0.001));
    }

    @Test
    void zeroMinutes_costsOnlyTheUnlockFee() {
        assertThat(calculator.calculateFare("STANDARD", 0, false)).isCloseTo(5.00, within(0.001));
    }

    @Test
    void members_getTenPercentOff_onStandardBikes() {
        assertThat(calculator.calculateFare("STANDARD", 10, true)).isCloseTo(11.25, within(0.001));
    }

    @Test
    void members_getTenPercentOff_onElectricBikes() {
        assertThat(calculator.calculateFare("ELECTRIC", 20, true)).isCloseTo(36.00, within(0.001));
    }

    @Test
    void members_getTenPercentOff_onCargoBikes() {
        assertThat(calculator.calculateFare("CARGO", 10, true)).isCloseTo(18.45, within(0.001));
    }

    @Test
    void unknownBikeType_isRejected() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateFare("SCOOTER", 10, false));
    }

    @Test
    void bikeTypeIsCaseSensitive() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateFare("standard", 10, false));
    }
}
