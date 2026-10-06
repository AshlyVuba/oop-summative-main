package za.co.wethinkcode.ridehub.bikes;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

class BikeTest {

    @Test
    void bike_cannotBeInstantiatedOnItsOwn() {
        assertThat(Modifier.isAbstract(Bike.class.getModifiers()))
                .as("Bike should be an abstract class - every bike is a StandardBike, ElectricBike or CargoBike")
                .isTrue();
    }

    @Test
    void unlockFee_isDeclaredAbstract() throws NoSuchMethodException {
        Method unlockFee = Bike.class.getDeclaredMethod("unlockFee");
        assertThat(Modifier.isAbstract(unlockFee.getModifiers()))
                .as("Bike cannot work out an unlock fee on its own - each subclass must supply it")
                .isTrue();
    }

    @Test
    void pricePerMinute_isDeclaredAbstract() throws NoSuchMethodException {
        Method pricePerMinute = Bike.class.getDeclaredMethod("pricePerMinute");
        assertThat(Modifier.isAbstract(pricePerMinute.getModifiers()))
                .as("Bike cannot work out a per-minute price on its own - each subclass must supply it")
                .isTrue();
    }

    @Test
    void serialNo_isNotAPublicField() throws NoSuchFieldException {
        Field serialNo = Bike.class.getDeclaredField("serialNo");
        assertThat(Modifier.isPrivate(serialNo.getModifiers()))
                .as("A bike's data should only be reachable through its methods")
                .isTrue();
    }

    @Test
    void tripCost_isUnlockFeePlusPricePerMinuteTimesMinutes() {
        Bike bike = new StandardBike("SB-100");
        assertThat(bike.tripCost(10)).isEqualTo(5.00 + 0.75 * 10);
    }

    @Test
    void newBike_isAvailable_andCanBeMarkedUnavailable() {
        Bike bike = new StandardBike("SB-101");
        assertThat(bike.isAvailable()).isTrue();

        bike.markUnavailable();

        assertThat(bike.isAvailable()).isFalse();
    }
}
