package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Currency;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.ActivityCurrencyBinding;
import algonquin.cst2335.finalproject.databinding.ActivityCurrencyDetailsBinding;

public class CurrencyActivity extends AppCompatActivity {

    ImageView convertButton, depositButton, withdrawButton;
    TextView showAmount;
    EditText currencyFrom, currencyTo, amountInput;
    ActivityCurrencyBinding currencyBinding;
    Set<String> currenciesList = CurrenciesList.currenciesList;
    RequestQueue queue = null;
    CurrencyTransactionDAO ctDAO;
    AppViewModel currencyModel;
    ArrayList<CurrencyTransaction> currencyTransactions = new ArrayList<>();
    private RecyclerView.Adapter myAdapter;
    ActivityCurrencyDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currencyBinding = ActivityCurrencyBinding.inflate(getLayoutInflater());
        setContentView(currencyBinding.getRoot());

        setSupportActionBar(currencyBinding.toolbar);

        queue = Volley.newRequestQueue(this);
        
        CurrencyTransactionDatabase ctdb = Room.databaseBuilder(getApplicationContext(), CurrencyTransactionDatabase.class, "CurrencyTransaction").build();
        ctDAO = ctdb.ctDAO();

        currencyModel = new ViewModelProvider(this).get(AppViewModel.class);
        currencyTransactions = currencyModel.currencyTransactions.getValue();

        // binding corresponding View items
        currencyFrom = currencyBinding.currencyFrom;
        currencyTo = currencyBinding.currencyTo;
        amountInput = currencyBinding.amountFrom;
        showAmount = currencyBinding.amountTo;

        if (currencyTransactions == null) {
            currencyModel.currencyTransactions.postValue(currencyTransactions = new ArrayList<>());
            Executor thread0 = Executors.newSingleThreadExecutor();
            thread0.execute(() ->
            {
                currencyTransactions.addAll( ctDAO.getAllTransactions() ); // get the data from database
                runOnUiThread( () -> currencyBinding.currencyRecycler.setAdapter( myAdapter ));
            });
        }

