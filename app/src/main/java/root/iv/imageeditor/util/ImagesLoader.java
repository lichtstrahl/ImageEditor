package root.iv.imageeditor.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.LinkedList;
import java.util.List;

import root.iv.imageeditor.app.App;
import root.iv.imageeditor.image.Image;
import root.iv.imageeditor.image.Album;

public class ImagesLoader {
    public static List<Album> getImageAlbums(Context context) {
        List<Album> albums = new LinkedList<>();
        List<String> albumNames = new LinkedList<>();

        String[] proj = new String[] {
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns._ID
        };

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        try (Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null)) {
            if (cursor.moveToFirst()) {
                int bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME);
                int imageUriColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                int imageIDColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);

                do {
                    String bucketName = cursor.getString(bucketNameColumn);
                    String data = cursor.getString(imageUriColumn);
                    String id = cursor.getString(imageIDColumn);

                    Image image = new Image(Integer.valueOf(id), bucketName, data);

                    if (albumNames.contains(bucketName)) {
                        for (Album album : albums) {
                            if (album.getName().equals(bucketName)) {
                                album.addImage(image);
                                break;
                            }
                        }
                    } else {
                        Album album = new Album(image.getId(), image.getAlbumName(), image.getPhotoUri());
                        album.addImage(image);

                        albums.add(album);
                        albumNames.add(album.getName());
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            App.logE(e.getMessage());
        }

        return albums;
    }
}
