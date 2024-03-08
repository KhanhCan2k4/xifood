package vn.edu.tdc.quanlynhansu2023.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.edu.tdc.quanlynhansu2023.models.MyPerson;

@Database(entities = {MyPerson.class}, version = MyDatabase.DB_VERSION)
public abstract class MyDatabase extends RoomDatabase {
    // Dinh nghia cac thuoc tinh cho Database
    public final static String DB_NAME = "my_sqlite";
    public final static int DB_VERSION = 1;

    private static volatile MyDatabase database;

    // Definition of Thread to read or write to the database
    private static final int THREAD_NUMBER = 10;
    public static final ExecutorService dbReadWriteExecutor = Executors.newFixedThreadPool(THREAD_NUMBER);


    // Dinh nghia ham lay ve doi tuong PersonDAO
    public abstract PersonDAO getPersonDAO();

    // Dinh nghia ham khoi tao database
    public static MyDatabase getDatabase(Context context) {
        // Khoi tao database neu chua ton tai
        if (database == null) {
            synchronized (MyDatabase.class) {
                database = Room.databaseBuilder(context, MyDatabase.class, DB_NAME).build();
            }
        }
        return database;
    }
}
