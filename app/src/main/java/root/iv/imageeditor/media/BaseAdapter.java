package root.iv.imageeditor.media;

public interface BaseAdapter<T> {
    void append(T item);
    T getItem(int pos);
    void clear();
}
