package algonquin.cst2335.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.databinding.ActivityCurrencyTransLayoutBinding;

/**
 * Class to implement fragment layout to display individual transaction details
 * @author Ka Yan Ieong
 * @version 1.0
 */
public class CurrencyDetailsFragment extends Fragment {

    /**
     * a CurrencyTransaction object that stored the selected transaction from recycler view
     */
    CurrencyTransaction selected;

    /**
     * Constructor to create an object under CurrencyDetailsFragment class
     * @param transaction an object from CurrencyTransaction class
     */
    public CurrencyDetailsFragment (CurrencyTransaction transaction) {
        selected = transaction;
    }

    /**
     * method to bind items from activityCurrencyTransLayoutBinding (fragment layout) to this class
     * and put the corresponding values to the fragment layout
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return bound items started from the root
     */
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ActivityCurrencyTransLayoutBinding binding = ActivityCurrencyTransLayoutBinding.inflate(inflater);

        binding.detailsID.setText( Long.toString(selected.getId()));
        binding.detailsCurFrom.setText(selected.getCurrencyFrom());
        binding.detailsAmtFrom.setText(selected.getAmountInput());
        binding.detailsCurTo.setText(selected.getCurrencyTo());
        binding.detailsAmtTo.setText(selected.getShowAmount());

        return binding.getRoot();
    }
}
