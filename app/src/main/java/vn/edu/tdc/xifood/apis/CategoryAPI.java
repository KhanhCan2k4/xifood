package vn.edu.tdc.xifood.apis;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.edu.tdc.xifood.datamodels.Category;

public class CategoryAPI {
    private static String tblName = "categories";
    private static DatabaseReference productRef = FirebaseDatabase.getInstance().getReference(tblName);
    public static void  all(FirebaseCallbackAll callback) {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Category> categories = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot shot: snapshot.getChildren()) {
                        categories.add(shot.getValue(Category.class));
                    }
                }
                callback.onCallback(categories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(new ArrayList<>());
            }
        });
    }

    public static void find(int id, FirebaseCallback callback) {
        DatabaseReference itemRef = productRef.child("" + id);
        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Category category = null;
                if (snapshot.exists()) {
                    category = snapshot.getValue(Category.class);
                }
                callback.onCallback(category);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(null);
            }
        });
    }
    public static void store(Category category) {
        DatabaseReference itemRef = productRef.push();
        category.setKey(itemRef.getKey());
        itemRef.setValue(category);
    }

    public static void update(Category category) {
        DatabaseReference itemRef = productRef.child(category.getKey() + "");
        itemRef.setValue(category);
    }
    public static void destroy(Category category) {
        DatabaseReference itemRef = productRef.child(category.getKey() + "");
        itemRef.removeValue();
    }

    //interfaces
    public interface FirebaseCallbackAll {
        void onCallback(ArrayList<Category> categories);
    }
    public interface FirebaseCallback {
        void onCallback(Category category);
    }
}
