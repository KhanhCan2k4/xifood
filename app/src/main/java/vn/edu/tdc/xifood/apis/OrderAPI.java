package vn.edu.tdc.xifood.apis;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.edu.tdc.xifood.models.Category;
import vn.edu.tdc.xifood.datamodels.Order;

public class OrderAPI {
    private static String tblName = "orders";
    private static DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference(tblName);
    public static void  all(FirebaseCallbackAll callback) {
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Order> orders = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot shot: snapshot.getChildren()) {
                        orders.add(shot.getValue(Order.class));
                    }
                }
                callback.onCallback(orders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(new ArrayList<>());
            }
        });
    }

    public static void find(int id, FirebaseCallback callback) {
        DatabaseReference itemRef = orderRef.child("" + id);
        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Order order = null;
                if (snapshot.exists()) {
                    order = snapshot.getValue(Order.class);
                }
                callback.onCallback(order);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(null);
            }
        });
    }
    public static void store(Order order) {
        DatabaseReference itemRef = orderRef.push();
        order.setKey(itemRef.getKey());
        itemRef.setValue(order);
    }

    public static void update(Order order) {
        DatabaseReference itemRef = orderRef.child(order.getKey() + "");
        itemRef.setValue(order);
    }
    public static void destroy(Order order) {
        DatabaseReference itemRef = orderRef.child(order.getKey() + "");
        itemRef.removeValue();
    }

    //interfaces
    public interface FirebaseCallbackAll {
        void onCallback(ArrayList<Order> orders);
    }
    public interface FirebaseCallback {
        void onCallback(Order order);
    }
}
