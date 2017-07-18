package ru.ksu.motygullin.yatambyl.adapters;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import ru.ksu.motygullin.yatambyl.R;
import ru.ksu.motygullin.yatambyl.entites.Artwork;
import ru.ksu.motygullin.yatambyl.entites.ProfileModel;
import ru.ksu.motygullin.yatambyl.entites.Track;


public class ArtworkRecyclerViewAdapter extends RecyclerView.Adapter<ArtworkRecyclerViewAdapter.ArtworkViewHolder> {

    private ProfileModel profileModel;

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
    public void onBindViewHolder(ArtworkViewHolder holder, int position) {

        if (profileModel!=null && !profileModel.getTracks().isEmpty()) {
            final Track track = profileModel.getTracks().get(position);

            holder.rating.setText(String.valueOf(track.getRating()));


            for (Artwork artwork : profileModel.getArtworks()) {
                if (track.getArtworkId() == artwork.getId()) {
                    holder.author.setText(artwork.getAuthor());
                    holder.name.setText("'"+artwork.getName()+"'");

                }
            }
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
