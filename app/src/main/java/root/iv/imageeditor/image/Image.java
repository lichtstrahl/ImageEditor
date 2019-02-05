package root.iv.imageeditor.image;

public class Image {
    private int id;
    private String albumName;
    private String photoUri;

    public Image(int id, String albumName, String photoUri) {
        this.id = id;
        this.albumName = albumName;
        this.photoUri = photoUri;
    }

    public int getId() {
        return id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getPhotoUri() {
        return photoUri;
    }
}
