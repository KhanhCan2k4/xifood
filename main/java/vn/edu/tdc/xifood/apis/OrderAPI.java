package vn.edu.tdc.xifood.apis;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

    public static void find(String key, FirebaseCallback callback) {
        DatabaseReference itemRef = orderRef.child(key);
        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Order order = snapshot.getValue(Order.class);
                    order.setKey(snapshot.getKey());
                    callback.onCallback(order);
                } else {
                    callback.onCallback(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(null);
            }
        });
    }


    public static Task store(Order order) {
        DatabaseReference itemRef = orderRef.push();
        order.setKey(itemRef.getKey());
        return itemRef.setValue(order);
    }

    public static void update(Order order) {
        DatabaseReference itemRef = orderRef.child(order.getKey() + "");
        itemRef.setValue(order);
    }
    public static void destroy(Order order) {
        DatabaseReference itemRef = orderRef.child(order.getKey() + "");
        itemRef.removeValue();
    }

    public static void destroy(Order order, FirebaseCallback callback) {
        DatabaseReference itemRef = orderRef.child(order.getKey() + "");
        itemRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onCallback(order);
            } else {
                callback.onCallback(null);
            }
        });
    }

    // de them don hang vao firebase
    public static void store(Order order, FirebaseCallback callback) {
        // Thêm đơn hàng mới vào Firebase
        String key = orderRef.push().getKey();
        if (key != null) {
            order.setKey(key);
            orderRef.child(key).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        callback.onCallback(order);
                    } else {
                        callback.onCallback(null);
                    }
                }
            });
        } else {
            callback.onCallback(null);
        }
    }

    //interfaces
    public interface FirebaseCallbackAll {
        void onCallback(ArrayList<Order> orders);
    }
    public interface FirebaseCallback {
        void onCallback(Order order);
    }
}
