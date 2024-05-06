package vn.edu.tdc.xifood.data;


import java.util.ArrayList;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.models.Category;
import vn.edu.tdc.xifood.models.Product;

public class CategoryData {

    private static ArrayList<Category> categoryArrayList;

    public static ArrayList<Category> getCategoryArrayList() {

        if (categoryArrayList == null) {
            // cac san pham
            Product product1 = new Product(1,"uu dai 1","", 19000);
            Product product2 = new Product(2,"ua dai 2","", 29000);
            Product product3 = new Product(3,"uu dai 3","", 39000);
            Product product4 = new Product(4,"uu dai 4","", 49000);
            Product product5 = new Product(5,"do an 1","", 59000);
            Product product6 = new Product(6,"do an 2","", 19000);
            Product product7 = new Product(7,"do an 3","", 29000);
            Product product8 = new Product(8,"do an 4","", 39000);
            Product product9 = new Product(9,"do uong 1","", 49000);
            Product product10 = new Product(10,"do uong 2","", 59000);
            Product product11 = new Product(11,"do uong 3","", 19000);
            Product product12 = new Product(12,"do uong 4","", 29000);
            Product product13 = new Product(13,"do an vat 1","", 39000);
            Product product14 = new Product(14,"do an vat 2","", 49000);
            Product product15= new Product(15,"do an vat 3","", 59000);
            Product product16= new Product(16,"do an vat 4","", 59000);
            Product product17 = new Product(17,"mon moi 1","", 39000);
            Product product18 = new Product(18,"mon moi 2","", 49000);
            Product product19= new Product(19,"mon moi 3","", 59000);
            Product product20= new Product(20,"mon moi 4","", 59000);
            Product product21 = new Product(21,"xem gan day 1","", 39000);
            Product product22 = new Product(22,"xem gan day 2","", 49000);
            Product product23= new Product(23,"xem gan day 3","", 59000);
            Product product24= new Product(24,"xem gan day 4","", 59000);


            categoryArrayList = new ArrayList<Category>();
            Category category1 = new Category(0, R.drawable.main_icon, "Trang chu");

            //them san pham vao danh sach san pham
//            ArrayList<Product> products1 = new ArrayList<>();

            //them danh sach san pham vao danh muc
//            category1.setProducts(products1);

            Category category2 = new Category(1, R.drawable.discount_icon, "Uu dai");

            ArrayList<Product> products2 = new ArrayList<>();
            products2.add(product1);
            products2.add(product2);
            products2.add(product3);
            products2.add(product4);

            //them danh sach san pham vao danh muc
            category2.setProducts(products2);

            Category category3 = new Category(2, R.drawable.drink_icon, "Do an");

            ArrayList<Product> products3 = new ArrayList<>();
            products3.add(product5);
            products3.add(product6);
            products3.add(product7);
            products3.add(product8);

            //them danh sach san pham vao danh muc
            category3.setProducts(products3);

            Category category4 = new Category(3, R.drawable.food_icon, "Do uong");

            ArrayList<Product> products4 = new ArrayList<>();
            products4.add(product9);
            products4.add(product10);
            products4.add(product11);
            products4.add(product12);

            //them danh sach san pham vao danh muc
            category4.setProducts(products4);

            Category category5 = new Category(4, R.drawable.snack_icon, "Do an vat");
            ArrayList<Product> products5 = new ArrayList<>();
            products5.add(product13);
            products5.add(product14);
            products5.add(product15);
            products5.add(product16);

            //them danh sach san pham vao danh muc
            category5.setProducts(products5);


            Category category6 = new Category(5, R.drawable.drink_icon, "Mon moi");

            ArrayList<Product> products6 = new ArrayList<>();
            products6.add(product17);
            products6.add(product18);
            products6.add(product19);
            products6.add(product20);

            //them danh sach san pham vao danh muc
            category6.setProducts(products6);

            Category category7 = new Category(6, R.drawable.food_icon, "Xem gan day");

            ArrayList<Product> products7 = new ArrayList<>();
            products7.add(product21);
            products7.add(product22);
            products7.add(product23);
            products7.add(product24);

            //them danh sach san pham vao danh muc
            category7.setProducts(products7);


            //day du lieu vao mang
            categoryArrayList.add(category1);
            categoryArrayList.add(category2);
            categoryArrayList.add(category3);
            categoryArrayList.add(category4);
            categoryArrayList.add(category5);
            categoryArrayList.add(category6);
            categoryArrayList.add(category7);
        }
        return categoryArrayList;
    }

    public static ArrayList<Product> getProductsByCategoryID(int id){
        for (Category category:categoryArrayList){
            if (category.getId() == id){
                return category.getProducts();
            }
        }
        return new ArrayList<Product>();
    }
}
