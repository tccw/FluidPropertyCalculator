package model;

public abstract class Fluid {

    public double temperature;      // temperature in degrees C
    public double pressure;         // Pressure in MPa
    public double salinity;         // Salinity in weight fraction (i.e. ppm/1e6)
    public double solutionGasRatio; // Dissolved gas ratio (L/L) [For gassy brine]
    public double density;

    //----- getters -----
    public double getTemperature() {
        return temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public double getSalinity() {
        return salinity;
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

    // REQUIRES: Salinity >= 0, and <= 1.
    // MODIFIES: this
    public void setSalinity(double salinity) {
        this.salinity = salinity;
    }

    // REQUIRES: Rg is > 0
    // MODIFIES: this
    public void setSolutionGasRatio(double rg) {
        this.solutionGasRatio = rg;
    }

    // REQUIRES:
    // EFFECTS:
    public abstract double compressionalVelocity();

    // REQUIRES:
    // EFFECTS:
    public abstract double density();

    // REQUIRES:
    // EFFECTS:
    public abstract double bulkModulus();

    // REQUIRES:
    // EFFECTS:
    public abstract double viscosity();
}
