package vn.edu.tdc.xifood.data;


import java.util.ArrayList;

import vn.edu.tdc.xifood.models.Bill;

public class BillData {
    private static ArrayList<Bill> bills;

    public static ArrayList<Bill> getBills() {
        if (bills == null)
        {
            bills =new ArrayList<>();

            Bill b =new Bill();
            b.setSoLuong(1);
            b.setName("Tra Sua Chan Chau Duong Den");
            b.setGia(25000);
            bills.add(b);

            Bill b1 =new Bill();
            b1.setGia(5000);
            b1.setName("Chan Chau Duong Den");
            b1.setSoLuong(1);
             bills.add(b1);
        }
        return bills;
    }
}
