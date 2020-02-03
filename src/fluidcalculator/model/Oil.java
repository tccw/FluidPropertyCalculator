package model;

public class Oil extends Fluid {

    private double apiGravity;
    private boolean live;
    private double gasGravity; // The molar mass of the natural gas divided by the molar mass of air (unitless)

    public Oil() {
        temperature = 45.0;   // 25C / km thermal gradient at 1 km depth
        pressure = 10.0;      // roughly hydrostatic pressure at 1 km
        solutionGasRatio = 5; // default to low dissolved gas
        apiGravity = 39.6;    // mean API for West Texas Intermediate (WTI) crude oil
        gasGravity = 0.6;     // generic natural gas gravity
        live = false;         // default to dead oil calculations
    }

    //------ unique getters -----
    public double getAPIGravity() {
        return apiGravity;
    }

    public boolean isLive() {
        return live;
    }

    public double getGasGravity() {
        return gasGravity;
    }

    //------ unique setters -----
    public void setAPIGravity(double apiGravity) {
        this.apiGravity = apiGravity;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public void setGasGravity(double gasGravity) {
        this.gasGravity = gasGravity;
    }

    //----- calculations -----

    //REQUIRES: If the oil is live, gasGravity must be > 0, usually < 1. Typical ranges for natural gas are 0.50 - 0.70.
    //EFFECTS: Returns the density of the oil for live or dead cases.
    @Override
    public double density() {
        if (this.live) {
            return this.oilDensityLive();
        } else {
            return this.oilDensityDead();
        }
    }

    //EFFECTS: calculates the compressional velocity of oil in meters/second.
    @Override
    public double compressionalVelocity() {
        //stub
        return 0;
    }

    @Override
    public double bulkModulus() {
        //stub
        return 0;
    }

    //EFFECTS: Uses eqtn 25a, 25b, 26a, and 26b from Batzle & Wang 1992 to return the viscosity in cP for dead oil.
    @Override
    public double viscosity() {
        double density;
        if (this.live) {
            density = this.density();
        } else {
            density = 141.5 / (this.apiGravity + 131.5);
        }
        double P = this.pressure;
        double T = this.temperature;
        double y = Math.pow(10, (5.693 - 2.863) / density);
        double muT = Math.pow(10, 0.505 * y * Math.pow(17.8 + T, -1.163)) - 1;
        double I = Math.pow(10, 18.6 * (0.1 * Math.log10(muT) + Math.pow(Math.log10(muT) + 2, -0.1) - 0.985));

        return muT + 0.145 * P * I;
    }

    @Override
    public double[] calcProperties() {
        //stub
        return new double[0];
    }

    //EFFECTS:
    private double oilDensityLive() {
        double denO = 141.5 / (this.apiGravity + 131.5);
        double G = this.gasGravity;
        double T = this.temperature;
        double Rg = this.solutionGasRatio;
        double volumeFactorBo = 0.972 + 0.00038 * Math.pow((2.4 * Rg * Math.sqrt(G / denO) + T + 17.8), 1.175);
        return (denO + 0.0012 * this.gasGravity * this.solutionGasRatio) / volumeFactorBo;
    }

    //REQUIRES: denO > 0
    //EFFECTS: calculates the density of dead oil at the correct temperature and pressure.
    private double oilDensityDead() {
        double denO = 141.5 / (this.apiGravity + 131.5);
        double P = this.pressure;
        double T = this.temperature;
        double denP = denO + (0.00277 * P - 1.71e-7 * Math.pow(P, 3)) * Math.pow((denO - 1.15), 2) + 3.49e-4 * P;
        return denP / (0.972 + 3.81e-4 * Math.pow(T + 17.78, 1.175));
    }

//    //EFFECTS:
//    private double oilViscosityLive() {
//        double liveOilDensity = this.oilDensityLive();
//        double P = this.pressure;
//        double T = this.temperature;
//        double y = Math.pow(10, (5.693 - 2.863) / liveOilDensity);
//        double muT = Math.pow(10, 0.505 * y * Math.pow(17.8 + T, -1.163)) - 1;
//        double I = Math.pow(10, 18.6 * (0.1 * Math.log10(muT) + Math.pow(Math.log10(muT) + 2, -0.1) - 0.985));
//
//        return muT + 0.145 * P * I;
//    }
//
//    //EFFECTS: Uses eqtn 25a, 25b, 26a, and 26b from Batzle & Wang 1992 to return the viscosity in cP for dead oil.
//    private double oilViscosityDead() {
//        double denO = 141.5 / (this.apiGravity + 131.5);
//        double P = this.pressure;
//        double T = this.temperature;
//        double y = Math.pow(10, (5.693 - 2.863) / denO);
//        double muT = Math.pow(10, 0.505 * y * Math.pow(17.8 + T, -1.163)) - 1;
//        double I = Math.pow(10, 18.6 * (0.1 * Math.log10(muT) + Math.pow(Math.log10(muT) + 2, -0.1) - 0.985));
//
//        return muT + 0.145 * P * I;
//    }
}


