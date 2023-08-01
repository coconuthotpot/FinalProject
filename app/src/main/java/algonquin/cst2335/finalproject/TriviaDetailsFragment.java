package algonquin.cst2335.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.databinding.TriviaFragmentBinding;

public class TriviaDetailsFragment extends Fragment {
    Score selected;
    public  TriviaDetailsFragment(Score h){
        selected = h;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        TriviaFragmentBinding binding = TriviaFragmentBinding.inflate(inflater);
        binding.textItemID.setText("Id = " +String.valueOf(selected.getId()));
        //binding.textItemID.setText("Id = " + selected.id);
        binding.textItemName.setText("User = "+selected.getName());
//        binding.textItemScore.setText(selected.getScore());
        binding.textItemScore.setText("Score = " + String.valueOf(selected.getScore()));

        return binding.getRoot();
    }
}
