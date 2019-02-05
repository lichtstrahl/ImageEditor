package root.iv.imageeditor;

public class ImageItem {
    private String path;
    private String name;

    public ImageItem(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
}
