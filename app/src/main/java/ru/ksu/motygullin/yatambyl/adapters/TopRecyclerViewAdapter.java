package ru.ksu.motygullin.yatambyl.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ksu.motygullin.yatambyl.R;
import ru.ksu.motygullin.yatambyl.entites.PostModel;
import ru.ksu.motygullin.yatambyl.entites.TopModel;
import ru.ksu.motygullin.yatambyl.entites.Track;
import ru.ksu.motygullin.yatambyl.service.Api;
import ru.ksu.motygullin.yatambyl.service.YaTamBylAPI;

public class TopRecyclerViewAdapter extends RecyclerView.Adapter<TopRecyclerViewAdapter.ArtworkViewHolder> {

    Context context;
    int pos = 0;
    Boolean isPaused = false;
    private TopModel topModel;

    public TopRecyclerViewAdapter(Context context, TopModel topModel) {
        this.topModel = topModel;
        this.context = context;
    }

    @Override
    public TopRecyclerViewAdapter.ArtworkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recording_item, parent, false);
        return new TopRecyclerViewAdapter.ArtworkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TopRecyclerViewAdapter.ArtworkViewHolder holder, final int position) {

        if (topModel != null && !topModel.getTracks().isEmpty()) {
            final Track track = topModel.getTracks().get(position);

            holder.rating.setText(String.valueOf(track.getRating()));
            holder.author.setText(topModel.getUsers().get(position).getName());

            final MediaPlayer player = new MediaPlayer();


            holder.upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!topModel.getLikes().get(position)) {
                        YaTamBylAPI api = Api.getInstance().getApi();
                        Call<PostModel> call = api.likeTrack(FirebaseAuth.getInstance().getCurrentUser().getUid(), track.getId());
                        call.enqueue(new Callback<PostModel>() {
                            @Override
                            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                                if (response.body().getSuccess()) {
                                    Toast.makeText(context, "Вы проголосовали", Toast.LENGTH_SHORT).show();
                                    holder.upvote.setVisibility(View.INVISIBLE);
                                    holder.rating.setVisibility(View.VISIBLE);
                                } else {
                                    Toast.makeText(context, "Невозможно произвести данное действие", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<PostModel> call, Throwable t) {
                                Toast.makeText(context, "Невозможно произвести данное действие", Toast.LENGTH_SHORT).show();

                            }
                        });


                    } else {
                        Toast.makeText(context, "Невозможно произвести данное действие", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.play.setClickable(false);

                    if (isPaused) {

                        player.seekTo(pos);
                        player.start();
                        holder.play.setVisibility(View.INVISIBLE);
                        holder.pause.setVisibility(View.VISIBLE);
                    } else {
                        holder.play.setVisibility(View.INVISIBLE);
                        holder.pause.setVisibility(View.VISIBLE);

                        try {
                            player.setDataSource(track.getLink());
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.start();
                    }
                }
            });

            holder.pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.play.setClickable(true);
                    holder.play.setVisibility(View.VISIBLE);
                    holder.pause.setVisibility(View.INVISIBLE);
                    isPaused = player.isPlaying();
                    player.pause();
                    pos = player.getCurrentPosition();

                }
            });

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    holder.play.setVisibility(View.VISIBLE);
                    holder.pause.setVisibility(View.INVISIBLE);
                    player.stop();
                    player.reset();
                }
            });

        }
    }

    @Override
    public int getItemCount() {

        if (topModel == null) {
            return 0;
        } else
            return topModel.getTracks().size();
    }

    public void setModel(TopModel topModel) {
        this.topModel = topModel;
        notifyDataSetChanged();
    }

    class ArtworkViewHolder extends RecyclerView.ViewHolder {

        FloatingActionButton play;
        FloatingActionButton pause;
        ImageButton upvote;
        TextView rating;
        TextView author;
        TextView name;
        View view;

        ArtworkViewHolder(View itemView) {
            super(itemView);

            rating = (TextView) itemView.findViewById(R.id.rating);
            rating.setVisibility(View.INVISIBLE);
            author = (TextView) itemView.findViewById(R.id.author);
            name = (TextView) itemView.findViewById(R.id.name);
            upvote = (ImageButton) itemView.findViewById(R.id.i_button);
            upvote.setVisibility(View.VISIBLE);
            play = (FloatingActionButton) itemView.findViewById(R.id.play);
            pause = (FloatingActionButton) itemView.findViewById(R.id.pause);
            view = itemView;
        }
    }
}