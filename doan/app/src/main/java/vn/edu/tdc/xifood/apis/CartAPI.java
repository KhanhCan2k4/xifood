package vn.edu.tdc.xifood.apis;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.edu.tdc.xifood.datamodels.Order;

public class CartAPI {
    private static String tblName = "carts";
    private static DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference(tblName);

    public static void all(FirebaseCallbackAll callback) {
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Order> orders = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot shot : snapshot.getChildren()) {
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

    public static void find(FirebaseCallback callback) {
        DatabaseReference itemRef = cartRef.child(SharePreference.find(SharePreference.CART_KEY));

        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

    public static void store(Order order, OnSuccessListener onSuccessListener, OnCanceledListener onCanceledListener) {

        find(new FirebaseCallback() {
            @Override
            public void onCallback(Order oldOder) {
                if (oldOder == null) { //new cart
                    //first time to have a cart --> create new one
                    DatabaseReference itemRef = cartRef.push();
                    String cartKey = itemRef.getKey();
                    order.setKey(cartKey);

                    //save into local
                    SharePreference.store(SharePreference.CART_KEY, cartKey);
                    Log.d("TAG", "onCallback: " + SharePreference.find(SharePreference.CART_KEY));

                    //save new order into cart
                    itemRef.setValue(order)
                        .addOnSuccessListener(onSuccessListener)
                        .addOnCanceledListener(onCanceledListener);
                } else {
                    //save new order into order list
                    oldOder.addOrderedProduct(order.getOrderedProducts().get(0));
                    Log.d("TAG", "onCallback: " + SharePreference.find(SharePreference.CART_KEY));
                    DatabaseReference itemRef = cartRef.child(SharePreference.find(SharePreference.CART_KEY));
                    itemRef.setValue(oldOder)
                        .addOnSuccessListener(onSuccessListener)
                        .addOnCanceledListener(onCanceledListener);
                }
            }
        });
    }

    public static void update(Order order) {
        DatabaseReference itemRef = cartRef.child(order.getKey() + "");
        itemRef.setValue(order);
    }

    public static void destroy(Order order) {
        DatabaseReference itemRef = cartRef.child(order.getKey() + "");
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
