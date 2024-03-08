package vn.edu.tdc.quanlynhansu2023.databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import vn.edu.tdc.quanlynhansu2023.models.MyPerson;

@Dao
public interface PersonDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public long savePerson(MyPerson person);
    @Query("SELECT * FROM " + MyPerson.TEN_BANG + " ORDER BY " + MyPerson.NAME + " ASC")
    public List<MyPerson> getAllPerson();
    @Update
    public int updatePerson(MyPerson person);
    @Delete
    public int deletePerson(MyPerson person);
    @Query("SELECT * FROM " +MyPerson.TEN_BANG + " WHERE " + MyPerson.NAME + " LIKE :name")
    public List<MyPerson> findPersonByName(String name);
}
