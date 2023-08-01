package algonquin.cst2335.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * An adapter class for displaying a list of TriviaScores in a RecyclerView.
 * This adapter handles the creation of RecyclerView items and binds data to the item views.
 */
public class TriviaHighScoresAdapter extends RecyclerView.Adapter<TriviaHighScoresAdapter.ScoreViewHolder> {
    /**
     * The list of TriviaScore objects representing the high scores to be displayed.
     */
    private List<TriviaScore> highScoresList;

    /**
     * The click listener interface for handling item click events in the RecyclerView.
     */
    private OnItemClickListener itemClickListener;

    /**
     * Constructor for creating a new instance of TriviaHighScoresAdapter with the high scores list.
     * @param highScoresList The list of TriviaScore objects representing the high scores to be displayed.
     */
    public TriviaHighScoresAdapter(List<TriviaScore> highScoresList) {
        this.highScoresList = highScoresList;
    }

    public interface OnItemClickListener {
        void onItemClick(TriviaScore score);
    }

    /**
     * Constructor for creating a new instance of TriviaHighScoresAdapter with the high scores list and click listener.
     * @param highScoresList The list of TriviaScore objects representing the high scores to be displayed.
     * @param itemClickListener The OnItemClickListener for handling item click events in the RecyclerView.
     */
    public TriviaHighScoresAdapter(List<TriviaScore> highScoresList, OnItemClickListener itemClickListener) {
        this.highScoresList = highScoresList;
        this.itemClickListener = itemClickListener;
    }

    /**
     * Sets the list of high scores to be displayed by the adapter.
     * @param highScoresList The list of TriviaScore objects representing the high scores to be displayed.
     */
    public void setHighScoresList(List<TriviaScore> highScoresList) {
        this.highScoresList = highScoresList;
    }

    /**
     * Called when a new ScoreViewHolder is needed for creating a RecyclerView item.
     * @param parent The parent ViewGroup in which the item view will be created.
     * @param viewType The type of the view to be created (not used in this case).
     * @return The newly created ScoreViewHolder containing the item view.
     */
    @NonNull
    @Override
    public TriviaHighScoresAdapter.ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trivia_item_score, parent, false);
        return new ScoreViewHolder(view);
    }

    /**
     * Called when binding data to a ScoreViewHolder for a RecyclerView item.
     * @param holder The ScoreViewHolder containing the item view to be bound with data.
     * @param position The position of the item in the RecyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull TriviaHighScoresAdapter.ScoreViewHolder holder, int position) {
        TriviaScore score = highScoresList.get(position);
        holder.bind(score);
    }

    /**
     * Gets the total number of items in the RecyclerView.
     * @return The total number of high scores in the RecyclerView.
     */
    @Override
    public int getItemCount() {
        return highScoresList.size();
    }

    /**
     * The ViewHolder class representing each item in the RecyclerView.
     */
    class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView scoreTextView;

        /**
         * Constructor for creating a new instance of ScoreViewHolder for a RecyclerView item.
         * @param itemView The item view representing the RecyclerView item.
         */
        ScoreViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            scoreTextView = itemView.findViewById(R.id.textViewScore);

            /**
             * Set click listener for the entire item view
             */
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    TriviaScore score = highScoresList.get(position);
                    itemClickListener.onItemClick(score);
                }
            });
        }

        /**
         * Binds data to the ScoreViewHolder for a RecyclerView item.
         * @param score The TriviaScore object containing data to be displayed in the item view.
         */
        void bind(TriviaScore score) {
            nameTextView.setText(score.getName());
            scoreTextView.setText(String.valueOf(score.getScore()));
        }

        }
    }

