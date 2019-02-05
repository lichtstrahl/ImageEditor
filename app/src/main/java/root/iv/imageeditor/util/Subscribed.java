package root.iv.imageeditor.util;

public interface Subscribed<T> {
    void subscribe(T listener);
    void unsibscribe();
}