        // part to send data to recyclerview
        currencyBinding.currencyRecycler.setLayoutManager(new LinearLayoutManager(this));
        currencyBinding.currencyRecycler.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                binding = ActivityCurrencyDetailsBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                CurrencyTransaction trans = currencyTransactions.get(position);
                holder.curFromDetails.setText("");
                holder.amtToDetails.setText("");
                holder.curFromDetails.setText("");
                holder.amtToDetails.setText("");
            }

            @Override
            public int getItemCount() {
                return currencyTransactions.size();
            }
        });

        // get the saved values in shared preferences
        SharedPreferences prefs = getSharedPreferences("MyCurrencyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        prefs.getString("CurrencyFrom", "");
        prefs.getString("CurrencyTo", "");
        prefs.getString("AmountFrom", "");

        // set the saved shared preferences to show up while reopening the app
        currencyFrom.setText(prefs.getString("CurrencyFrom", ""));
        currencyTo.setText(prefs.getString("CurrencyTo", ""));
        amountInput.setText(prefs.getString("AmountFrom",""));

        // binding corresponding buttons
        depositButton = currencyBinding.deposit;
        withdrawButton = currencyBinding.withdraw;
        convertButton = currencyBinding.convertButtonImage;

        /*depositButton.setOnClickListener( click -> {
            String curFrom = currencyFrom.getText().toString();
            if (checkCurrency(curFrom) == false) {
                Snackbar.make(currencyFrom, "Please input a correct original currency", Snackbar.LENGTH_LONG).show();
            } else {
                editor.putString("CurrencyFrom", curFrom);
                editor.apply();
            }

        });*/

        /*withdrawButton.setOnClickListener( click -> {
            String curTo = currencyTo.getText().toString();
            if (checkCurrency(curTo) == false) {
                Snackbar.make(currencyTo, "Please input a correct target currency", Snackbar.LENGTH_LONG).show();
            } else {
                editor.putString("CurrencyTo", curTo);
                editor.apply();
            }
        });*/

        // set the retrieved value from API to show on screen
        convertButton.setOnClickListener( click -> {
            // check the input currency code before passing the data to connect API
            String curFrom = currencyFrom.getText().toString().toUpperCase();
            if (checkCurrency(curFrom) == false) {
                Snackbar.make(currencyFrom, "Please input a correct original currency", Snackbar.LENGTH_LONG).show();
            } else {
                editor.putString("CurrencyFrom", curFrom);
                editor.apply();
            }

            // check the input currency code before passing the data to connect API
            String curTo = currencyTo.getText().toString().toUpperCase();
            if (checkCurrency(curTo) == false) {
                Snackbar.make(currencyTo, "Please input a correct target currency", Snackbar.LENGTH_LONG).show();
            } else {
                editor.putString("CurrencyTo", curTo);
                editor.apply();
            }

            String amtFrom = amountInput.getText().toString();
            editor.putString("AmountFrom", amtFrom);
            editor.apply();

            // start to retrieve data through API
            String apiKey = "c5172e86dcc4fa44ea0e08ff0d4c8a2522995839";

            String url = null;
            try {
                url = "https://api.getgeoapi.com/v2/currency/convert?api_key=" + apiKey +
                        "&from=" + curFrom +
                        "&to=" + curTo +
                        "&amount=" + amtFrom +
                        "&format=json";
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {
                        try {
                            JSONObject rates = response.getJSONObject("rates");
                            JSONObject curFromInput = rates.getJSONObject(curTo);
                            String amtTo = curFromInput.getString("rate_for_amount");

                            showAmount.setText(amtTo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    (error) -> {});

            queue.add(request);

            CurrencyTransaction transaction = new CurrencyTransaction(curFrom, curTo, amtFrom, showAmount.getText().toString());
            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute( () -> {
                transaction.id = ctDAO.insertTransaction(transaction);});
            currencyTransactions.add(transaction);
            myAdapter.notifyItemInserted(currencyTransactions.size()-1);
        });


    }

    private boolean checkCurrency(String currency) {
        String convertedCurrency = currency.toUpperCase();

        if (currenciesList.contains(convertedCurrency)){
            return true;
        } else {
            return false;
        }
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView curFromDetails, curToDetails, amtFromDetails, amtToDetails;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            curFromDetails = itemView.findViewById(R.id.currencyFromDetails);
            curToDetails = itemView.findViewById(R.id.currencyToDetails);
            amtFromDetails = itemView.findViewById(R.id.amountFromDetails);
            amtToDetails = itemView.findViewById(R.id.amountToDetails);

            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();

                AlertDialog.Builder builder = new AlertDialog.Builder( CurrencyActivity.this );
                builder.setTitle("Question:");
                builder.setMessage("Delete this message?: "  + curFromDetails.getText());

                builder.setPositiveButton("Yes", ((dialog, clk) ->{
                    CurrencyTransaction removedMsg = currencyTransactions.get(position);

                    Executor thread2 = Executors.newSingleThreadExecutor();
                    thread2.execute( () -> {
                        ctDAO.deleteTransaction(removedMsg);}
                    );

                    currencyTransactions.remove(position);
                    myAdapter.notifyItemRemoved(position);

                    Snackbar.make(curFromDetails, "You deleted message#" + position, Snackbar.LENGTH_LONG)
                            .setAction("Undo", check -> {
                                currencyTransactions.add(position, removedMsg);
                                myAdapter.notifyItemInserted(position);
                            })
                            .show();

                } ));

                builder.setNegativeButton("No", ((dialog, clk) ->{} ));

                builder.create().show();
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyActivity.this);

        if (item.getItemId() == R.id.help) {
            builder.setTitle(getString(R.string.help_navi));
            builder.setMessage(getString(R.string.help_currency));
            builder.setPositiveButton("OK", ((dialog, click) -> {}));
            builder.setNegativeButton("", ((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.aviation) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent aviationPage = new Intent(CurrencyActivity.this, AviationActivity.class);
                startActivity(aviationPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.quiz) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent triviaPage = new Intent(CurrencyActivity.this, TriviaActivity.class);
                startActivity(triviaPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.currency) {
            Toast.makeText(this, getString(R.string.current_page), Toast.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.bear) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent bearPage = new Intent(CurrencyActivity.this, BearActivity.class);
                startActivity(bearPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.contact) {
            Snackbar.make(showAmount, getString(R.string.contact_msg), Snackbar.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.about) {
            Toast.makeText(this, getString(R.string.about_msg), Toast.LENGTH_LONG).show();
        }

        return true;
    }

}