package algonquin.cst2335.finalproject;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class AviationActivity2 extends AviationActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviation2);

        Button showSnackbarButton = findViewById(R.id.button2);
        Button showAlertDialogButton = findViewById(R.id.button3);

        showSnackbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AviationActivity2.this, "This is a Toast notification", Toast.LENGTH_SHORT).show();
            }
        });

        showAlertDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AviationActivity2.this);
                builder.setTitle("Alert")
                        .setMessage("This is an AlertDialog notification")
                        .setPositiveButton("OK", null)
                        .show();
            }

    });
}}

