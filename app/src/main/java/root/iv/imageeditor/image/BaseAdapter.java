package root.iv.imageeditor.image;

public interface BaseAdapter<T> {
    void append(T item);
    T getItem(int pos);
    void clear();
}
