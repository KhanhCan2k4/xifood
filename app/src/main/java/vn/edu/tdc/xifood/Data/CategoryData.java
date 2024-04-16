package vn.edu.tdc.xifood.Data;

import java.util.ArrayList;

import vn.edu.tdc.xifood.Model.Category;

public class CategoryData {

    private static ArrayList<Category> categoryArrayList;
    public static ArrayList<Category> getCategoryArrayList() {

        if (categoryArrayList == null){
            categoryArrayList = new ArrayList<Category>();
            Category category1 = new Category(0, "Trang chu");
            Category category2 = new Category(1, "Thuc an");
            Category category3 = new Category(2, "Thuc uong");

            //day du lieu vao mang
            categoryArrayList.add(category1);
            categoryArrayList.add(category2);
            categoryArrayList.add(category3);
        }
        return categoryArrayList;
    }
}
