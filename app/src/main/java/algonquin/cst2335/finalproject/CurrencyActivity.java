package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.Button;
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
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.ActivityCurrencyBinding;
import algonquin.cst2335.finalproject.databinding.ActivityCurrencyDetailsBinding;

/**
 * Class to implement Currency Converter logics
 * let user input currency info, amount to convert to retrieve converted amount from app
 * Also, user can save and retrieve record from previous conversions
 */
public class CurrencyActivity extends AppCompatActivity {

    /**
     * ImageView object for convert button
     */
    ImageView convertButton;

    /**
     * TextView object to show amount after conversion
     */
    TextView showAmount;

    /**
     * EditText objects to have value from user input
     */
    EditText currencyFrom, currencyTo, amountInput;

    /**
     * Button to trigger corresponding actions after clicking save or show button
     */
    Button save, show;

    /**
     * a binding object to connect currency module layout to this class to implement app logic
     */
    ActivityCurrencyBinding currencyBinding;

    /**
     * a Set Collection stored currency codes for input validation
     */
    Set<String> currenciesList = CurrenciesList.currenciesList;

    /**
     * Volley Queue object for API connection
     */
    RequestQueue queue = null;

    /**
     * database access object connected to CurrencyTransaction Table to implement database related actions
     */
    CurrencyTransactionDAO ctDAO;

    /**
     * a view model to hold mutable live data from changing device's orientation or send info from recycler view to fragment layout
     */
    AppViewModel currencyModel;

    /**
     * arraylist to store records retrieved from database
     */
    ArrayList<CurrencyTransaction> currencyTransactions = new ArrayList<>();

    /**
     * adapter to establish recycler view within the same layout
     */
    private RecyclerView.Adapter myAdapter;

    /**
     * binding object to bind currency details layout to recycler view within this class
     */
    ActivityCurrencyDetailsBinding binding;

    /**
     * method to launch Currency Converter module
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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

        currencyModel.currencyTransactions.postValue(currencyTransactions = new ArrayList<>());

        // part to send data to recyclerview
        currencyBinding.currencyRecycler.setLayoutManager(new LinearLayoutManager(this));

        // open the fragment view
        currencyModel.selectedCurTrans.observe(this, newTransValue -> {
            CurrencyDetailsFragment transFragment = new CurrencyDetailsFragment(newTransValue);

            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            tx.replace(R.id.transactionFrame, transFragment);
            tx.addToBackStack("Back to last transaction");
            tx.commit();
        });

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
                holder.idDetails.setText( Long.toString(trans.getId()) );
                holder.curFromDetails.setText(trans.getCurrencyFrom());
                holder.amtFromDetails.setText(trans.getAmountInput());
                holder.curToDetails.setText(trans.getCurrencyTo());
                holder.amtToDetails.setText(trans.getShowAmount());
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
        convertButton = currencyBinding.convertButtonImage;
        save = currencyBinding.saveTransaction;
        show = currencyBinding.showTransaction;


        // set the retrieved value from API to show on screen
        convertButton.setOnClickListener( click -> {
            // check the input currency code before passing the data to connect API
            String curFrom = currencyFrom.getText().toString().toUpperCase();
            if (checkCurrency(curFrom) == false) {
                Snackbar.make(currencyFrom, getString(R.string.inputCorrectCurFrom), Snackbar.LENGTH_LONG).show();
            } else {
                editor.putString("CurrencyFrom", curFrom);
                editor.apply();
            }

            // check the input currency code before passing the data to connect API
            String curTo = currencyTo.getText().toString().toUpperCase();
            if (checkCurrency(curTo) == false) {
                Snackbar.make(currencyTo, getString(R.string.inputCorrectCurTo), Snackbar.LENGTH_LONG).show();
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

        });

        // save the query
        save.setOnClickListener(click -> {
            String curFrom = currencyFrom.getText().toString().toUpperCase();
            String curTo = currencyTo.getText().toString().toUpperCase();
            String amtFrom = amountInput.getText().toString();

            CurrencyTransaction transaction = new CurrencyTransaction(curFrom, curTo, amtFrom, showAmount.getText().toString());
            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute( () -> {
                transaction.id = ctDAO.insertTransaction(transaction);});
            // message to notice user record is saved
            Toast.makeText(this, getString(R.string.successfulSave), Toast.LENGTH_LONG).show();
            //currencyTransactions.add(transaction);
            //myAdapter.notifyItemInserted(currencyTransactions.size()-1);
        });


        // to show all saved queries in recycler view
        show.setOnClickListener(click -> {
            // reset recycler view to blank
            currencyTransactions.clear();

            Executor thread0 = Executors.newSingleThreadExecutor();
            thread0.execute(() ->
            {
                currencyTransactions.addAll( ctDAO.getAllTransactions() ); // get the data from database
                runOnUiThread( () -> currencyBinding.currencyRecycler.setAdapter( myAdapter ));
            });

        });
    }

    /**
     * method to validate user input for the currency code
     * @param currency String input of the currency code
     * @return true if correct, false if incorrect
     */
    private boolean checkCurrency(String currency) {
        String convertedCurrency = currency.toUpperCase();

        if (currenciesList.contains(convertedCurrency)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * inner class to establish recycler view within the same layout
     */
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView idDetails, curFromDetails, curToDetails, amtFromDetails, amtToDetails;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            idDetails = itemView.findViewById(R.id.idDetails);
            curFromDetails = itemView.findViewById(R.id.currencyFromDetails);
            curToDetails = itemView.findViewById(R.id.currencyToDetails);
            amtFromDetails = itemView.findViewById(R.id.amountFromDetails);
            amtToDetails = itemView.findViewById(R.id.amountToDetails);

            // can delete or view clicked transaction individually
            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();

                AlertDialog.Builder builder = new AlertDialog.Builder( CurrencyActivity.this );
                builder.setTitle(getString(R.string.alertHeader));
                builder.setMessage(getString(R.string.alertQuestion));

                builder.setPositiveButton( getString(R.string.alertView), ((dialog, clk) ->{
                    CurrencyTransaction selected = currencyTransactions.get(position);
                    currencyModel.selectedCurTrans.postValue(selected);
                } ));

                builder.setNegativeButton( getString(R.string.alertDelete), ((dialog, clk) ->{
                    CurrencyTransaction removedMsg = currencyTransactions.get(position);

                    Executor thread2 = Executors.newSingleThreadExecutor();
                    thread2.execute( () -> {
                        ctDAO.deleteTransaction(removedMsg);}
                    );

                    currencyTransactions.remove(position);
                    myAdapter.notifyItemRemoved(position);

                    Snackbar.make(curFromDetails, getString(R.string.deleteMsgNotice) + position, Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.deleteMsgUndo), check -> {
                                currencyTransactions.add(position, removedMsg);
                                myAdapter.notifyItemInserted(position);
                            })
                            .show();

                } ));

                builder.create().show();

            });

        }
    }

    /**
     * method to show option bar on top of the app
     * @param menu The options menu in which you place your items.
     *
     * @return true to use the option bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    /**
     * method to implement option bar logic when user click on selected option
     * @param item The menu item that was selected.
     *
     * @return true to let user choose options in the option bar
     */
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