package root.iv.imageeditor.ui.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rapid.decoder.BitmapDecoder;
import root.iv.imageeditor.R;
import root.iv.imageeditor.app.App;
import root.iv.imageeditor.ui.ControlPanel;
import root.iv.imageeditor.glide.GlideApp;
import root.iv.imageeditor.holder.ReactiveImageHolder;
import root.iv.imageeditor.rx.StdObserver;

public class EditFragment extends Fragment {
    public static final String TAG = "EditFragment";
    private static final String ARG_BITMAP_PATH = "args:bitmap_path";
    private static final String SAVE_IMG_PATH = "save:image_path";
    private static final String SAVE_IMAGE_HOLDER = "save:holder";
    private static final String RECEIVER_ACTION = "receiver:action";
    private static final String RESULT_PIXELS = "result:pixels";
    @BindView(R.id.preview)
    ImageView preview;
    private String path;
    @BindView(R.id.bottomAppBar)
    BottomAppBar bottomAppBar;
    @Nullable
    private ReactiveImageHolder holder = null;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.dynamicLayout)
    ViewGroup dynamicLayout;
    private StdObserver<ReactiveImageHolder> createHolderObserver = new StdObserver<>(this::successfulHolderCreate, this::stdError);
    private StdObserver<Bitmap> workObserver = new StdObserver<>(this::successfulWorkFinish, this::stdError);
    private ControlPanel lightPanel;
    private ControlPanel contrastPanel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        ButterKnife.bind(this, view);

        preview.getViewTreeObserver().addOnDrawListener(() -> {
            App.logI("OnDraw");
        });

        Bundle args = getArguments();
        Activity activity = this.getActivity();
        if (activity instanceof AppCompatActivity) {
            App.logI("Create edit: " + args);
            if (args != null) { // Было передано изображение
                path = ARG_BITMAP_PATH;

                progressBar.setVisibility(View.VISIBLE);

                DisplayMetrics metrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int width = metrics.widthPixels;
                int height = metrics.heightPixels * 9 / 10;
                Single.fromCallable(() -> ReactiveImageHolder.getInstance(args.getString(ARG_BITMAP_PATH), width, height))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(createHolderObserver);

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
            setHasOptionsMenu(true);
            dynamicLayout.setAlpha(0.0f);
            lightPanel =    new ControlPanel(getLayoutInflater(), v -> applyLightPanel(), 0.0, 5.0, 0.1, 1.0);
            contrastPanel = new ControlPanel(getLayoutInflater(), v -> applyContrastPanel(), -1.0, 1.0, 0.1, 0.0);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (dynamicLayout.getAlpha() > 0.1) {
            lightPanel.hide();
            contrastPanel.hide();
            return true;
        }

        switch (item.getItemId()) {
            case R.id.menu_edit_light:
                lightPanel.show(dynamicLayout, "Яркость");
                break;

            case R.id.menu_edit_contrast:
                contrastPanel.show(dynamicLayout, "Контраст");
                break;
        }
        return true;
    }

    private void applyLightPanel() {
        Toast.makeText(getActivity(), String.format(Locale.ENGLISH, "%8.3f", lightPanel.getValue()), Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        if (holder != null) {
            holder.brightness(lightPanel.getValue())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(workObserver);
        }
        lightPanel.hide();
    }

    private void applyContrastPanel() {
        Toast.makeText(getActivity(), String.format(Locale.ENGLISH, "%8.3f", contrastPanel.getValue()), Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        if (holder != null) {
            holder.contrast(contrastPanel.getValue())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(workObserver);

        }

        contrastPanel.hide();
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
        GlideApp
                .with(getActivity().getApplicationContext())
                .load(holder.getCurrentBitmap())
                .into(preview);
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
