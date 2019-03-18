package fr.android.ppe.frap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DataHydrant extends AppCompatActivity {

    private TextView numero_hydrant;
    private TextView pression_stat;
    private TextView pression_dynamique;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_hydrant);
        numero_hydrant=findViewById(R.id.numero_hydrant);
        pression_stat=findViewById(R.id.ps_result);
        pression_dynamique=findViewById(R.id.pd_result);

        //Get data from intent
        Intent intent= getIntent();
        String numero=intent.getStringExtra("numero_hydrant");
        String p_stat=intent.getStringExtra("Pression_statique");
        String p_dym=intent.getStringExtra("Pression_dynamique");

        numero_hydrant.setText(numero_hydrant.getText().toString()+ numero);
        pression_stat.setText(String.valueOf(p_stat));
        pression_dynamique.setText(String.valueOf(p_dym));
    }
}
