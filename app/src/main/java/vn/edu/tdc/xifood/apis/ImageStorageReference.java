package vn.edu.tdc.xifood.apis;

import android.net.Uri;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageStorageReference {
    private static final StorageReference ref = FirebaseStorage.getInstance().getReference();

    public static void setImageInto(ImageView imageView, String url) {
        StorageReference imgRef = ref.child(url);

        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri.toString()).into(imageView);
        });
    }

    public static void setImageInto(ImageButton imageButton, String url) {
        StorageReference imgRef = ref.child(url);

        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri.toString()).into(imageButton);
        });
    }

    public static void upload(String fileName, String filePath) {
        Uri uri = Uri.fromFile(new File(filePath));
        StorageReference imgRef = ref.child(fileName);
        imgRef.putFile(uri);
    }

    public static void upload(String fileName, File file) {
        Uri uri = Uri.fromFile(file);
        StorageReference imgRef = ref.child(fileName);
        imgRef.putFile(uri);
    }
}
