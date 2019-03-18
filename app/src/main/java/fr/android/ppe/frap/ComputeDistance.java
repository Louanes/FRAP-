package fr.android.ppe.frap;

import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import java.text.DecimalFormat;
import java.util.ArrayList;
import static java.lang.Math.asin;
import static java.lang.Math.cbrt;
import static java.lang.Math.pow;

import fr.android.ppe.frap.Hydrant;


public class ComputeDistance {

    //Valeur des pertes de charges de référence
    private final float j_45=1.5f;
    private final float j_70=0.55f;
    private final float j_110=0.28f;

    //Valeur des débit de référence
    private final int d_45=250;
    private final int d_70=500;
    private final int d_110=1000;

    //Les débits à calculer
    private final int t_1000=1000;
    private final int t_1500=1500;
    private final int t_2000=2000;

    //Ratio de securite
    private final float ratio_sec=0.9f;



    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
        + " Meter   " + meterInDec);

        return Radius * c;
    }

    public ArrayList getAvailableHydrant()
    {
        ArrayList<Hydrant> listAvailableHydrant = new ArrayList<Hydrant>() {
        };
        Hydrant currentHydrant;
        LatLng inc_loc;


        //Récupèrer l'adresse de l'incendie

        //Calculer la distance entre le lieu et les hydrants, si <250m, ajouter l'hydrant dans la liste
        return listAvailableHydrant;
    }

    public void computeHydrantRadius(Hydrant hydrant, int diametre_tuyau)
    {
        double L_1000, L_1500, L_2000;

        switch (diametre_tuyau)
        {
            case 45 :
                L_1000=100*pow(d_45/t_1000,2)*((hydrant.getPression_static()-hydrant.getPression_dynamic())/j_45)*ratio_sec;
                L_1500=100*pow(d_45/t_1500,2)*((hydrant.getPression_static()-hydrant.getPression_dynamic())/j_45)*ratio_sec;
                L_2000=100*pow(d_45/t_2000,2)*((hydrant.getPression_static()-hydrant.getPression_dynamic())/j_45)*ratio_sec;
                break;

            case 70:
                L_1000=100*pow(d_70/t_1000,2)*((hydrant.getPression_static()-hydrant.getPression_dynamic())/j_70)*ratio_sec;
                L_1500=100*pow(d_70/t_1500,2)*((hydrant.getPression_static()-hydrant.getPression_dynamic())/j_70)*ratio_sec;
                L_2000=100*pow(d_70/t_2000,2)*((hydrant.getPression_static()-hydrant.getPression_dynamic())/j_70)*ratio_sec;
                break;

            case 110:
                L_1000=100*pow(d_110/t_1000,2)*((hydrant.getPression_static()-hydrant.getPression_dynamic())/j_110)*ratio_sec;
                L_1500=100*pow(d_110/t_1500,2)*((hydrant.getPression_static()-hydrant.getPression_dynamic())/j_110)*ratio_sec;
                L_2000=100*pow(d_110/t_2000,2)*((hydrant.getPression_static()-hydrant.getPression_dynamic())/j_110)*ratio_sec;
                break;

            default:
                Log.i("Diamètre du tuyau", "Non récupérer");
                break;
        }

        //Afficher les cercles en fonction des valeurs obtenues

    }

}
