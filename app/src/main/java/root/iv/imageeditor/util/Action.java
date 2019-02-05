package root.iv.imageeditor.util;

public class Action {
    public interface Action1Void<T> {
        void run(T x);
    }

    public interface Action1Result<T, resultType> {
        resultType run(T x);
    }
}