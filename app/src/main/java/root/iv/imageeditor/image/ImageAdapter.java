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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import root.iv.imageeditor.R;
import root.iv.imageeditor.util.GlideApp;
import root.iv.imageeditor.util.Subscribed;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>
    implements
        Subscribed<View.OnClickListener>,
        BaseAdapter<Image> {
    private List<Image> imageList;
    private LayoutInflater inflater;
    @Nullable
    private View.OnClickListener listener;

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

    @Override
    public void subscribe(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void unsibscribe() {
        listener = null;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView viewImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewImage = itemView.findViewById(R.id.viewImage);
            if (listener != null) {
                itemView.setOnClickListener(listener);
            }
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
}
