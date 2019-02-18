package root.iv.imageeditor.image;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import root.iv.imageeditor.R;
import root.iv.imageeditor.util.GlideApp;
import root.iv.imageeditor.util.Subscribed;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>
        implements
        Subscribed<View.OnClickListener>,
        BaseAdapter<Album> {
    private List<Album> listAlbums;
    private LayoutInflater inflater;
    private View.OnClickListener listener;

    public AlbumAdapter(List<Album> albums, LayoutInflater inflater) {
        this.listAlbums = albums;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.album_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(listAlbums.get(position));
    }

    @Override
    public int getItemCount() {
        return listAlbums.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView viewCover;
        private TextView viewName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            viewCover = itemView.findViewById(R.id.viewImageCover);
            viewName = itemView.findViewById(R.id.viewAlbumName);
            itemView.setOnClickListener(listener);
        }

        public void bind(Album album) {
            viewName.setText(album.getName());
            GlideApp.with(viewCover.getContext().getApplicationContext())
                    .load("file:" + album.getCoverUri())
                    .centerCrop()
                    .placeholder(new ColorDrawable(Color.WHITE))
                    .into(viewCover);
        }
    }

    @Override
    public void subscribe(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void unsibscribe() {
        listener = null;
    }

    @Override
    public void append(Album item) {
        int count = listAlbums.size();
        listAlbums.add(item);
        notifyItemInserted(count);
    }

    @Override
    public Album getItem(int pos) {
        return listAlbums.get(pos);
    }

    @Override
    public void clear() {
        int count = listAlbums.size();
        listAlbums.clear();
        notifyItemRangeRemoved(0, count);
    }
}
