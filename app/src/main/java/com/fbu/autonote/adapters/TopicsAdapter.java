package com.fbu.autonote.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.fbu.autonote.R;
import com.fbu.autonote.activities.NotesExploreActivity;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @about manages the buttons on main screen that display the topics of notes found in the user's firebase storage
 */
public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.ViewHolder> {
    Context context;
    List<String> topics;

    public TopicsAdapter(Context context) {
        this.context = context;
        topics = new ArrayList<>();
    }

    @NonNull
    @NotNull
    @Override
    public TopicsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_topic_btn, parent, false);
        return new ViewHolder(view);
    }

    public void addToContainer(String item) {
        topics.add(item);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TopicsAdapter.ViewHolder holder, int position) {
        String topicStr = topics.get(position);
        holder.bind(topicStr);
    }

    public void clearContainer() {
        topics.clear();
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialButton btnTopicBtn;
        LinearLayout topicLayout;
        TextView tvTopicLabel;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            topicLayout = itemView.findViewById(R.id.topicLinearLayout);
            tvTopicLabel = itemView.findViewById(R.id.tvTopicLabel);
            topicLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    goToNoteExploreAct(topics.get(position));
                }
            });
        }

        public void bind(String topicStr) {
            //Get color code by string hash
            String buttonColor = String.format("#%X", topicStr.hashCode());
            int colorInt = Color.parseColor(buttonColor);
            topicLayout.setBackgroundColor(colorInt);
            tvTopicLabel.setText(topicStr);
        }

        private void goToNoteExploreAct(String topic) {
            Intent intent = new Intent(context, NotesExploreActivity.class);
            intent.putExtra("topic", topic);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context);
            context.startActivity(intent, options.toBundle());
        }
    }
}
