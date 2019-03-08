package fr.android.ppe.frap;

import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import java.text.DecimalFormat;
import java.util.List;
import static java.lang.Math.asin;
import fr.android.ppe.frap.Hydrant;


public class ComputeDistance {

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

    public List<Hydrant> getAvailableHydrant()
    {
        List<Hydrant> listAvailableHydrant;
        Hydrant currentHydrant;
        LatLng inc_loc;
    }

}
