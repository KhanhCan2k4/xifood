package vn.edu.tdc.xifood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class API<T> {
    //final tabl names
    public static final String USER_TABLE_NAME = "USERS";
    public static final String CONFIG_TABLE_NAME = "CONFIGS";
    public static final String PRODUCT_TABLE_NAME = "PRODUCTS";
    public static final String CATEGORY_TABLE_NAME = "CATEGORIES";
    public static final String TOPPING_TABLE_NAME = "TOPPINGS";
    public static final String CART_TABLE_NAME = "CARTS";
    public static final String REVIEW_TABLE_NAME = "REVIEWS";
    public static final String VOUCHER_TABLE_NAME = "VOUCHERS";
    public static final String PAYMENT_TABLE_NAME = "PAYMENTS";
    public static final String TABLE_TABLE_NAME = "TABLES";
    public static final String ORDER_TABLE_NAME = "ORDERS/" + SharePreference.find(SharePreference.USER_TOKEN_KEY);

    //fields
    //type of object
    private Class<T> type;
    private DatabaseReference ref;

    //constructor
    public API(Class<T> type, String tblName) {
        this.type = type;
        this.ref = FirebaseDatabase.getInstance().getReference(tblName);
    }

    //get all
    public void all(FirebaseCallbackAll callback) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<T> list = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot shot: snapshot.getChildren()) {
                        list.add(shot.getValue(type));
                    }
                }
                callback.onCallback(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(new ArrayList());
            }
        });
    }

    public void find(String key,FirebaseCallback callback) {
        DatabaseReference itemRef = ref.child(key);
        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                T t = null;
                if (snapshot.exists()) {
                    t = snapshot.getValue(type);
                }
                callback.onCallback(t);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(null);
            }
        });
    }

    public void find(String key,FirebaseCallback callback, Object isSingle) {
        DatabaseReference itemRef = ref.child(key);
        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                T t = null;
                if (snapshot.exists()) {
                    t = snapshot.getValue(type);
                }
                callback.onCallback(t);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(null);
            }
        });
    }

    public String store(T t,@Nullable OnSuccessListener successListener,@Nullable OnCanceledListener canceledListener,@Nullable OnCompleteListener completeListener) {
        DatabaseReference itemRef = ref.push();
        String key = itemRef.getKey();

        itemRef.setValue(t)
                .addOnSuccessListener(successListener)
                .addOnCanceledListener(canceledListener)
                .addOnCompleteListener(completeListener);

        //save key
        itemRef.child("key").setValue(key);

        return key;
    }

    public void update(T t, String key,@Nullable OnSuccessListener successListener,@Nullable OnCanceledListener canceledListener,@Nullable OnCompleteListener completeListener) {
        DatabaseReference itemRef = ref.child(key);
        itemRef.setValue(t)
                .addOnSuccessListener(successListener)
                .addOnCanceledListener(canceledListener)
                .addOnCompleteListener(completeListener);
    }
    public void destroy(String key,@Nullable  OnSuccessListener successListener,@Nullable OnCanceledListener canceledListener,@Nullable OnCompleteListener completeListener) {
        DatabaseReference itemRef = ref.child(key);
        itemRef.removeValue()
                .addOnSuccessListener(successListener)
                .addOnCanceledListener(canceledListener)
                .addOnCompleteListener(completeListener);
    }

    //interface
    public interface FirebaseCallbackAll {
        void onCallback(ArrayList list);
    }

    public interface FirebaseCallback {
        void onCallback(Object object);
    }
}
