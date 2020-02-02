package model;

public abstract class Fluid {

    public double temperature;      // temperature in degrees C
    public double pressure;         // Pressure in MPa
    public double solutionGasRatio; // Dissolved gas ratio (L/L) [For gassy brine]

    //----- getters -----
    public double getTemperature() {
        return temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public double getRg() {
        return solutionGasRatio;
    }

    //----- setters ------
    // MODIFIES: this
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    //REQUIRES: Pressure > 0
    // MODIFIES: this
    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    // REQUIRES: Rg is > 0
    // MODIFIES: this
    public void setSolutionGasRatio(double rg) {
        this.solutionGasRatio = rg;
    }

    // REQUIRES:
    // EFFECTS:
    public abstract double density();

    // REQUIRES:
    // EFFECTS:
    public abstract double compressionalVelocity();

    // REQUIRES:
    // EFFECTS:
    public abstract double bulkModulus();

    // REQUIRES:
    // EFFECTS:
    public abstract double viscosity();

    // REQUIRES:
    // EFFECTS:
    public abstract double[] calcProperties();
}
