package fr.android.ppe.frap;

import com.google.android.gms.maps.model.LatLng;

public class Hydrant {

    private double id;
    private LatLng coord;
    private double pression_static;
    private double pression_dynamic;

    public Hydrant() {}

    public Hydrant(double id_hydrant,LatLng coord, double press_stat, double press_dyn)
    {
        this.id=id_hydrant;
        this.coord=coord;
        this.pression_static=press_stat;
        this.pression_dynamic=press_dyn;
    }

    public double getId()
    {return this.id;}

    public void setId(double _id)
    {this.id=_id ;}

    public LatLng getCoord()
    {
        return this.coord;
    }

    public void setCoord(LatLng _coord)
    {
        this.coord=_coord;
    }

    public double getPression_static()
    {return this.pression_static;}

    public void setPression_static(double _pression_static)
    {this.pression_static=_pression_static;}

    public double getPression_dynamic()
    {return this.pression_dynamic;}

    public void setPression_dynamic(double _pression_dynamique)
    {this.pression_dynamic=_pression_dynamique;}

}
