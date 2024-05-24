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
import vn.edu.tdc.xifood.datamodels.OrderedProduct;

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

    public static void find(String userId, FirebaseCallback callback) {
        DatabaseReference itemRef = cartRef.child(userId);

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

    public static void store(String userId, Order order, OnSuccessListener<Void> onSuccessListener, OnCanceledListener onCanceledListener) {
        find(userId, new FirebaseCallback() {
            @Override
            public void onCallback(Order oldOder) {
                if (oldOder == null || SharePreference.find(SharePreference.CART_KEY).isEmpty()) { //new cart
                    //first time to have a cart --> create new one
                    DatabaseReference itemRef = cartRef.push();
                    String cartKey = itemRef.getKey();

                    //save into local
                    SharePreference.store(SharePreference.CART_KEY, cartKey);
                    Log.d("TAG", "onCallback: SharePreference.CART_KEY" + SharePreference.find(SharePreference.CART_KEY));

                    //save new order into cart
                    itemRef.setValue(order)
                            .addOnSuccessListener(onSuccessListener)
                            .addOnCanceledListener(onCanceledListener);
                } else {
                    Log.d("TAG", "onCallback: old order" + oldOder);
                    //save new order into order list
                    if (!order.getOrderedProducts().isEmpty()) {
                        oldOder.addOrderedProduct(order.getOrderedProducts().get(0));
                    }
                    Log.d("TAG", "onCallback: SharePreference.CART_KEY " + SharePreference.find(SharePreference.CART_KEY));
                    DatabaseReference itemRef = cartRef.child(SharePreference.find(SharePreference.CART_KEY));
                    itemRef.setValue(oldOder)

                            .addOnSuccessListener(onSuccessListener)
                            .addOnCanceledListener(onCanceledListener);
                }
            }
        });
    }

    public static void update(String userId, OrderedProduct updatedProduct, OnSuccessListener<Void> onSuccessListener, OnCanceledListener onCanceledListener) {
        DatabaseReference itemRef = cartRef.child(userId);
        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null && order.getOrderedProducts() != null) {
                        ArrayList<OrderedProduct> orderedProducts = order.getOrderedProducts();
                        for (int i = 0; i < orderedProducts.size(); i++) {
                            if (orderedProducts.get(i).getProduct().getKey().equals(updatedProduct.getProduct().getKey())) {
                                orderedProducts.set(i, updatedProduct);
                                break;
                            }
                        }
                        order.setOrderedProducts(orderedProducts);
                        itemRef.setValue(order)
                                .addOnSuccessListener(onSuccessListener)
                                .addOnCanceledListener(onCanceledListener);
                    } else {
                        onCanceledListener.onCanceled();
                    }
                } else {
                    onCanceledListener.onCanceled();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CartAPI", "Failed to update item in Firebase: " + error.getMessage());
                onCanceledListener.onCanceled();
            }
        });
    }

    public static Task<Void> destroy() {
        DatabaseReference itemRef = cartRef.child(SharePreference.find(SharePreference.CART_KEY));
        return itemRef.removeValue();
    }

    public static void destroy(String userId, String productKey, OnSuccessListener<Void> onSuccessListener, OnCanceledListener onCanceledListener) {
        DatabaseReference itemRef = cartRef.child(userId);
        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null && order.getOrderedProducts() != null) {
                        // Tìm và xóa orderedProduct có chứa productKey tương ứng
                        ArrayList<OrderedProduct> orderedProducts = order.getOrderedProducts();
                        for (int i = 0; i < orderedProducts.size(); i++) {
                            if (orderedProducts.get(i).getProduct().getKey().equals(productKey)) {
                                orderedProducts.remove(i);
                                break;
                            }
                        }
                        // Cập nhật lại chỉ mục của orderedProducts
                        order.setOrderedProducts(orderedProducts);
                        // Cập nhật lại dữ liệu trên Firebase
                        itemRef.setValue(order)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (orderedProducts.isEmpty()) {
                                            // Nếu danh sách orderedProducts trống sau khi xóa
                                            // Thì xóa cả cart của người dùng
                                            itemRef.removeValue()
                                                    .addOnSuccessListener(onSuccessListener)
                                                    .addOnCanceledListener(onCanceledListener);
                                        } else {
                                            // Ngược lại, tiếp tục thông báo về sự thành công
                                            onSuccessListener.onSuccess(aVoid);
                                        }
                                    }
                                })
                                .addOnCanceledListener(onCanceledListener);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CartAPI", "Failed to remove item from Firebase: " + error.getMessage());
                onCanceledListener.onCanceled();
            }
        });
    }
    public static void update(String userId, ArrayList<OrderedProduct> order) {
        DatabaseReference itemRef = cartRef.child(userId);
        itemRef.setValue(order);
    }
    public static void update(String userId, Order order) {
        DatabaseReference itemRef = cartRef.child(userId);
        itemRef.setValue(order);
    }

    //interfaces
    public interface FirebaseCallbackAll {
        void onCallback(ArrayList<Order> orders);
    }

    public interface FirebaseCallback {
        void onCallback(Order order);
    }
}
