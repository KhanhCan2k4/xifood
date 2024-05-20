package vn.edu.tdc.xifood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import vn.edu.tdc.xifood.views.CancelHeader;

public class DetailActivity extends AppCompatActivity {

    private ProductDetailsLayoutBinding binding;
    private ArrayList<Topping> toppings;
    private ArrayList<Bill> bills;
    private ToppinAdapter toppinAdapter;
    private BillAdapter billAdapter;
    private int id;
    ArrayList<String> uids;
    public static final String DETAIL_PRODUCT_KEY = "DETAIL_PRODUCT_KEY";
    public static final int MAX_AMOUNT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        id = intent.getIntExtra("id", id);

        binding = ProductDetailsLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cancelHeader.setTitle("Sản phẩm #" + id);
        binding.cancelHeader.setCancelListener(new CancelHeader.OnCancelListener() {
            @Override
            public void onCancel(View view) {
                finish();
            }
        });

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
        DatabaseReference myRef = firebaseDatabase.getReference("actions");
        DatabaseReference myRef2 = firebaseDatabase.getReference("new");
        myRef2.setValue(bills.get(0));
//        myRef = myRef.push();
        myRef.setValue("huhu");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    uids.add(snapshot.getValue(String.class));
                    Toast.makeText(DetailActivity.this, uids.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("File", "Failed to read value.", error.toException());
            }
        });
    }

}