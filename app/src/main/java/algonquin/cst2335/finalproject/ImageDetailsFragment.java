package algonquin.cst2335.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.File;

import algonquin.cst2335.finalproject.databinding.DetailBearBinding;

/**
 * this class set the detail of the fragment page
 */


public class ImageDetailsFragment extends Fragment {
    /**
     * declare selected bear image
     */
    BearImage selected;

    /**
     * this is the selected fragment
     * @param bi the bearimage object
     */
    public ImageDetailsFragment(BearImage bi){
        selected = bi;
    }


    /**
     * this is the view of fragment
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return return the binding of the root
     */
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        DetailBearBinding binding = DetailBearBinding.inflate(inflater);


        binding.widthText.setText(selected.width);
        binding.heightText.setText(selected.height);
        binding.timeText.setText( selected.timeGenerated );


        Bitmap bitmap =  null;


        String fileName =  selected.timeGenerated + ".jpeg";
        File file = new File( getContext().getFilesDir()  + File.separator + fileName);

        if(file.exists()){
            bitmap =  BitmapFactory.decodeFile(file.getAbsolutePath());
        } else {
            Log.d("*onCreate, open file *", "File does not exist");
        }

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, Integer.parseInt(selected.width), Integer.parseInt(selected.height) , true);
        binding.image.setImageBitmap(resizedBitmap);

        return binding.getRoot();



    }
}
