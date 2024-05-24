package vn.edu.tdc.xifood.apis;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.edu.tdc.xifood.datamodels.Topping;

public class ToppingAPI {
    private static String tblName = "toppings";
    private static DatabaseReference toppingRef = FirebaseDatabase.getInstance().getReference(tblName);
    public static void  all(FirebaseCallbackAll callback) {
        toppingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Topping> toppings = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot shot: snapshot.getChildren()) {
                        toppings.add(shot.getValue(Topping.class));
                    }
                }
                callback.onCallback(toppings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(new ArrayList<>());
            }
        });
    }

    public static void find(int id, FirebaseCallback callback) {
        DatabaseReference itemRef = toppingRef.child("" + id);
        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Topping topping = null;
                if (snapshot.exists()) {
                    topping = snapshot.getValue(Topping.class);
                }
                callback.onCallback(topping);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(null);
            }
        });
    }
    public static void store(Topping topping) {
        DatabaseReference itemRef = toppingRef.push();
        topping.setKey(itemRef.getKey());
        itemRef.setValue(topping);
    }

    public static void update(Topping topping) {
        DatabaseReference itemRef = toppingRef.child(topping.getKey() + "");
        itemRef.setValue(topping);
    }
    public static void destroy(Topping topping) {
        DatabaseReference itemRef = toppingRef.child(topping.getKey() + "");
        itemRef.removeValue();
    }

    //interfaces
    public interface FirebaseCallbackAll {
        void onCallback(ArrayList<Topping> toppings);
    }
    public interface FirebaseCallback {
        void onCallback(Topping topping);
    }
}
