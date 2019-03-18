package fr.android.ppe.frap;

import com.google.android.gms.maps.model.LatLng;

public class Hydrant {

    private int id;
    private double lat;
    private double lng;
    private double pression_static;
    private double pression_dynamic;

    public Hydrant() {}

    public Hydrant(int _id, double _pression_stat, double _pression_dyn){
        this.id=_id;
        this.pression_static=_pression_stat;
        this.pression_dynamic=_pression_dyn;
    }

    public Hydrant(int id_hydrant, double _lat, double _lng, double press_stat, double press_dyn)
    {
        this.id=id_hydrant;
        this.lat=_lat;
        this.lng=_lng;
        this.pression_static=press_stat;
        this.pression_dynamic=press_dyn;
    }

    public int getId()
    {return this.id;}

    public void setId(int _id)
    {this.id=_id ;}

    public double getLat(){return lat;}

    public void setLat(double _lat){this.lat=_lat;}

    public double getLng(){return this.lng;}

    public double getPression_static()
    {return this.pression_static;}

    public void setPression_static(double _pression_static)
    {this.pression_static=_pression_static;}

    public double getPression_dynamic()
    {return this.pression_dynamic;}

    public void setPression_dynamic(double _pression_dynamique)
    {this.pression_dynamic=_pression_dynamique;}

}
