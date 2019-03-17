package root.iv.imageeditor.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

import androidx.appcompat.widget.AppCompatSeekBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import root.iv.imageeditor.R;
import root.iv.imageeditor.util.Subscribed;

public class ControlPanel implements Subscribed<Consumer<Integer>> {
    @BindView(R.id.seek)
    AppCompatSeekBar seekBar;
    @BindView(R.id.buttonOK)
    Button buttonOK;
    @BindView(R.id.viewSeek)
    TextView viewSeek;
    private PublishSubject<Integer> currentValue;
    private CompositeDisposable disposable;
    private double min;
    private double max;
    private int count;
    private double value;
    private int init;

    public ControlPanel(LayoutInflater inflater, ViewGroup parent, double min, double max, int count, double init) {
        View view = inflater.inflate(R.layout.control_panel, parent, true);
        ButterKnife.bind(this, view);
        viewSeek.setText("0");
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
        this.min = min;
        this.max = max;
        this.count = count;
        currentValue = PublishSubject.create();
        seekBar.setMax(count);
        double step = (max-min)/count;
        this.init = (int)Math.round((init-min)/step);
        init();
    }

    private void init() {
        disposable = new CompositeDisposable();
        disposable.add(currentValue.subscribe(this::updateViewValue));
        seekBar.setProgress(this.init);
    }

    public void setListener(View.OnClickListener listener) {
        buttonOK.setOnClickListener(listener);
    }

    /**
     * Метод для внешнего использования.
     * Если интерфейс захочет сразу же реагировать на изменения ползунка.
     * Например, сразу же запускать алгоритм яркости.
     * @param listener - функция обработчик.
     */
    @Override
    public void subscribe(Consumer<Integer> listener) {
        disposable.add(currentValue.subscribe(listener));
    }

    @Override
    public void unsibscribe() {
        disposable.dispose();
    }

    public void reset() {
        unsibscribe();
        init();
    }

    private void updateViewValue(int value) {
        double step = (max-min)/count;
        this.value = min + value*step;
        viewSeek.setText(String.format(Locale.ENGLISH, "%3.1f", min + value*step));
    }

    public double getValue() {
        return value;
    }
}
