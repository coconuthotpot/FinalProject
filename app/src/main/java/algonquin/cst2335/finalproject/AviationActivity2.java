package algonquin.cst2335.finalproject;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.Snackbar;

public class AviationActivity2 extends AviationActivity{

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviation2);

        Button showSnackbarButton = findViewById(R.id.button2);
        Button showAlertDialogButton = findViewById(R.id.button3);
        View parentLayout = findViewById(android.R.id.content);

        showSnackbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(parentLayout, "This is a Snackbar notification", Snackbar.LENGTH_SHORT);
                snackbar.show();
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

