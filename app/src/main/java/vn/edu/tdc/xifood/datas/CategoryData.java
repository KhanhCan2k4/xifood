package vn.edu.tdc.xifood.datas;

import com.example.myxifood.R;

import java.util.ArrayList;

import vn.edu.tdc.xifood.models.Category;

public class CategoryData {

    private static ArrayList<Category> categoryArrayList;
    public static ArrayList<Category> getCategoryArrayList() {

        if (categoryArrayList == null){
            categoryArrayList = new ArrayList<Category>();
            Category category1 = new Category(0, R.drawable.order_icon, "Do an");
            Category category2 = new Category(1, R.drawable.order_icon, "Do uong");
            Category category3 = new Category(2, R.drawable.order_icon, "Khanh");
            Category category4 = new Category(3, R.drawable.order_icon, "Minh");
            Category category5 = new Category(4, R.drawable.order_icon, "Du");
            Category category6 = new Category(5, R.drawable.order_icon, "Nhi");

            //day du lieu vao mang
            categoryArrayList.add(category1);
            categoryArrayList.add(category2);
            categoryArrayList.add(category3);
            categoryArrayList.add(category4);
            categoryArrayList.add(category5);
            categoryArrayList.add(category6);
        }
        return categoryArrayList;
    }
}
