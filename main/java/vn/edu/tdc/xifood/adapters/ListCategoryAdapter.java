package vn.edu.tdc.xifood.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.tdc.xifood.apis.ImageStorageReference;
import vn.edu.tdc.xifood.databinding.ListCategoriesBinding;
import vn.edu.tdc.xifood.datamodels.Category;

public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.ViewHolder> {

    private Activity content;
    private ArrayList<Category> categories;
    private ItemClickListener itemClick;

    public void setItemClick(ItemClickListener itemClick) {
        this.itemClick = itemClick;
    }

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

    public ListCategoryAdapter() {
    }
    public ListCategoryAdapter(Activity content, ArrayList<Category> categories) {
        this.content = content;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ListCategoriesBinding.inflate(content.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.categoryKey = category.getKey();
        ImageStorageReference.setImageInto(holder.listCategoriesBinding.photoCategory, category.getIcon());
        holder.listCategoriesBinding.nameCategory.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListCategoriesBinding listCategoriesBinding;
        private int id;

        public String getCategoryKey() {
            return categoryKey;
        }

        public void setCategoryKey(String categoryKey) {
            this.categoryKey = categoryKey;
        }

        private String categoryKey;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public ViewHolder(@NonNull ListCategoriesBinding itemView) {
            super(itemView.getRoot());
            listCategoriesBinding = itemView;
            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d("adapter", "Click");
                    if(itemClick != null){
                        itemClick.onItemClick(ViewHolder.this);
                    }else{
                        Log.d("adapter", "You must create an ItemClickLister before!!");
                    }
                }
            });
        }
    }

    public interface ItemClickListener{
        public void onItemClick(ViewHolder holder);
    }
}
