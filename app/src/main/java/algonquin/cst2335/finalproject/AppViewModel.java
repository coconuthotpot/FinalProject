package algonquin.cst2335.finalproject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * Class to keep data when change device's orientation
 * and get data from recycler view to fragment view
 * @author Ka Yan Ieong
 * @version 1.0
 */
public class AppViewModel extends ViewModel {

    /**
     * used to keep data in recycler view when turning device orientation by creating a mutablelivedata object
     * variable currencyTransactions is an arraylist for currency module usage
     */
    public MutableLiveData<ArrayList<CurrencyTransaction>> currencyTransactions = new MutableLiveData<>();

    /**
     * used to keep data selected transfer from recycler view to fragment view
     * variable selectedCurTrans is an object for currency module usage
     */
    public MutableLiveData<CurrencyTransaction> selectedCurTrans = new MutableLiveData<>();
}
