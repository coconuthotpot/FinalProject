package algonquin.cst2335.finalproject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class AppViewModel extends ViewModel {

    // used to keep data when turning device orientation
    public MutableLiveData<ArrayList<CurrencyTransaction>> currencyTransactions = new MutableLiveData<>();
}
