package vn.edu.tdc.xifood.data;

import java.util.ArrayList;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.models.NewProduct;

public class NewProductData {

    private static ArrayList<NewProduct> newProducts;

    public static ArrayList<NewProduct> getNewProducts() {
        if (newProducts == null) {
            newProducts = new ArrayList<NewProduct>();
            NewProduct newProduct1 = new NewProduct(1, R.drawable.milk_tea, "Tra sua", 59000);
            NewProduct newProduct2 = new NewProduct(1, R.drawable.milk_tea, "Sua tra", 59000);
            NewProduct newProduct3 = new NewProduct(1, R.drawable.milk_tea, "Tra", 59000);
            NewProduct newProduct4 = new NewProduct(1, R.drawable.milk_tea, "Sua", 59000);
            NewProduct newProduct5 = new NewProduct(1, R.drawable.milk_tea, "Khanh", 59000);
            NewProduct newProduct6 = new NewProduct(1, R.drawable.milk_tea, "Minh", 59000);

            //do du lieu
            newProducts.add(newProduct1);
            newProducts.add(newProduct2);
            newProducts.add(newProduct3);
            newProducts.add(newProduct4);
            newProducts.add(newProduct5);
            newProducts.add(newProduct6);
        }
        return newProducts;
    }
}
