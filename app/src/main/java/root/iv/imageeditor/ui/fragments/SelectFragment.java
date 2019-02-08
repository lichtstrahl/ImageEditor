package root.iv.imageeditor.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.LinkedList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import root.iv.imageeditor.R;
import root.iv.imageeditor.image.Album;
import root.iv.imageeditor.image.AlbumAdapter;
import root.iv.imageeditor.image.Image;
import root.iv.imageeditor.image.ImageAdapter;
import root.iv.imageeditor.util.ImagesLoader;

public class SelectFragment extends Fragment {
    @BindView(R.id.viewListAlbums)
    RecyclerView viewListAlbums;
    @BindView(R.id.viewListImage)
    RecyclerView viewListImage;
    private AlbumAdapter albumAdapter;
    private ImageAdapter imageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select, container, false);
        ButterKnife.bind(this, view);

        albumAdapter = new AlbumAdapter(ImagesLoader.getImageAlbums(this.getActivity()), this.getActivity().getLayoutInflater());
        viewListAlbums.setAdapter(albumAdapter);
        viewListAlbums.setLayoutManager(new LinearLayoutManager(this.getActivity(),RecyclerView.HORIZONTAL, false));

        imageAdapter = new ImageAdapter(new LinkedList<>(), this.getActivity().getLayoutInflater());
        viewListImage.setAdapter(imageAdapter);
        viewListImage.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));

        return view;
    }

    public static SelectFragment getInstance() {
        SelectFragment fragment = new SelectFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        albumAdapter.subscribe(view -> {
            int pos = viewListAlbums.getChildAdapterPosition(view);
            Album album = albumAdapter.getItem(pos);
            imageAdapter.clear();
            for (Image image : album.getImages()) {
                imageAdapter.append(image);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        albumAdapter.unsibscribe();
    }
}
