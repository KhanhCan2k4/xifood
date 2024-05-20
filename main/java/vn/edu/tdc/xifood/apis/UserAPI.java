package vn.edu.tdc.xifood.apis;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.edu.tdc.xifood.datamodels.User;

public class UserAPI {
    private static String tblName = "users";
    private static DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(tblName);

    public static void  all(FirebaseCallbackAll callback) {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> users = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot shot: snapshot.getChildren()) {
                        users.add(shot.getValue(User.class));
                    }
                }
                callback.onCallback(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(new ArrayList<>());
            }
        });
    }

    public static void find(String key, FirebaseCallback callback) {
        DatabaseReference itemRef = userRef.child(key);
        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = null;
                if (snapshot.exists()) {
                    user = snapshot.getValue(User.class);
                }
                callback.onCallback(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(null);
            }
        });
    }

    public static Task<Void> store(User user) {
        DatabaseReference itemRef = userRef.push();
        user.setKey(itemRef.getKey());
        return itemRef.setValue(user);
    }
    public static void update(User user) {
        DatabaseReference itemRef = userRef.child(user.getKey() + "");
        itemRef.setValue(user);
    }

    public static void destroy(User user) {
        DatabaseReference itemRef = userRef.child(user.getKey());
        itemRef.removeValue();
    }

    //interfaces
    public interface FirebaseCallbackAll {
        void onCallback(ArrayList<User> users);
    }
    public interface FirebaseCallback {
        void onCallback(User user);
    }
}
