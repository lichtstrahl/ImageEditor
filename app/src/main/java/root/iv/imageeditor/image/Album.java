package root.iv.imageeditor.image;

import java.util.LinkedList;
import java.util.List;

public class Album {
    private int id;
    private String name;
    private String coverUri;
    private List<Image> images;

    public Album(int id, String name, String coverUri) {
        this.id = id;
        this.name = name;
        this.coverUri = coverUri;
        this.images = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCoverUri() {
        return coverUri;
    }

    public int addImage(Image img) {
        images.add(img);
        return images.size();
    }

    public List<Image> getImages() {
        return images;
    }
}
