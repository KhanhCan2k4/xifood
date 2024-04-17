package vn.edu.tdc.xifood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.edu.tdc.xifood.adapters.BillAdapter;
import vn.edu.tdc.xifood.adapters.ToppinAdapter;
import vn.edu.tdc.xifood.data.BillData;
import vn.edu.tdc.xifood.data.ToppingData;
import vn.edu.tdc.xifood.databinding.ProductDetailsLayoutBinding;
import vn.edu.tdc.xifood.models.Bill;
import vn.edu.tdc.xifood.models.Topping;

public class DetailActivity extends AppCompatActivity {

    private ProductDetailsLayoutBinding binding;
    private ArrayList<Topping> toppings;
    private ArrayList<Bill> bills;
    private ToppinAdapter toppinAdapter;
    private BillAdapter billAdapter;
    ArrayList<String> uids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ProductDetailsLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get data
        toppings = ToppingData.getToppings();

//        Log.d("ToppingData", "Size of toppings: " + toppings.size());
        toppinAdapter = new ToppinAdapter(this, toppings);
        LinearLayoutManager manager = new LinearLayoutManager(DetailActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager manager2 = new LinearLayoutManager(DetailActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);


        binding.toppingList.setLayoutManager(manager);
        binding.toppingList.setAdapter(toppinAdapter);

        //bill
        bills = BillData.getBills();
        billAdapter = new BillAdapter(this, bills);
        binding.oderListView.setLayoutManager(manager2);
        binding.oderListView.setAdapter(billAdapter);

        uids = new ArrayList<String>();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        firebaseDatabase.getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot shot : snapshot.getChildren()) {
                        uids.add(shot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(this, "hihi" + uids.toString(), Toast.LENGTH_SHORT).show();
    }

}