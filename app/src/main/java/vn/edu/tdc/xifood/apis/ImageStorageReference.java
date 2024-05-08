package vn.edu.tdc.xifood.apis;

import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ImageStorageReference {
    private static StorageReference ref = FirebaseStorage.getInstance().getReference();

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
}
