package vn.edu.tdc.xifood.data;

import java.util.ArrayList;

import vn.edu.tdc.xifood.models.Topping;

public class ToppingData {
    private static ArrayList<Topping> toppings;

    public static ArrayList<Topping> getToppings() {
        if (toppings == null)
        {
            toppings = new ArrayList<>();

      Topping t1=new Topping();
      t1.setName("Chan Chau Trang");
      t1.setPrice(5000);
      t1.setSoluong(0);

        Topping t2=new Topping();
        t2.setName("Chan Chau Den");
        t2.setPrice(5000);
        t2.setSoluong(0);

        Topping t3=new Topping();
        t3.setName("Chan Chau Hoang Kim");
        t3.setPrice(5000);
        t3.setSoluong(0);

        Topping t4=new Topping();
        t4.setName("Suong Sao");
        t4.setPrice(5000);
        t4.setSoluong(0);

        Topping t5=new Topping();
        t5.setName("Khuch Bach");
        t5.setPrice(5000);
        t5.setSoluong(0);

        Topping t6=new Topping();
        t6.setName("Pho mai");
        t6.setPrice(5000);
        t6.setSoluong(0);

        toppings.add(t1);
        toppings.add(t2);
        toppings.add(t3);
        toppings.add(t4);
        toppings.add(t5);
        toppings.add(t6);
        }

        return toppings;
    }
}
