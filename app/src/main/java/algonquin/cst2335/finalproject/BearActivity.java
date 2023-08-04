package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.ActivityBearBinding;
import algonquin.cst2335.finalproject.databinding.BearDetailsLayoutBinding;

/**
 * This class is the main activity of bear activity
 */
public class BearActivity extends AppCompatActivity {

    /**
     * declare the binding
     */
    ActivityBearBinding binding;

    /**
     * declare the sharedpreferences
     */
    SharedPreferences prefs;
    /**
     * declare the sharedpreference editor
     */
    SharedPreferences.Editor editor;
    /**
     * declare the arraylist to save image
     */
    ArrayList<BearImage> images;
    /**
     * declare the queue
     */
    RequestQueue queue = null;

    Bitmap image = null;
    /**
     * declare myadapter
     */
    RecyclerView.Adapter myAdapter;
    /**
     * declare recyclerview
     */
    private RecyclerView myRecyclerView;
    /**
     * declare DAO
     */
    BearImageDAO myDAO;


    /**
     * declare selectedImage
     */
    MutableLiveData<BearImage> selectedImage = new MutableLiveData<>();


    /**
     * the on create function
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);

        binding = ActivityBearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Toast.makeText(this, getString(R.string.welcomebear), Toast.LENGTH_SHORT).show();

        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        editor = prefs.edit();
        binding.width.setText(prefs.getString("width", ""));
        binding.height.setText(prefs.getString("height", ""));

        myRecyclerView = binding.recycleView;

        BearDatabase db = Room.databaseBuilder(getApplicationContext(), BearDatabase.class, "BearDatabase").build();
        myDAO = db.bDAO();

        if(images == null) {
            images = new ArrayList<>();
        }
        // load image list from db start
        if(images == null || images.size() == 0) {

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                images.addAll( myDAO.getAllImages() ); //Once you get the data from database
                runOnUiThread( () ->  myRecyclerView.setAdapter(myAdapter )); //You can then load the RecyclerView
            });
        }
        //load image list from db end

        /**
         * this is the fragment
         */
        selectedImage.observe(this, (newImage)->{
            if (newImage != null) {


                ImageDetailsFragment bearImageDetailsFragment = new ImageDetailsFragment(newImage);
                //show the fragment on screen
                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fMgr.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentLocation, bearImageDetailsFragment);
                fragmentTransaction.addToBackStack("add to back stack");
                fragmentTransaction.commit();

            } else {
                Log.d(" observe", "newImage is  null");
            }

        });

        /**
         * this is the activity of click button
         */
        binding.generate.setOnClickListener(clk -> {

            String widthEdit = binding.width.getText().toString();
            String heightEdit = binding.height.getText().toString();

            int widthValue = Integer.parseInt(widthEdit);
            int heightValue = Integer.parseInt(heightEdit);

            // Check if width and height are positive whole numbers
            if (widthValue <= 0 || heightValue <= 0) {
                Toast.makeText(BearActivity.this, getString(R.string.checkvalue), Toast.LENGTH_SHORT).show();
                return;
            }

            /**
             *  the alter to user
             */
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(getString(R.string.askgenerated)+ " "+ widthValue+"*"+heightValue);
            builder.setPositiveButton(getString(R.string.yes), (dialog, click) -> {
                String width = binding.width.getText().toString();
                String height = binding.height.getText().toString();


                String imageUrl = "https://placebear.com/" + width + "/" + height;

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh:mm:ss a");
                String currentDateandTime = sdf.format(new Date());

                BearImage bearImage = new BearImage(currentDateandTime, width, height);

                Executor thread1 = Executors.newSingleThreadExecutor();
                thread1.execute(() -> {
                    bearImage.id = (int) myDAO.insertImage(bearImage);//add to database;
                    /*this runs in another thread*/
                });
                ImageRequest imgReq = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {

                    /**
                     * this function is to save the image
                     * @param response  the parameter
                     */
                    @Override
                    public void onResponse(Bitmap response) {


                        FileOutputStream fOut = null;
                        String fileName = currentDateandTime + ".jpeg";
                        try {
                            fOut = openFileOutput(fileName, Context.MODE_PRIVATE);
                            response.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                            fOut.flush();
                            fOut.close();
                            System.out.println("saveImagetoDisk save image to disk");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }



                    }
                }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {
                });

                queue.add(imgReq);

                images.add(bearImage);
                myRecyclerView.setAdapter(myAdapter);


                // the snackbar
                Snackbar.make(binding.getRoot(), getString(R.string.generated) + width + "*" + height, Snackbar.LENGTH_LONG)

                        .show();

                editor.putString("width", width);
                editor.putString("height", height);
                editor.apply();
            });

            builder.setNegativeButton(getString(R.string.no), (dialog2, click2) -> {
            });
            builder.create().show();

            myAdapter.notifyDataSetChanged();

        });

        myRecyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<BearActivity.MyRowHolder>() {

            /**
             * create RowHolder( for recyclerview)
             * @param parent
             * @return
             */
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                BearDetailsLayoutBinding  binding =
                        BearDetailsLayoutBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(binding.getRoot());

            }


            /**
             * this function is to bind the view holder
             * @param holder   The ViewHolder which should be updated to represent the contents of the
             *                 item at the given position in the data set.
             * @param position The position of the item within the adapter's data set.
             */
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                //this initializes a row
                BearImage bearImage = images.get(position);
                holder.sizeTextView.setText(bearImage.width.toString() + " * " + bearImage.height.toString());
                holder.timeTextView.setText(bearImage.timeGenerated);
                //holder.bearListImageView.set

                Bitmap savedImage = null ;
                String fileName = bearImage.timeGenerated + ".jpeg";
                //load image file from device start
                Log.d("*onCreate, open file *", getFilesDir() + File.separator + fileName);
                File file = new File( getFilesDir() + File.separator + fileName);

                if(file.exists()){
                    savedImage = BitmapFactory.decodeFile(file.getAbsolutePath());
                } else {
                    Log.d("*onCreate, open file *", "File does not exist");
                }
                //load image file from device end

                if (savedImage != null) {
                    // show image to imageview
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(savedImage, 150, 150, true);
                    holder.image.setImageBitmap(resizedBitmap);
                }
            }

            @Override
            public int getItemCount() {
                return images.size();
            }
        });
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));



    }//end of oncreate

    /**
     * this is the row holder class, it is the inner class
     */

    class MyRowHolder extends RecyclerView.ViewHolder {

        /**
         * declare image view
         */
        ImageView image;

        /**
         *  declare size view
         */
        TextView sizeTextView;

        /**
         * declare time view
         */
        TextView timeTextView;


        /**
         * this is the detail of row holder
         * @param itemView
         */
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageView);
            timeTextView=itemView.findViewById(R.id.textViewTime);
            sizeTextView=itemView.findViewById(R.id.textViewSize);

            itemView.setOnLongClickListener(click -> {

                int position = getAbsoluteAdapterPosition();
                BearImage selected = images.get(position);
                selectedImage.setValue(selected);
                return true;
            });

            itemView.setOnClickListener(clk1-> {

                int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(BearActivity.this);

                BearImage selected = images.get(position);

                builder.setMessage(getString(R.string.delete)
                                + selected.width + " * " + selected.height )
                        .setTitle(getString(R.string.Question))
                        .setNegativeButton(getString(R.string.no), (dialog, cl) -> {
                        })
                        //delete the message if Yes is clicked.
                        .setPositiveButton(getString(R.string.yes), (dialog, cl) -> {

                            Executor thread = Executors.newSingleThreadExecutor();
                            //run on a second thread
                            thread.execute(() -> {
                                //delete message from the database
                                myDAO.deleteImage(selected);
                            });
                            //delete the message from the array list
                            images.remove(position);             //remove the message from the arraylist
                            myAdapter.notifyItemRemoved(position);
                        }).create().show();
            });



        }
    }


    /**
     * this is the menu.
     * @param menu The options menu in which you place your items.
     *
     * @return  return a boolean
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    /**
     * this is the toolbar content
     * @param item The menu item that was selected.
     *
     * @return return a boolean
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        AlertDialog.Builder builder = new AlertDialog.Builder(BearActivity.this);

        if (item.getItemId() == R.id.help) {
            builder.setTitle(getString(R.string.help_navi));
            builder.setMessage(getString(R.string.help_bear));
            builder.setPositiveButton(getString(R.string.ok), ((dialog, click) -> {
            }));
            builder.setNegativeButton("", ((dialog, click) -> {
            }));
            builder.create().show();
        } else if (item.getItemId() == R.id.aviation) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes), ((dialog, click) -> {
                Intent aviationPage = new Intent(BearActivity.this, AviationActivity.class);
                startActivity(aviationPage);
            }));
            builder.setNegativeButton(getString(R.string.no), ((dialog, click) -> {
            }));
            builder.create().show();
        } else if (item.getItemId() == R.id.quiz) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes), ((dialog, click) -> {
                Intent triviaPage = new Intent(BearActivity.this, TriviaActivity.class);
                startActivity(triviaPage);
            }));
            builder.setNegativeButton(getString(R.string.no), ((dialog, click) -> {
            }));
            builder.create().show();
        } else if (item.getItemId() == R.id.bear) {
            Toast.makeText(this, getString(R.string.current_page), Toast.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.currency) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes), ((dialog, click) -> {
                Intent bearPage = new Intent(BearActivity.this, CurrencyActivity.class);
                startActivity(bearPage);
            }));
            builder.setNegativeButton(getString(R.string.no), ((dialog, click) -> {
            }));
            builder.create().show();
        } else if (item.getItemId() == R.id.contact) {
            Snackbar.make(binding.getRoot(), getString(R.string.contact_msg), Snackbar.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.about) {
            Toast.makeText(this, getString(R.string.about_msg), Toast.LENGTH_LONG).show();
        }

        return true;
    }

}


