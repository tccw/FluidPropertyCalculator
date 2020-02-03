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
        double denO = 141.5 / (this.apiGravity + 131.5);
        double G = this.gasGravity;
        double T = this.temperature;
        double Rg = this.solutionGasRatio;
        double volumeFactorBo = 0.972 + 0.00038 * Math.pow((2.4 * Rg * Math.sqrt(G / denO) + T + 17.8), 1.175);
        if (this.live) {
            return this.oilDensityLive(volumeFactorBo, denO);
        } else {
            return this.oilDensityDead(denO);
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

    @Override
    public double viscosity() {
        //stub
        return 0;
    }

    @Override
    public double[] calcProperties() {
        //stub
        return new double[0];
    }

    //EFFECTS:
    private double oilDensityLive(double Bo, double denO) {
        return (denO + 0.0012 * this.gasGravity * this.solutionGasRatio) / Bo;
    }

    private double oilDensityDead(double denO) {
        double P = this.pressure;
        double T = this.temperature;
        double denP = denO + (0.00277 * P - 1.71e-7 * Math.pow(P, 3)) * Math.pow((denO - 1.15),2) + 3.49e-4 * P;
        return denP /(0.972 + 3.81e-4 * Math.pow(T + 17.78, 1.175));
    }

}


