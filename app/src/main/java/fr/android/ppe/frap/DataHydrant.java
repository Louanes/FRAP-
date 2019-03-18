package fr.android.ppe.frap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


public class DataHydrant extends AppCompatActivity {

    private TextView numero_hydrant;
    private TextView pression_stat;
    private TextView pression_dynamique;
    private Spinner spinner;
    private Button button;
    private String diametre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_hydrant);
        numero_hydrant=findViewById(R.id.numero_hydrant);
        pression_stat=findViewById(R.id.ps_result);
        pression_dynamique=findViewById(R.id.pd_result);
        spinner=findViewById(R.id.spinner1);
        button=findViewById(R.id.submit);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(DataHydrant.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);

        //Get data from intent
        Intent intent= getIntent();
        final String numero=intent.getStringExtra("numero_hydrant");
        final String p_stat=intent.getStringExtra("Pression_statique");
        final String p_dym=intent.getStringExtra("Pression_dynamique");
        final String lat=intent.getStringExtra("lat");
        final String lng=intent.getStringExtra("lng");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                diametre=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Hydrant hydrant= new Hydrant(Integer.parseInt(numero), Double.parseDouble(lat), Double.parseDouble(lng),Double.parseDouble(p_stat), Double.parseDouble(p_dym));

        //ComputeDistance computeDistance= new ComputeDistance();
        //computeDistance.computeHydrantRadius(hydrant, 45, new LatLng(hydrant.getLat(),hydrant.getLng()), GoogleMap map);

        numero_hydrant.setText(numero_hydrant.getText().toString()+ numero);
        pression_stat.setText(String.valueOf(p_stat));
        pression_dynamique.setText(String.valueOf(p_dym));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("numero", numero);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("p_stat", p_stat);
                intent.putExtra("p_dym", p_dym);
                intent.putExtra("diametre",diametre);
                setResult(RESULT_OK, intent);
                System.out.println(numero);
                System.out.println(lat);
                System.out.println(lng);
                System.out.println(p_stat);
                System.out.println(p_dym);
                System.out.println(diametre);
                finish();

            }
        });
    }
}
