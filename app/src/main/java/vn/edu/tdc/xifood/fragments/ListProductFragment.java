package vn.edu.tdc.xifood.fragments;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myxifood.R;
import com.example.myxifood.databinding.ListProductBinding;

import java.util.ArrayList;

import vn.edu.tdc.xifood.adapters.ListProductsAdapter;
import vn.edu.tdc.xifood.models.Product;

public class ListProductFragment extends LinearLayout {


    public ListProductFragment(Context context) {
        super(context);
        setUp(context);
    }

    public ListProductFragment(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUp(context);
    }

    public ListProductFragment(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp(context);
    }

    private void setUp(Context context) {
        inflate(context, R.layout.list_product_fragment, this);



        //xet noi dung cho RecyclerView
    }

    public void setCategoryName(String nameCategory){
        TextView txtCategoryName = findViewById(R.id.txtCategoryName);
        txtCategoryName.setText(nameCategory);
    }

    public void setListProducts(ArrayList<Product> products, Activity context){
        RecyclerView listProducts = findViewById(R.id.listProducts);

        //tao 1 adapter
        ListProductsAdapter adapter = new ListProductsAdapter(context, products);
        GridLayoutManager manager = new GridLayoutManager(context, 3);
        manager.setOrientation(GridLayoutManager.HORIZONTAL);

        //xet lan luot manager & adapter
        listProducts.setLayoutManager(manager);
        listProducts.setAdapter(adapter);
    }

//    public interface OnSetDataListenner{
//        public void
//    }
}
