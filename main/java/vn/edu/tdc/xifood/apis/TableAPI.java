package vn.edu.tdc.xifood.apis;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.edu.tdc.xifood.datamodels.Product;
import vn.edu.tdc.xifood.datamodels.Table;

public class TableAPI {
    private static String tblName = "tables";
    private static DatabaseReference tableRef = FirebaseDatabase.getInstance().getReference(tblName);
    public static void all(FirebaseCallbackAll callback) {
        tableRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Table> tables = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot shot: snapshot.getChildren()) {
                        tables.add(shot.getValue(Table.class));
                    }
                }
                callback.onCallback(tables);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(new ArrayList<>());
            }
        });
    }

    //interfaces
    public interface FirebaseCallbackAll {
        void onCallback(ArrayList<Table> tables);
    }
}
