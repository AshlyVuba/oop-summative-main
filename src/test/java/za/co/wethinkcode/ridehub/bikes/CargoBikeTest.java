package za.co.wethinkcode.ridehub.bikes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CargoBikeTest {

    @Test
    void isABike() {
        assertThat(new CargoBike("CB-001", 100) instanceof Bike).isTrue();
    }

    @Test
    void unlockFee_isEightRand() {
        assertThat(new CargoBike("CB-001", 100).unlockFee()).isCloseTo(8.00, within(0.001));
    }

    @Test
    void pricePerMinute_isOneTwentyFive() {
        assertThat(new CargoBike("CB-001", 100).pricePerMinute()).isCloseTo(1.25, within(0.001));
    }

    @Test
    void tripCost_forTenMinutes() {
        assertThat(new CargoBike("CB-001", 100).tripCost(10)).isCloseTo(20.50, within(0.001));
    }

    @Test
    void keepsTheLoadCapacityItWasGiven() {
        assertThat(new CargoBike("CB-001", 120).getLoadCapacityKg()).isEqualTo(120);
    }

    @Test
    void type_isCargo() {
        assertThat(new CargoBike("CB-001", 100).getType()).isEqualTo("CARGO");
    }

    @Test
    void rejectsZeroLoadCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new CargoBike("CB-001", 0));
    }

    @Test
    void rejectsNegativeLoadCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new CargoBike("CB-001", -50));
    }
}
