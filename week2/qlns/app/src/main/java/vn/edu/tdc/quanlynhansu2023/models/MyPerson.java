package vn.edu.tdc.quanlynhansu2023.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = MyPerson.TEN_BANG)
public class MyPerson {
    // Dinh nghia thuoc tinh cua bang
    @Ignore
    public final static String TEN_BANG = "persons";
    @Ignore
    public final static String ID = "_id";
    @Ignore
    public final static String NAME = "name";
    @Ignore
    public final static String DEGREE = "degree";
    @Ignore
    public final static String HOBBIES = "hobbies";

   @PrimaryKey(autoGenerate = true)
   @ColumnInfo(name = ID)
    private int id;
   @ColumnInfo(name = NAME)
    private String name;
   @ColumnInfo(name = DEGREE)
    private String degree;
   @ColumnInfo(name = HOBBIES)
    private String hobbies;

    public MyPerson() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return name + "#" + degree + "#" + hobbies;
    }
}
