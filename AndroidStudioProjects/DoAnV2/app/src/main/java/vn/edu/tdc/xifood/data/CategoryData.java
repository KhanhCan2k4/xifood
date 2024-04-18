package vn.edu.tdc.xifood.data;


import java.util.ArrayList;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.models.Category;

public class CategoryData {

    private static ArrayList<Category> categoryArrayList;

    public static ArrayList<Category> getCategoryArrayList() {

        if (categoryArrayList == null) {
            categoryArrayList = new ArrayList<Category>();
            Category category1 = new Category(0, R.drawable.main_icon, "Trang chu");
            Category category2 = new Category(1, R.drawable.discount_icon, "Uu dai");
            Category category3 = new Category(2, R.drawable.drink_icon, "Do an");
            Category category4 = new Category(3, R.drawable.food_icon, "Do uong");
            Category category5 = new Category(4, R.drawable.snack_icon, "Do an vat");

            //day du lieu vao mang
            categoryArrayList.add(category1);
            categoryArrayList.add(category2);
            categoryArrayList.add(category3);
            categoryArrayList.add(category4);
            categoryArrayList.add(category5);
        }
        return categoryArrayList;
    }
}
