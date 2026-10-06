package za.co.wethinkcode.ridehub.bikes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class FleetTest {

    @Test
    void totalCostFor_isZeroForAnEmptyFleet() {
        assertThat(new Fleet().totalCostFor(10)).isCloseTo(0.0, within(0.001));
    }

    @Test
    void totalCostFor_sumsTheTripCostOfEveryBike() {
        Fleet fleet = new Fleet();
        fleet.add(new StandardBike("SB-001"));
        fleet.add(new ElectricBike("EB-001", 80));

        // standard: 5.00 + 10 * 0.75 = 12.50, electric: 10.00 + 10 * 1.50 = 25.00
        assertThat(fleet.totalCostFor(10)).isCloseTo(37.50, within(0.001));
    }

    @Test
    void getBikes_returnsEveryBikeThatWasAdded() {
        Fleet fleet = new Fleet();
        fleet.add(new StandardBike("SB-001"));
        fleet.add(new StandardBike("SB-002"));

        assertThat(fleet.getBikes()).hasSize(2);
    }
}
