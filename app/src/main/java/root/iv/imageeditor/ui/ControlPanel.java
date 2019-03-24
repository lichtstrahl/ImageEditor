package root.iv.imageeditor.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import root.iv.imageeditor.R;
import root.iv.imageeditor.rx.Subscribed;
import root.iv.imageeditor.anim.AnimationManager;

public class ControlPanel implements Subscribed<Consumer<Integer>> {
    @BindView(R.id.seek)
    AppCompatSeekBar seekBar;
    @BindView(R.id.buttonOK)
    Button buttonOK;
    @BindView(R.id.viewSeek)
    TextView viewSeek;
    @BindView(R.id.viewTitle)
    TextView viewTitle;
    private PublishSubject<Integer> currentValue;
    @Nullable
    private CompositeDisposable disposable;
    private double min;
    private double max;
    private double step;
    private double value;
    private int init;
    private ViewGroup parent;
    private boolean visible;
    private LayoutInflater inflater;
    private View.OnClickListener listener;

    public ControlPanel(LayoutInflater inflater, View.OnClickListener listener, double min, double max, double step, double init) {
        this.min = min;
        this.max = max;
        this.step = step;
        this.init = (int)Math.round((init-min)/step);
        this.visible = false;
        this.inflater = inflater;
        this.listener = listener;
    }

    /**
     * Метод для внешнего использования.
     * Если интерфейс захочет сразу же реагировать на изменения ползунка.
     * Например, сразу же запускать алгоритм яркости.
     * @param listener - функция обработчик.
     */
    @Override
    public void subscribe(Consumer<Integer> listener) {
        if (disposable != null) disposable.add(currentValue.subscribe(listener));
    }

    @Override
    public void unsibscribe() {
        if (disposable != null) disposable.dispose();
    }

    private void updateViewValue(int value) {
        this.value = min + value*step;
        viewSeek.setText(String.format(Locale.ENGLISH, "%3.1f", min + value*step));
    }

    public double getValue() {
        return value;
    }

    public void show(ViewGroup parent, String title) {
        if (!visible) {
            unsibscribe();
            visible = true;
            this.parent = parent;
            View view = inflater.inflate(R.layout.control_panel, parent, true);
            ButterKnife.bind(this, view);
            currentValue = PublishSubject.create();
            disposable = new CompositeDisposable();
            disposable.add(currentValue.subscribe(this::updateViewValue));
            buttonOK.setOnClickListener(listener);
            int count = (int) Math.round((max-min)/step);
            seekBar.setMax(count);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    currentValue.onNext(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            viewTitle.setText(title);
            seekBar.setProgress(this.init);
            viewSeek.setText(String.format(Locale.ENGLISH, "%3.1f", min+init*step));

            AnimationManager.changeAlpha(parent, 1.0f);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) parent.getLayoutParams();
            AnimationManager.translate(parent, 0, -parent.getHeight() - params.topMargin);
        }
    }

    public void hide() {
        if (visible) {
            visible = false;
            unsibscribe();
            parent.removeAllViews();
            parent.removeAllViewsInLayout();
            AnimationManager.changeAlpha(parent, 0.0f);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) parent.getLayoutParams();
            AnimationManager.translate(parent, 0, parent.getHeight() + params.topMargin);
        }
    }
}
