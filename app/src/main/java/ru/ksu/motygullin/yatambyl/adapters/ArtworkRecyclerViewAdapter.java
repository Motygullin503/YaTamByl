package ru.ksu.motygullin.yatambyl.adapters;

import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

import ru.ksu.motygullin.yatambyl.R;
import ru.ksu.motygullin.yatambyl.entites.Artwork;
import ru.ksu.motygullin.yatambyl.entites.ProfileModel;
import ru.ksu.motygullin.yatambyl.entites.Track;


public class ArtworkRecyclerViewAdapter extends RecyclerView.Adapter<ArtworkRecyclerViewAdapter.ArtworkViewHolder> {

    private ProfileModel profileModel;
    int pos = 0;
    Boolean isPaused = false;

    public ArtworkRecyclerViewAdapter(ProfileModel profileModel) {
        this.profileModel = profileModel;
    }

    @Override
    public ArtworkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recording_item, parent, false);
        return new ArtworkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ArtworkViewHolder holder, int position) {

        if (profileModel!=null && !profileModel.getTracks().isEmpty()) {
            final Track track = profileModel.getTracks().get(position);

            holder.rating.setText(String.valueOf(track.getRating()));

            final MediaPlayer player = new MediaPlayer();


            for (Artwork artwork : profileModel.getArtworks()) {
                if (track.getArtworkId() == artwork.getId()) {
                    holder.author.setText(artwork.getAuthor());
                    holder.name.setText("'"+artwork.getName()+"'");

                }
            }

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

        if (profileModel==null){
            return 1;
        } else
        return profileModel.getTracks().size();
    }

    public void setModel(ProfileModel profileModel) {
        this.profileModel = profileModel;
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
            author = (TextView) itemView.findViewById(R.id.author);
            name = (TextView) itemView.findViewById(R.id.name);
            upvote = (ImageButton) itemView.findViewById(R.id.i_button);
            play = (FloatingActionButton) itemView.findViewById(R.id.play);
            pause = (FloatingActionButton) itemView.findViewById(R.id.pause);
            view = itemView;
        }
    }
}
