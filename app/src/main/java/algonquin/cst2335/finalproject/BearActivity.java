package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.finalproject.databinding.ActivityBearBinding;

public class BearActivity extends AppCompatActivity {

    ActivityBearBinding binding;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;


    RequestQueue queue = null;

    Bitmap image=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);

        binding = ActivityBearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        editor = prefs.edit();


        binding.width.setText(prefs.getString("width", ""));
        binding.height.setText(prefs.getString("height", ""));


        binding.button.setOnClickListener(clk -> {

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

            builder.setNegativeButton("No", (dialog2, click2) -> {
            });
            builder.create().show();


        });


        binding.generate.setOnClickListener(click->{
            String width= binding.width.getText().toString();
            String height= binding.height.getText().toString();


            String imageUrl ="https://placebear.com/" + width+"/"+ height;
            ImageRequest imgReq = new ImageRequest(imageUrl, new Response.Listener<Bitmap>(){

                    @Override
                    public void onResponse(Bitmap response) {
                        binding.image.setImageBitmap(response);
                        binding.image.setVisibility(View.VISIBLE);


                    }
                },1024, 1024, ImageView.ScaleType.CENTER, null, (error ) -> {});

                queue.add(imgReq);

        });


    };


    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        AlertDialog.Builder builder = new AlertDialog.Builder(BearActivity.this);

        if (item.getItemId() == R.id.help) {
            builder.setTitle(getString(R.string.help_navi));
            builder.setMessage(getString(R.string.help_bear));
            builder.setPositiveButton("OK", ((dialog, click) -> {}));
            builder.setNegativeButton("", ((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.aviation) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent aviationPage = new Intent(BearActivity.this, AviationActivity.class);
                startActivity(aviationPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.quiz) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent triviaPage = new Intent(BearActivity.this, TriviaActivity.class);
                startActivity(triviaPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.bear) {
            Toast.makeText(this, getString(R.string.current_page), Toast.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.currency) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent bearPage = new Intent(BearActivity.this, CurrencyActivity.class);
                startActivity(bearPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.contact) {
            Snackbar.make(binding.getRoot(), getString(R.string.contact_msg), Snackbar.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.about) {
            Toast.makeText(this, getString(R.string.about_msg), Toast.LENGTH_LONG).show();
        }

        return true;
    }
}








