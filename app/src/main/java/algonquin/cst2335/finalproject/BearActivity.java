package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.finalproject.databinding.ActivityBearBinding;

public class BearActivity extends AppCompatActivity {

    ActivityBearBinding binding;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        editor= prefs.edit();

        //prefs.getString("width", "");
       // prefs.getString("height", "");
        binding.width.setText(prefs.getString("width", ""));
        binding.height.setText(prefs.getString("height", ""));

       // SharedPreferences.Editor editor = prefs.edit();


        binding.button.setOnClickListener(clk-> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Are you sure you want to generate the image?");
            builder.setPositiveButton("Yes", (dialog, click) -> {
                String width = binding.width.getText().toString();
                String height = binding.height.getText().toString();

                Toast.makeText(this, "Image generated!", Toast.LENGTH_SHORT).show();

                Snackbar.make(binding.getRoot(), "Generated image with size: " + width + "*" + height, Snackbar.LENGTH_LONG)

                        .show();

                editor.putString("width", width);
                editor.putString("height", height);
                editor.apply();
                    });

                builder.setNegativeButton("No", (dialog2, click2) -> {});
                builder.create().show();


            });
        }
    }







