package fluidcalculator;

import model.Oil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestOil {

    private Oil oil;
    double props[];

    @BeforeEach
    public void setUp() {
        oil = new Oil();
    }

    @Test
    public void testOilConstructor() {
        helperAssertValues(15, 10, 35.0, 0, false, oil);
    }

    @Test
    public void testOilSingleSetter() {
        oil.setTemperature(43);
        oil.setPressure(65);
        oil.setAPIGravity(29);
        oil.setSolutionGasRatio(23);
        oil.setLive(true);
        helperAssertValues(43, 65, 29, 23, true, oil);
    }

    @Test
    public void testMultipleAssignment() {
        oil.setSolutionGasRatio(34);
        oil.setTemperature(53);
        oil.setPressure(65);
        oil.setLive(true);
        oil.setAPIGravity(12);

        oil.setSolutionGasRatio(23);
        oil.setTemperature(67);
        oil.setPressure(34);
        oil.setLive(false);
        oil.setAPIGravity(43);
        helperAssertValues(67, 24, 43, 23, false, oil);
    }

    @Test
    public void testOilViscosity() {
        helperAssertValues(15, 10, 35.0, 0, false, oil);
        double density = oil.density();
        double T = oil.getTemperature();
        double P = oil.getPressure();
        double y = Math.pow(10, (5.693 - 2.863) / density);
        double muT = Math.pow(10, 0.505 * y * Math.pow(17.8 + T, -1.163));
        double I = Math.pow(10, 18.6 * (0.1 * Math.log10(muT) + Math.pow(Math.log10(muT) + 2,-0.1) - 0.985));
        double muExpected = muT + 0.145 * P * I;
        assertEquals(muExpected, oil.viscosity());
    }

    @Test
    public void testDensity() {
        //stub
    }

    @Test
    public void test
    // Helpers
    private void helperAssertValues(double temp, double press, double api, double rg, boolean live, Oil b) {
        assertEquals(temp, b.getTemperature());
        assertEquals(press, b.getPressure());
        assertEquals(api, b.getAPIGravity());
        assertEquals(rg, b.getRg());
        assertEquals(live, b.isLive());
    }

}
