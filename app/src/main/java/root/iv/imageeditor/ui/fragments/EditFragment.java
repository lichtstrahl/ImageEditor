package root.iv.imageeditor.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
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
import rapid.decoder.BitmapDecoder;
import root.iv.imageeditor.R;
import root.iv.imageeditor.util.GlideApp;

public class EditFragment extends Fragment {
    public static final String TAG = "EditFragment";
    private static final String ARG_BITMAP_PATH = "args:bitmap_path";
    @BindView(R.id.preview)
    ImageView preview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if (args != null) { // Было передано изображение
            GlideApp
                    .with(this.getActivity())
                    .load(new File(args.getString(ARG_BITMAP_PATH)))
                    .into(preview);
        } else {            // Не было изображения
            GlideApp
                    .with(this.getActivity())
                    .load(R.drawable.ic_broken_image)
                    .into(preview);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private Bitmap parseArgs(@NonNull Bundle args) {
        Bitmap bitmap =  BitmapDecoder.from(args.getString(ARG_BITMAP_PATH)).decode();

        return bitmap;
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
