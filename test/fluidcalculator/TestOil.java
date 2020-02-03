package fluidcalculator;

import model.Oil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestOil {

    private Oil oil;
    double[] props;

    @BeforeEach
    public void setUp() {
        oil = new Oil();
    }

    @Test
    public void testOilConstructor() {
        helperAssertValues(45, 10, 39.6, 5, 0.6,false, oil);
    }

    @Test
    public void testOilSingleSetter() {
        oil.setTemperature(43);
        oil.setPressure(65);
        oil.setAPIGravity(29);
        oil.setSolutionGasRatio(23);
        oil.setLive(true);
        helperAssertValues(43, 65, 29, 23, 0.6,true, oil);
    }

    @Test
    public void testMultipleAssignment() {
        oil.setSolutionGasRatio(34);
        oil.setTemperature(53);
        oil.setPressure(65);
        oil.setLive(true);
        oil.setAPIGravity(12);
        oil.setGasGravity(0.24);

        oil.setSolutionGasRatio(23);
        oil.setTemperature(67);
        oil.setPressure(34);
        oil.setLive(false);
        oil.setAPIGravity(43);
        oil.setGasGravity(0.67);
        helperAssertValues(67, 24, 43, 23,0.67, false, oil);
    }

    @Test
    public void testOilViscosity() {
        helperAssertValues(45, 10, 39.6, 5, 0.6,false, oil);
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
    public void testDensityLiveOil() {
        oil.setSolutionGasRatio(30);
        oil.setLive(true);
        helperAssertValues(45, 10, 39.6, 30, 0.6, true, oil);
        double P = oil.getPressure();
        double T = oil.getTemperature();
        double G = oil.getGasGravity();
        double Rg = oil.getSolutionGasRatio();
        double denO = 141.5 / (oil.getAPIGravity() + 131.5);
        double volumeFactorBo = 0.972 + 0.00038 * Math.pow((2.4 * Rg) * Math.sqrt(G / denO) + T + 17.8, 1.175);
        double expectedDenLiveOil = (denO + 0.0012 * G * Rg) / volumeFactorBo;
        assertEquals(expectedDenLiveOil, oil.density());
    }

    @Test
    public void testDensityDeadOil() {
        helperAssertValues(45, 10, 39.6, 5, 0.6, false, oil);
        double P = oil.getPressure();
        double T = oil.getTemperature();
        double denO = 141.5 / (oil.getAPIGravity() + 131.5);
        double denP = denO + (0.00277 * P - 1.71e-7 * Math.pow(P,3)) * Math.pow((denO - 1.15),2) + 3.49e-4 * P;
        double expectedDensity = denP / (0.972 + 3.81e-4 * Math.pow((T + 17.78), 1.175));
        assertEquals(expectedDensity, oil.density());
    }

    @Test
    public void testCompressionalVelocity() {
        double P = oil.getPressure();
        double T = oil.getTemperature();
        double expectedVelocity = 15450 * Math.pow((77.1 + oil.getAPIGravity()), -0.5)
                - 3.7 * T + 4.64 * P + 0.0115 * (0.36 * Math.pow(oil.getAPIGravity(), 0.5) - 1) * T * P;
        assertEquals(expectedVelocity, oil.compressionalVelocity());
    }

    @Test
    public void testBulkModulusLiveOil() {
        oil.setLive(true);
        oil.setSolutionGasRatio(50);
        helperAssertValues(45, 10, 39.6, 50, 0.6, true, oil);
        double expectedBulkModulusLiveOil = (oil.density() * Math.pow(oil.compressionalVelocity(),2)) / 1e6;
        assertEquals(expectedBulkModulusLiveOil, oil.bulkModulus());
    }

    @Test
    public void testBulkModulusDeadOil() {
        helperAssertValues(45, 10, 39.6, 5, 0.6, false, oil);
        double expectedBulkModulusLiveOil = (oil.density() * Math.pow(oil.compressionalVelocity(),2)) / 1e6;
        assertEquals(expectedBulkModulusLiveOil, oil.bulkModulus());
    }

    @Test
    public void testCalcProperties() {
        //stub
    }

    @Test
    public void  testCompressionalVelocityDeadOil() {
        //stub
    }

    @Test public void testCompressionalVelocityLiveOil() {
        //stub
    }

    // Helpers
    private void helperAssertValues(double temp, double press, double api, double rg, double G, boolean live, Oil o) {
        assertEquals(temp, o.getTemperature());
        assertEquals(press, o.getPressure());
        assertEquals(api, o.getAPIGravity());
        assertEquals(rg, o.getSolutionGasRatio());
        assertEquals(G, o.getGasGravity());
        assertEquals(live, o.isLive());
    }

}
