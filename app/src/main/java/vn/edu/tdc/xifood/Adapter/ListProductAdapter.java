package vn.edu.tdc.xifood.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myxifood.databinding.ListCategoriesBinding;

import java.util.ArrayList;
import java.util.Locale;

import vn.edu.tdc.xifood.Model.Category;

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ViewHolder> {

    private Activity content;
    private ArrayList<Category> categories;
//    private View.OnClickListener


    public Activity getContent() {
        return content;
    }

    public void setContent(Activity content) {
        this.content = content;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public ListProductAdapter() {
    }
    public ListProductAdapter(Activity content, ArrayList<Category> categories) {
        this.content = content;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ListProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ListProductAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListCategoriesBinding listCategoriesBinding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
