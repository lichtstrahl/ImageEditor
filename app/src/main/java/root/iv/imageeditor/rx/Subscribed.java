package root.iv.imageeditor.rx;

public interface Subscribed<T> {
    void subscribe(T listener);
    void unsibscribe();
}
