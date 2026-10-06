package za.co.wethinkcode.ridehub.rentals;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class LateReturnPenaltyTest {

    private final LateReturnPenalty penalty = new LateReturnPenalty();

    // Worked example: study its name and shape, then write your own below.
    @Test
    void noPenalty_whenReturnedOnTime() {
        assertThat(penalty.calculate(0)).isCloseTo(0.0, within(0.001));
    }

    // TODO (Q5.3): add your tests below this line.
    // You may only edit this file - do not change LateReturnPenalty.
}
