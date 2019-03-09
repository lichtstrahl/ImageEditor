package root.iv.imageeditor.ui.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.bottomappbar.BottomAppBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rapid.decoder.BitmapDecoder;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import root.iv.imageeditor.R;
import root.iv.imageeditor.app.App;
import root.iv.imageeditor.util.GlideApp;
import root.iv.imageeditor.util.ReactiveImageHolder;
import root.iv.imageeditor.util.StdObserver;

public class EditFragment extends Fragment {
    public static final String TAG = "EditFragment";
    private static final String ARG_BITMAP_PATH = "args:bitmap_path";
    private static final String SAVE_IMG_PATH = "save:image_path";
    private static final String SAVE_IMAGE_HOLDER = "save:holder";
    @BindView(R.id.preview)
    ImageView preview;
    private String path;
    @BindView(R.id.bottomAppBar)
    BottomAppBar bottomAppBar;
    @Nullable
    private ReactiveImageHolder holder = null;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    private StdObserver<ReactiveImageHolder> createHolderObserver = new StdObserver<>(this::successfulHolderCreate, this::stdError);
    private StdObserver<Bitmap> workObserver = new StdObserver<>(this::successfulWorkFinish, this::stdError);

    @OnClick(R.id.buttonAction)
    public void clickAction() {
        progressBar.setVisibility(View.VISIBLE);
        if (holder != null) {
            holder.brightness(0.5)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(workObserver);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        Activity activity = this.getActivity();
        if (activity instanceof AppCompatActivity) {
            App.logI("Create edit: " + args);
            if (args != null) { // Было передано изображение
                path = ARG_BITMAP_PATH;

                Bitmap bitmap = BitmapDecoder.from(args.getString(ARG_BITMAP_PATH)).decode();
                progressBar.setVisibility(View.VISIBLE);
                Single.fromCallable(() -> ReactiveImageHolder.getInstance(bitmap))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(createHolderObserver);
                GlideApp
                        .with(activity.getApplicationContext())
                        .load(bitmap)
                        .into(preview);

            } else {            // Не было передано изображение
                progressBar.setVisibility(View.GONE);
                path = savedInstanceState != null ? savedInstanceState.getString(SAVE_IMG_PATH) : null;
                holder = savedInstanceState != null ? (ReactiveImageHolder)savedInstanceState.getSerializable(SAVE_IMAGE_HOLDER): null;
                GlideApp
                        .with(activity.getApplicationContext())
                        .load(path != null ? BitmapDecoder.from(Uri.parse(path)).decode() : R.drawable.ic_broken_image)
                        .into(preview);
            }

            ((AppCompatActivity) activity).setSupportActionBar(bottomAppBar);
        }

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        createHolderObserver.unsubscribe();
        workObserver.unsubscribe();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_IMG_PATH, path);
        outState.putSerializable(SAVE_IMAGE_HOLDER, holder);
        App.logI(TAG + " save");
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

    private void successfulHolderCreate(ReactiveImageHolder holder) {
        progressBar.setVisibility(View.GONE);
        this.holder = holder;
    }

    private void successfulWorkFinish(Bitmap bitmap) {
        progressBar.setVisibility(View.GONE);
        GlideApp
                .with(this.getActivity().getApplicationContext())
                .load(bitmap)
                .into(preview);
    }

    private void stdError(Throwable t) {
        progressBar.setVisibility(View.GONE);
        App.logE(t.getMessage());
    }
}
