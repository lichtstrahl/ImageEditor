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

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>
    implements
        BaseAdapter<Image> {
    private List<Image> imageList;
    private LayoutInflater inflater;

    public ImageAdapter(List<Image> list, LayoutInflater inflater) {
        imageList = list;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView viewImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewImage = itemView.findViewById(R.id.viewImage);
        }

        public void bind(Image image) {
            GlideApp
                    .with(viewImage.getContext())
                    .load("file:" + image.getPhotoUri())
                    .centerCrop()
                    .placeholder(new ColorDrawable(Color.WHITE))
                    .into(viewImage);
        }
    }

    @Override
    public void append(Image item) {
        int count = imageList.size();
        imageList.add(item);
        notifyItemInserted(count);
    }

    @Override
    public Image getItem(int pos) {
        return imageList.get(pos);
    }

    @Override
    public void clear() {
        int count = imageList.size();
        imageList.clear();
        notifyItemRangeRemoved(0, count);
    }
}
