package vn.edu.tdc.xifood;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConfigAPI {
    private static String tblName = "configs";
    private static DatabaseReference configRef = FirebaseDatabase.getInstance().getReference(tblName);

    public static void find(String key, FirebaseCallback callback) {
        DatabaseReference itemRef = configRef.child(key);
        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String config = null;
                if (snapshot.exists()) {
                    config = snapshot.getValue(String.class);
                }
                callback.onCallback(config);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(null);
            }
        });
    }

    public interface FirebaseCallback {
        void onCallback(String config);
    }
}
