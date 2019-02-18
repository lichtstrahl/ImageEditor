package root.iv.imageeditor.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import root.iv.imageeditor.R;
import root.iv.imageeditor.app.App;
import root.iv.imageeditor.image.Album;
import root.iv.imageeditor.image.AlbumAdapter;
import root.iv.imageeditor.image.Image;
import root.iv.imageeditor.image.ImageAdapter;
import root.iv.imageeditor.util.ImagesLoader;

public class SelectFragment extends Fragment {
    private static final String SAVE_POS_ALBUM_SELECTED = "save:pos-last-album-selected";
    private static final int BAD_INDEX = -1;
    public static final String TAG = "SelectFragment";
    @BindView(R.id.viewListAlbums)
    RecyclerView viewListAlbums;
    @BindView(R.id.viewListImage)
    RecyclerView viewListImage;
    private AlbumAdapter albumAdapter;
    private ImageAdapter imageAdapter;
    @Nullable
    private Listener mainListener;

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

        Bundle bundle = getArguments();
        if (bundle != null) {
            int pos = bundle.getInt(SAVE_POS_ALBUM_SELECTED, BAD_INDEX);
            if (pos != BAD_INDEX) {
                loadImage(albumAdapter.getItem(pos));
            }
        }

        return view;
    }

    public static Fragment getInstance() {
        SelectFragment fragment = new SelectFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        albumAdapter.subscribe(view -> {
            int pos = viewListAlbums.getChildAdapterPosition(view);
            getArguments().putInt(SAVE_POS_ALBUM_SELECTED, pos);
            loadImage(albumAdapter.getItem(pos));
        });

        imageAdapter.subscribe(view -> {
            int pos = viewListImage.getChildAdapterPosition(view);
            Image image = imageAdapter.getItem(pos);
            if (mainListener != null) {
                mainListener.openEditFragment(image.getPhotoUri());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainListener = (Listener)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        albumAdapter.unsibscribe();
        imageAdapter.unsibscribe();

        App.logI(TAG + " stop");
    }

    private void loadImage(Album album) {
        imageAdapter.clear();
        for (Image image : album.getImages()) {
            imageAdapter.append(image);
        }
    }

    public interface Listener {
        void openEditFragment(String bitmapPath);
    }
}
