package vn.edu.tdc.xifood.apis;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.edu.tdc.xifood.datamodels.Product;

public class ProductAPI {
    private static String tblName = "products";
    private static DatabaseReference productRef = FirebaseDatabase.getInstance().getReference(tblName);
    public static void  all(FirebaseCallbackAll callback) {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Product> products = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot shot: snapshot.getChildren()) {
                        products.add(shot.getValue(Product.class));
                    }
                }
                callback.onCallback(products);
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
                Product product = null;
                if (snapshot.exists()) {
                    product = snapshot.getValue(Product.class);
                }
                callback.onCallback(product);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(null);
            }
        });
    }
    public static void store(Product product) {
        DatabaseReference itemRef = productRef.push();
        product.setKey(itemRef.getKey());
        itemRef.setValue(product);
    }

    public static void update(Product product) {
        DatabaseReference itemRef = productRef.child(product.getKey() + "");
        itemRef.setValue(product);
    }
    public static void destroy(Product product) {
        DatabaseReference itemRef = productRef.child(product.getKey() + "");
        itemRef.removeValue();
    }

    //interfaces
    public interface FirebaseCallbackAll {
        void onCallback(ArrayList<Product> products);
    }
    public interface FirebaseCallback {
        void onCallback(Product product);
    }
}
