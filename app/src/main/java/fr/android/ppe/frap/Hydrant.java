package fr.android.ppe.frap;

public class Hydrant {

    private double id;
    private double longitude;
    private double latitude;
    private double pression_static;
    private double pression_dynamic;

    public Hydrant(double id_hydrant,double long_hydrant,double lat_hydrant, double press_stat, double press_dyn)
    {
        id_hydrant = id;
        long_hydrant = longitude;
        lat_hydrant = latitude;
        press_stat = pression_static;
        press_dyn = pression_dynamic;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double[] Hydrant_position()
    {
        double[] position = new double[2];
        position[0]=getLongitude();
        position[1]=getLatitude();

        return position;
    }
}
