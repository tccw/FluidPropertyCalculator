package model;

public class Oil extends Fluid {

    private double apiGravity;
    private boolean live;

    public Oil() {
        temperature = 45.0;  // 25C / km thermal gradient at 1 km depth
        pressure = 10.0;  // roughly hydrostatic pressure at 1 km
        solutionGasRatio = 0;    // default to no dissolved gas
        apiGravity = 35.0;
        live = false;
    }

    //------ unique getters -----
    public double getAPIGravity() {
        return 0;
    }

    public boolean isLive() {
        return true;
    }

    //------ unique setters -----
    public void setAPIGravity(double apiGravity) {
        this.apiGravity = apiGravity;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    //----- calculations -----

    @Override
    public double density() {
        //stub
        return 0;
    }

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

}


