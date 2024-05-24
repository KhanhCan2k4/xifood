package vn.edu.tdc.xifood.apis;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.edu.tdc.xifood.datamodels.Payment;

public class PaymentAPI {
    private static String tblName = "payments";
    public static final String MOMO_KEY = "MOMO_KEY";
    public static final String SACOMBANK_KEY = "SACOMBANK_KEY";
    private static DatabaseReference paymentRef = FirebaseDatabase.getInstance().getReference(tblName);

    public static void find(String key, PaymentAPI.FirebaseCallback callback) {
        DatabaseReference itemRef = paymentRef.child(key);
        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Payment payment = null;
                if (snapshot.exists()) {
                    payment = snapshot.getValue(Payment.class);
                }
                callback.onCallback(payment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(null);
            }
        });
    }


    public interface FirebaseCallback {
        void onCallback(Payment payment);
    }
}
