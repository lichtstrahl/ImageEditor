package root.iv.imageeditor.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import root.iv.imageeditor.R;
import root.iv.imageeditor.app.App;
import root.iv.imageeditor.util.GlideApp;

public class EditFragment extends Fragment {
    public static final String TAG = "EditFragment";
    private static final String ARG_BITMAP_PATH = "args:bitmap_path";
    private static final String SAVE_IMG_PATH = "save:image_path";
    @BindView(R.id.preview)
    ImageView preview;
    private String path;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        App.logI("Create edit: " + args);
        if (args != null) { // Было передано изображение
            path = ARG_BITMAP_PATH;
            GlideApp
                    .with(this.getActivity())
                    .load(new File(args.getString(ARG_BITMAP_PATH)))
                    .into(preview);
        } else {            // Не было изображения
            path = savedInstanceState != null ? savedInstanceState.getString(SAVE_IMG_PATH) : null;
            GlideApp
                    .with(this.getActivity())
                    .load(path != null ? new File(path) : R.drawable.ic_broken_image)
                    .into(preview);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_IMG_PATH, path);
    }

    public static EditFragment getInstance(@Nullable String bitmapPath) {
        EditFragment fragment = new EditFragment();
        if (bitmapPath != null) {
            Bundle bundle = new Bundle();
            bundle.putString(ARG_BITMAP_PATH, bitmapPath);
            fragment.setArguments(bundle);
        }
        return fragment;
    }
}
