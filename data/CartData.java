package vn.edu.tdc.xifood.data;


import java.util.ArrayList;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.models.Cart;

public class CartData {
    private  static ArrayList<Cart> carts;

    public static ArrayList<Cart> getCarts() {
        if(carts == null)
        {
            carts =new ArrayList<>();
            Cart c = new Cart();
            c.setGia(1);
            c.setGhiChu("it duong");
            c.setName("Tra Sua Thai Do");
            c.setHinhAnh(R.drawable.img);
            c.setSoLuong(1);
            c.setThuTuSanPham("1");
            c.setToppingThem("");

            Cart c1 = new Cart();
            c1.setGia(11);
            c1.setGhiChu("it duong 1");
            c1.setName("Tra Sua Thai Do 1");
            c1.setHinhAnh(R.drawable.img);
            c1.setSoLuong(11);
            c1.setThuTuSanPham("11");
            c1.setToppingThem("");

            Cart c2 = new Cart();
            c2.setGia(1);
            c2.setGhiChu("it duong");
            c2.setName("Tra Sua Thai Do");
            c2.setHinhAnh(R.drawable.img);
            c2.setSoLuong(1);
            c2.setThuTuSanPham("1");
            c2.setToppingThem("");

            carts.add(c);
            carts.add(c1);
            carts.add(c2);


        }
        return carts;
    }
}
