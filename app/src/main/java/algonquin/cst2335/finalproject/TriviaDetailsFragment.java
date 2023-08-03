package algonquin.cst2335.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.databinding.TriviaFragmentBinding;
/**
 * A fragment that displays the details of a TriviaScore object.
 * It shows the user's name, ID, and score in a UI layout.
 */
public class TriviaDetailsFragment extends Fragment {

    /**
     * The TriviaScore object representing the selected score details to be displayed.
     */
    TriviaScore selected;
    /**
     * Constructor for creating a new instance of TriviaDetailsFragment with the selected TriviaScore.
     * @param h The TriviaScore object representing the selected score details.
     */
    public  TriviaDetailsFragment(TriviaScore h){
        selected = h;
    }

    /**
     * Called when the fragment's view is created.
     * It inflates the layout for the fragment and sets up the UI to display the selected score details.
     * @param inflater The LayoutInflater used to inflate the layout.
     * @param container The parent ViewGroup in which the fragment UI will be displayed.
     * @param savedInstanceState The saved instance state of the fragment (not used in this case).
     * @return The root View of the fragment's UI layout.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        TriviaFragmentBinding binding = TriviaFragmentBinding.inflate(inflater);
        binding.textItemID.setText("The order of user answers is ID  " +String.valueOf(selected.getId()));
        binding.textItemName.setText("The user name is  "+selected.getName());
        binding.textItemScore.setText("The Score is  " + String.valueOf(selected.getScore()));
        return binding.getRoot();
    }
}
