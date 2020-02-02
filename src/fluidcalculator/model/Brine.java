package model;

public class Brine extends Fluid {

    // class for a model.Brine in the Batzle & Wang domain
    private static final double[][] W_COEFF = {{1402.85, 1.524, 3.437e-3, -1.197e-5},
            {4.871, -0.0111, 1.739e-4, -1.628e-6},
            {-0.04783, 2.747e-4, -2.135e-6, 1.237e-8},
            {1.487e-4, -6.503e-7, -1.455e-8, 1.327e-10},
            {-2.197e-7, 7.987e-10, 5.230e-11, -4.614e-13}};

    private boolean live;

    public Brine() {
        temperature = 45.0;  // 25C / km thermal gradient at 1 km depth
        pressure = 10.0;  // roughly hydrostatic pressure at 1 km
        salinity = 0.035; // rough salinity of seawater
        solutionGasRatio = 0;    // default to no dissolved gas
        live = false;
    }

    //------ unique getters -------
    public boolean isLive() {
        return live;
    }

    //----- calculations -----
    // REQUIRES:
    // EFFECTS:
    private double waterDensity() {
        //equation 27a
        double T = this.temperature;
        double P = this.pressure;
        double den = 1 + 1e-6 * (-80 * T - 3.3 * Math.pow(T, 2) + 0.00175 * Math.pow(T, 3) + 489 * P
                - 2 * T * P + 0.016 * Math.pow(T, 2) * P - 1.3e-5 * Math.pow(T, 3) * P
                - 0.333 * Math.pow(P, 2) - 0.002 * T * Math.pow(P, 2));

        return den;
    }

    // REQUIRES: Sodium chloride solution. There can be considerable errors with other mineral salts.
    // EFFECTS:
    private double brineDensity() {
        // equation 27b
        double S = this.salinity;
        double P = this.pressure;
        double T = this.temperature;
        double den = this.waterDensity() + S * (0.668 + 0.44 * S + 1e-6 *
                (300 * P - 2400 * P * S + T * (80 + 3 * T - 3300 * S - 13 * P + 47 * P * S)));
        return den;
    }

    @Override
    public double density() {
        return brineDensity();
    }

    // REQUIRES: Valid for 0 < temperature <= 100 C, and 0 < pressure <= 100 MPa.
    // EFFECTS:
    private double vpWater() {
        // equation 28
        double vpWater = 0;
        double T = this.temperature;
        double P = this.pressure;

        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 5; i++) {
                vpWater += W_COEFF[i][j] * Math.pow(T, i) * Math.pow(P, j);
            }
        }
        return vpWater;
    }

    // REQUIRES:
    // EFFECTS:
    public double vpBrine() {
        // equation 29
        double S = this.salinity;
        double T = this.temperature;
        double P = this.pressure;
        double vpBrine = vpWater() + S * (1170 - 9.6 * T + 0.055 * Math.pow(T, 2) - 8.5e-5 * Math.pow(T, 3) + 2.6 * P
                - 0.0029 * T * P - 0.0476 * Math.pow(P, 2)) + Math.pow(S, 1.5) * (780 - 10 * P + 0.16 * Math.pow(P, 2))
                - 820 * Math.pow(S, 2);
        return vpBrine;
    }

    // REQUIRES:
    // EFFECTS: Calculates the compressional velocity of a brine using an empirical relationship from Batzle & Wang 1992
    public double compressionalVelocity() {
        return vpBrine();
    }

    // REQUIRES: Properties of the dissolved gas.
    // EFFECTS: Calculates the bulk modulus brine with dissolved gas.
    private double gassyBrineBulkModulus() {
        double Rg = this.solutionGasRatio;
        double bulkModGasFree = brineDensity() * Math.pow(vpBrine(), 2);
        double bulkModDissolvedGas = bulkModGasFree / (1 + 0.0494 * Rg);
        return bulkModDissolvedGas / 1e6; // units correction
    }

    // REQUIRES:
    // EFFECTS: Calculates the bulk modulus of a brine with no dissolved gas.
    private double brineBulkModulus() {
        double bulkModGasFree = brineDensity() * Math.pow(vpBrine(), 2);
        return bulkModGasFree / 1e6; // units correction
    }

    // REQUIRES:
    // EFFECTS: Calculates the bulk modulus of a brine.
    public double bulkModulus() {
        if (this.live) {
            return this.gassyBrineBulkModulus();
        } else {
            return this.brineBulkModulus();
        }
    }

    // REQUIRES: Temperature < 250 C
    // EFFECTS: Calculates the viscosity of a brine in centipoise. Ignores pressure and dissolved gas as these effects are not
    //           expected to be large. (per Batzle & Wang, 1992)
    public double viscosity() {
        double S = this.salinity;
        double T = this.temperature;
        double viscosity = 0.1 + 0.333 * S + (1.65 + 91.9 * Math.pow(S, 3)) *
                Math.exp(-(0.42 * Math.pow((Math.pow(S, 0.8) - 0.17), 2) + 0.045) * Math.pow(T, 0.8));
        return viscosity;
    }

    // REQUIRES:
    // EFFECTS: Calculates the bulk modulus and density of a brine. Outputs a double[]
    //          where the first element is the bulk modulus (K) and the second is density (rhobr) in g/cc.
    public double[] brineProperties() {
        double[] brineProps = {0.0, 0.0};
        brineProps[0] = this.bulkModulus();
        brineProps[1] = this.density();

        return brineProps;
    }


    public static void main(String[] args) {
        Brine B101 = new Brine();
        double[] props = B101.brineProperties();
        System.out.println("Bulk Modulus: " + props[0] + " GPa");
        System.out.println("Density: " + props[1] + " g/cc");
        System.out.println("P-wave Velocity:" + B101.compressionalVelocity());


    }


}
