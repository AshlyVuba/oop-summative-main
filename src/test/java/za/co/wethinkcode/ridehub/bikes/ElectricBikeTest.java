package za.co.wethinkcode.ridehub.bikes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ElectricBikeTest {

    @Test
    void unlockFee_isTenRand() {
        assertThat(new ElectricBike("EB-001", 80).unlockFee()).isCloseTo(10.00, within(0.001));
    }

    @Test
    void pricePerMinute_isOneFifty() {
        assertThat(new ElectricBike("EB-001", 80).pricePerMinute()).isCloseTo(1.50, within(0.001));
    }

    @Test
    void keepsTheBatteryPercentageItWasGiven() {
        assertThat(new ElectricBike("EB-001", 64).getBatteryPercent()).isEqualTo(64);
    }

    @Test
    void rejectsBatteryPercentageAboveOneHundred() {
        assertThrows(IllegalArgumentException.class, () -> new ElectricBike("EB-001", 101));
    }

    @Test
    void rejectsNegativeBatteryPercentage() {
        assertThrows(IllegalArgumentException.class, () -> new ElectricBike("EB-001", -1));
    }
}
