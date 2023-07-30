package algonquin.cst2335.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HighScoresAdapter extends RecyclerView.Adapter<HighScoresAdapter.ScoreViewHolder> {

    private List<Score> highScoresList;

    public HighScoresAdapter(List<Score> highScoresList) {
        this.highScoresList = highScoresList;
    }

    @NonNull
    @Override
    public HighScoresAdapter.ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HighScoresAdapter.ScoreViewHolder holder, int position) {
        Score score = highScoresList.get(position);
        holder.nameTextView.setText(score.getName());
        holder.scoreTextView.setText(String.valueOf(score.getScore()));
    }

    @Override
    public int getItemCount() {
        return highScoresList.size();
    }
    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView scoreTextView;

        ScoreViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            scoreTextView = itemView.findViewById(R.id.textViewScore);
        }
    }
}
