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
