package algonquin.cst2335.finalproject;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class AviationViewModel extends ViewModel {
    public MutableLiveData<FlightDetails> messages = new MutableLiveData<>();
    public MutableLiveData<FlightDetails> selectedMessage = new MutableLiveData< >();

}
