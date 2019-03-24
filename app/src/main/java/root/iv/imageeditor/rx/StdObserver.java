package root.iv.imageeditor.rx;

import androidx.annotation.Nullable;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import root.iv.imageeditor.app.App;

public class StdObserver<T> implements SingleObserver<T> {
    @Nullable
    private Disposable disposable;
    @Nullable
    private Consumer<T> action;
    @Nullable
    private Consumer<Throwable> error;

    public StdObserver(@Nullable Consumer<T> action, @Nullable Consumer<Throwable> error) {
        this.action = action;
        this.error = error;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onSuccess(T x) {
        try {
            if (action != null) action.accept(x);
        } catch (Exception e) {
            App.logE(e.getMessage());
        }
    }

    @Override
    public void onError(Throwable e) {
        try {
            if (error != null) error.accept(e);
        } catch (Exception er) {
            App.logE(er.getMessage());
        }
    }

    public void unsubscribe() {
        if (disposable != null) disposable.dispose();
    }
}
