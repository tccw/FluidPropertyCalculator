package fluidcalculator;

import model.Brine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBrine {

    private static final double[] W_COEFF = {1402.85, 1.524, 3.437e-3, -1.197e-5, 4.871,
            -0.0111, 1.739e-4, -1.628e-6, -0.04783, 2.747e-4,
            -2.135e-6, 1.237e-8, 1.487e-4, -6.503e-7, -1.455e-8,
            1.327e-10, -2.197e-7, 7.987e-10, 5.230e-11, -4.614e-13};
    private Brine b;
    double[] props;

    @BeforeEach
    public void setUp() {
        b = new Brine();
    }

    @Test
    public void testConstructor() {
        helperAssertValues(45.0,10.0, 0.035, 0.0, false, b);
    }

    @Test
    public void testBrineSingleSetter() {
        b.setPressure(55);
        b.setTemperature(85);
        b.setSalinity(0.088);
        b.setSolutionGasRatio(25);
        b.setLive(true);
        helperAssertValues(85, 55,0.088,25.0,true, b);
    }

    @Test
    public void testMultipleAssignment() {
        b.setPressure(20);
        b.setPressure(32.2);
        assertEquals(32.2, b.getPressure());
    }

    @Test
    public void testBrinePropertiesNotGassy() {
        b.setLive(false);
        props = b.calcProperties();
        double bulkModNotGassy = helperTestDensityBrine(b) * Math.pow(helperVpBrineNotGassy(45.0, 10.0, 0.035), 2);
        bulkModNotGassy /= 1e6;
        assertEquals(bulkModNotGassy, props[0]);
        assertEquals(helperTestDensityBrine(b), props[1]);
    }

    @Test
    public void testBrinePropertiesGassy() {
        b.setSolutionGasRatio(20);
        b.setLive(true);
        props = b.calcProperties();
        assertEquals(helperGassyBrineBulkMod(b.getTemperature(), b.getPressure(), b.getSalinity(), b.getSolutionGasRatio()), props[0]);  // bulk modulus
        assertEquals(helperTestDensityBrine(b), props[1]);      // density
    }

    @Test
    public void testBrineViscosity() {
        helperAssertValues(45.0,10.0, 0.035, 0.0, false, b);
        double expTerm = -(0.42 * Math.pow((Math.pow(0.035, 0.8) - 0.17), 2) + 0.045) * Math.pow(45, 0.8);
        double expectedViscosity =  0.1 + 0.333 * 0.035 + (1.65 + 91.9 * Math.pow(0.035,3)) * Math.exp(expTerm);
        assertEquals(expectedViscosity, b.viscosity());
    }

    private double helperTestDensityBrine(Brine b) {
        double densityWater;
        double densityBrine;
        double T = b.getTemperature();
        double P = b.getPressure();
        double S = b.getSalinity();

        densityWater = 1 + 1e-6 * (-80 * T - 3.3 * Math.pow(T, 2) + 0.00175 * Math.pow(T, 3) + 489 * P
                - 2 * T * P + 0.016 * Math.pow(T, 2) * P - 1.3e-5 * Math.pow(T, 3) * P
                - 0.333 * Math.pow(P, 2) - 0.002 * T * Math.pow(P, 2));
        densityBrine = densityWater + S * (0.668 + 0.44 * S + 1e-6 *
                (300 * P - 2400 * P * S + T * (80 + 3 * T - 3300 * S - 13 * P + 47 * P * S)));

        return densityBrine;
    }

    private double helperVpBrineNotGassy(double T, double P, double S) {
        double vpWaterTest = 1402.85 * Math.pow(T, 0) * Math.pow(P, 0) + 4.871 * Math.pow(T, 1) * Math.pow(P, 0) +
                -0.04783 * Math.pow(T, 2) * Math.pow(P, 0) + 1.487e-4 * Math.pow(T, 3) * Math.pow(P, 0) +
                -2.197e-7 * Math.pow(T, 4) * Math.pow(P, 0) + 1.524 * Math.pow(T, 0) * Math.pow(P, 1) +
                -0.0111 * Math.pow(T, 1) * Math.pow(P, 1) + 2.747e-4 * Math.pow(T, 2) * Math.pow(P, 1) +
                -6.503e-7 * Math.pow(T, 3) * Math.pow(P, 1) + 7.987e-10 * Math.pow(T, 4) * Math.pow(P, 1) +
                3.437e-3 * Math.pow(T, 0) * Math.pow(P, 2) + 1.739e-4 * Math.pow(T, 1) * Math.pow(P, 2) +
                -2.135e-6 * Math.pow(T, 2) * Math.pow(P, 2) + -1.455e-8 * Math.pow(T, 3) * Math.pow(P, 2) +
                5.230e-11 * Math.pow(T, 4) * Math.pow(P, 2) + -1.197e-5 * Math.pow(T, 0) * Math.pow(P, 3) +
                -1.628e-6 * Math.pow(T, 1) * Math.pow(P, 3) + 1.237e-8 * Math.pow(T, 2) * Math.pow(P, 3) +
                1.327e-10 * Math.pow(T, 3) * Math.pow(P, 3) + -4.614e-13 * Math.pow(T, 4) * Math.pow(P, 3);
        double vpBrineTest = vpWaterTest + S * (1170 - 9.6 * T + 0.055 * Math.pow(T, 2) - 8.5e-5 * Math.pow(T, 3)
                + 2.6 * P - 0.0029 * T * P - 0.0476 * Math.pow(P, 2))
                + Math.pow(S, 1.5) * (780 - 10 * P + 0.16 * Math.pow(P, 2)) - 820 * Math.pow(S, 2);
        return vpBrineTest;
    }

    private double helperGassyBrineBulkMod(double T, double P, double S, double Rg) {
        double bulkModNotGassy = helperTestDensityBrine(b) * Math.pow(helperVpBrineNotGassy(T, P, S), 2);
        double gassyBrineBulkMod = bulkModNotGassy / (1 + 0.0494 * Rg);
        return gassyBrineBulkMod / 1e6;

    }

    private void helperAssertValues(double temp, double press, double sal, double rg, boolean live, Brine b) {
        assertEquals(temp, b.getTemperature());
        assertEquals(press, b.getPressure());
        assertEquals(sal, b.getSalinity());
        assertEquals(rg, b.getSolutionGasRatio());
        assertEquals(live, b.isLive());
    }


}

