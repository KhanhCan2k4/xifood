package vn.edu.tdc.quanlynhansu2023.databases;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.quanlynhansu2023.models.MyPerson;

public class MyDatabaseAPIs {
    // Dinh nghia cac NotificationID
    public static final int SAVE_DONE = 1;
    public static final int GET_ALL_DONE = 2;
    public static final int UPDATE_DONE = 3;
    public static final int DELETE_DONE = 4;
    public static final int FIND_DONE = 5;
    private PersonDAO personDAO;

    // B2. Dinh nghia mot bien interface
    private CompleteListener completeListener;

    // B3. Dinh nghia ham setter cho interface

    public void setCompleteListener(CompleteListener completeListener) {
        this.completeListener = completeListener;
    }


    // Constructor

    public MyDatabaseAPIs(Context context) {
        personDAO = MyDatabase.getDatabase(context).getPersonDAO();
    }

    private static class Temp {
        public long position;
        public boolean complete;
    }
    // Dinh nghia cac APIs cho tang tren
    // 1. Ghi person
    public long savePerson(MyPerson person) {
        final Temp temp = new Temp();
        MyDatabase.dbReadWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                temp.complete = false;
                temp.position = personDAO.savePerson(person);
                temp.complete = true;
            }
        });

        // Tien trinh ngoai doi cho den khi nao tien trinh ben trong hoan thanh
        while (!temp.complete) {}

        // Bao cho phia Activity
        if (temp.position != -1) {
            // B4. Thuc hien thong bao
            if (completeListener != null) {
                // Cap nhat id cho doi tuong person
                person.setId((int)temp.position);
                completeListener.notifyToActivity(SAVE_DONE);
            }
        }
        // Khi hoan thanh ghi du lieu vao database => Tra ve vi tri ghi duoc
        return temp.position;
    }

    // 2. Doc toan bo person
    public void getAllPerson(ArrayList<MyPerson> people) {
        MyDatabase.dbReadWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<MyPerson> myPeople = personDAO.getAllPerson();
                if (myPeople.size() != 0) {
                    people.addAll(myPeople);
                    if (completeListener != null) {
                        completeListener.notifyToActivity(GET_ALL_DONE);
                    }
                }
            }
        });
    }

    // 3. Cap nhat person
    public int updatePerson (MyPerson person) {
        Temp temp = new Temp();
        MyDatabase.dbReadWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                temp.complete = false;
                temp.position = personDAO.updatePerson(person);
                temp.complete = true;
            }
        });
        // DOi cho den khi tien trinh cap nhat du lieu hoan thanh
        while (!temp.complete) {}

        // Thong bao cho tien trinh ngoai biet da hoan thanh
        if (completeListener != null) {
            completeListener.notifyToActivity(UPDATE_DONE);
        }
        return (int)temp.position;
    }

    // 4. Delete person
    public int deletePerson(MyPerson person) {
        final Temp temp = new Temp();
        MyDatabase.dbReadWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                temp.complete = false;
                temp.position = personDAO.deletePerson(person);
                temp.complete = true;
            }
        });
        // Wait to complete
        while (!temp.complete){}
        // Notification
        completeListener.notifyToActivity(DELETE_DONE);
        return (int) temp.position;
    }

    // 5. Find person by name
    public void findPersonByName(String name, ArrayList<MyPerson> people) {
        MyDatabase.dbReadWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<MyPerson> myPeople;
                if (name.isEmpty()) {
                    myPeople = personDAO.getAllPerson();
                }
                else {
                    myPeople = personDAO.findPersonByName(name);
                }
                if (myPeople.size() > 0) {
                    people.clear();
                    people.addAll(myPeople);
                    completeListener.notifyToActivity(FIND_DONE);
                }
            }
        });
    }

    // B1. Dinh nghia interface
    public interface CompleteListener {
        public void notifyToActivity(int notificationID);
    }
}
