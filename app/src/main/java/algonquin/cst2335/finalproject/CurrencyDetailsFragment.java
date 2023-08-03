package algonquin.cst2335.finalproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.databinding.ActivityCurrencyTransLayoutBinding;

public class CurrencyDetailsFragment extends Fragment {

    CurrencyTransaction selected;

    public CurrencyDetailsFragment (CurrencyTransaction transaction) {
        selected = transaction;
    }

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
