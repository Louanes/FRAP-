package fr.android.ppe.frap;

public class Hydrant {

    private double Lieu_lat=0.0;
    private double Lieu_lng=0.0;
    private float pstat;
    private float pdym;
    private boolean disponible;

    public Hydrant(){

    }

    public Hydrant(double latitude,double longitude){
        this.Lieu_lat=latitude;
        this.Lieu_lng=longitude;
    }

    public Hydrant(double latitude,double longitude,float pstat,float pdym,boolean disponible){
        this.Lieu_lat=latitude;
        this.Lieu_lng=longitude;
        this.pstat=pstat;
        this.pdym=pdym;
        this.disponible=disponible;
    }

    public double getLatitude(){
        return Lieu_lat;
    }

    public double getLongitude(){
        return Lieu_lng;
    }

    public float getPstat(){
        return pstat;
    }

    public float getPdyn(){
        return pdym;
    }

    public boolean getDisponible(){
        return disponible;
    }

    public void setLatitude(double latitude){
        this.Lieu_lat=latitude;
    }

    public void setLongitude(double longitude){
        this.Lieu_lng=longitude;
    }

}
