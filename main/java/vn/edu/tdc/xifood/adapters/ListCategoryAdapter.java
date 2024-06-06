package vn.edu.tdc.xifood.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.tdc.xifood.ImageStorageReference;
import vn.edu.tdc.xifood.databinding.ListCategoriesBinding;
import vn.edu.tdc.xifood.mydatamodels.Category;

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
        ImageStorageReference.setImageInto(holder.listCategoriesBinding.photoCategory, category.getIcon());
        holder.listCategoriesBinding.nameCategory.setText(category.getName());
        final int p = position;
        holder.position = p;
        holder.context = content;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListCategoriesBinding listCategoriesBinding;
        private int position;
        private Activity context;
        public int geCategorytPosition() {
            return position;
        }

        public ViewHolder(@NonNull ListCategoriesBinding itemView) {
            super(itemView.getRoot());
            listCategoriesBinding = itemView;
            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClick != null) {
                        itemClick.onItemClick(ViewHolder.this);
                    } else {
                        new AlertDialog.Builder(context)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setMessage("Không thể chọn danh mục này")
                                .show();
                    }
                }
            });
        }
    }

    public interface ItemClickListener {
        void onItemClick(ListCategoryAdapter.ViewHolder holder);
    }
}
