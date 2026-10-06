package za.co.wethinkcode.ridehub.bikes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class StandardBikeTest {

    @Test
    void unlockFee_isFiveRand() {
        assertThat(new StandardBike("SB-001").unlockFee()).isCloseTo(5.00, within(0.001));
    }

    @Test
    void pricePerMinute_isSeventyFiveCents() {
        assertThat(new StandardBike("SB-001").pricePerMinute()).isCloseTo(0.75, within(0.001));
    }

    @Test
    void type_isStandard() {
        assertThat(new StandardBike("SB-001").getType()).isEqualTo("STANDARD");
    }
}
