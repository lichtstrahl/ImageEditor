package root.iv.imageeditor.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import rapid.decoder.BitmapDecoder;
import root.iv.imageeditor.R;
import root.iv.imageeditor.util.GlideApp;

public class EditFragment extends Fragment {
    private static final String ARG_BITMAP_PATH = "args:bitmap_path";
    @BindView(R.id.preview)
    ImageView preview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if (args != null) {
            GlideApp.with(this.getActivity())
                    .load(parseArgs(args))
                    .centerCrop()
                    .into(preview);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private Bitmap parseArgs(@NonNull Bundle args) {
        return BitmapDecoder.from(args.getString(ARG_BITMAP_PATH)).decode();
    }

    public static EditFragment getInstance(String bitmapPath) {
        EditFragment fragment = new EditFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_BITMAP_PATH, bitmapPath);
        fragment.setArguments(bundle);

        return fragment;
    }
}
