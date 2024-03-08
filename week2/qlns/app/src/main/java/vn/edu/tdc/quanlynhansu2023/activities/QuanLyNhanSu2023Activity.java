package vn.edu.tdc.quanlynhansu2023.activities;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.RoomDatabase;

import java.util.ArrayList;

import vn.edu.tdc.quanlynhansu2023.R ;
import vn.edu.tdc.quanlynhansu2023.adapters.MyRecycleViewAdapter;
import vn.edu.tdc.quanlynhansu2023.adapters.PersonAdapter;
import vn.edu.tdc.quanlynhansu2023.databases.MyDatabaseAPIs;
import vn.edu.tdc.quanlynhansu2023.databinding.QuanlyNhansuLayoutBinding;
import vn.edu.tdc.quanlynhansu2023.databinding.QuanlyNhansuRecycleViewLayoutBinding;
import vn.edu.tdc.quanlynhansu2023.models.MyPerson;

public class QuanLyNhanSu2023Activity extends AppCompatActivity {
//    private QuanlyNhansuLayoutBinding binding;
    private QuanlyNhansuRecycleViewLayoutBinding binding;

    private ArrayList<MyPerson> persons;

    private int selectedRow = -1;
    private int backColor;
    private View prev;

//    PersonAdapter adapter;
    MyRecycleViewAdapter adapter;

    private MyDatabaseAPIs databaseAPIs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = QuanlyNhansuLayoutBinding.inflate(getLayoutInflater());
        binding = QuanlyNhansuRecycleViewLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Init
        databaseAPIs = new MyDatabaseAPIs(this);

        // THuc hien xu ly uy quyen
        databaseAPIs.setCompleteListener(new MyDatabaseAPIs.CompleteListener() {
            @Override
            public void notifyToActivity(int notificationID) {
                if (notificationID == MyDatabaseAPIs.SAVE_DONE) {
                    adapter.notifyDataSetChanged();
                    clear();
                }
                else if (notificationID == MyDatabaseAPIs.GET_ALL_DONE) {
                    adapter.notifyDataSetChanged();
                }
                else if (notificationID == MyDatabaseAPIs.UPDATE_DONE) {
                    adapter.notifyDataSetChanged();
                }
                else if (notificationID == MyDatabaseAPIs.DELETE_DONE) {
                    persons.remove(selectedRow);
                    if (prev != null) {
                        prev.setBackgroundColor(backColor);
                    }
                    selectedRow = -1;
                    binding.btnThem.setEnabled(true);
                    clear();
                    adapter.notifyDataSetChanged();
                }
                else if (notificationID == MyDatabaseAPIs.FIND_DONE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        getMainExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                if (prev != null) {
                                    prev.setBackgroundColor(backColor);
                                }
                                selectedRow = -1;
                                clear();
                                binding.btnThem.setEnabled(true);
                            }
                        });
                    }
                }
            }
        });

        persons = new ArrayList<MyPerson>();

        // Doc du lieu tu database neu co
        databaseAPIs.getAllPerson(persons);

        //ArrayAdapter<MyPerson> adapter = new ArrayAdapter<MyPerson>(this, android.R.layout.simple_list_item_1, persons);

//        adapter = new PersonAdapter(this, R.layout.list_item, persons);
        adapter = new MyRecycleViewAdapter(this, persons);

        //create an instance of RecycleView's LayoutManager
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.personsList.setLayoutManager(manager);

        binding.personsList.setAdapter(adapter);

        // Event processing
        binding.btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ghi vao database
                MyPerson person = getPerson();
                databaseAPIs.savePerson(person);
                persons.add(person);
            }
        });

        adapter.setOnItemClickListener(new MyRecycleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(MyRecycleViewAdapter.MyViewHolder viewHolder) {
                Log.d("TAG-adapter", "onItemClick: " + viewHolder.getRecycleViewPosition() +".");
            }
        });

        /*
        binding.personsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectedRow == -1) {
                    backColor = view.getSolidColor();
                    view.setBackgroundColor(getColor(R.color.selected));
                    selectedRow = position;
                    prev = view;
                    setPerson(persons.get(position));
                    binding.btnThem.setEnabled(false);
                } else {
                    if (selectedRow == position) {
                        view.setBackgroundColor(backColor);
                        selectedRow = -1;
                        clear();
                        binding.btnThem.setEnabled(true);
                    } else {
                        prev.setBackgroundColor(backColor);
                        view.setBackgroundColor(getColor(R.color.selected));
                        prev = view;
                        selectedRow = position;
                        clear();
                        setPerson(persons.get(position));
                    }
                }
            }
        });
        */
    }

    // Xoa du lieu
    private void clear() {
        binding.edtName.setText("");
        binding.radGroup.clearCheck();
        binding.chkDocSach.setChecked(false);
        binding.chkDuLich.setChecked(false);
        binding.edtSoThichKhac.setText("");
        binding.edtName.requestFocus();
    }
    private MyPerson getPerson() {
        MyPerson person = new MyPerson();
        person.setName(binding.edtName.getText().toString());
        int checkID = binding.radGroup.getCheckedRadioButtonId();
        // Khong radio button nao duoc chon
        if (checkID == -1) {
            person.setDegree(getResources().getString(R.string.noDegree));
        } else {
            person.setDegree(((RadioButton) binding.getRoot().findViewById(checkID)).getText().toString());
        }
        // So thich
        String soThich = "";
        if (binding.chkDocSach.isChecked()) {
            if (binding.chkDuLich.isChecked()) {
                soThich = binding.chkDocSach.getText() + ", " + binding.chkDuLich.getText();
            } else {
                soThich = binding.chkDocSach.getText().toString();
            }
        } else {
            if (binding.chkDuLich.isChecked()) {
                soThich = binding.chkDuLich.getText().toString();
            }
        }
        if (!binding.edtSoThichKhac.getText().toString().isEmpty()) {
            if (!soThich.isEmpty()) {
                soThich += ", " + binding.edtSoThichKhac.getText();
            } else {
                soThich = binding.edtSoThichKhac.getText().toString();
            }
        }
        if (soThich.isEmpty()) {
            person.setHobbies(getResources().getString(R.string.noHobbies));
        } else {
            person.setHobbies(soThich);
        }
        return person;
    }

    private void setPerson(MyPerson person) {
        binding.edtName.setText(person.getName());
        if (person.getDegree().equalsIgnoreCase(getResources().getString(R.string.radCaoDang))) {
            binding.radCD.setChecked(true);
        } else if (person.getDegree().equalsIgnoreCase(getResources().getString(R.string.radDaiHoc))) {
            binding.radDH.setChecked(true);
        } else if (person.getDegree().equalsIgnoreCase(getResources().getString(R.string.radTrungCap))) {
            binding.radTC.setChecked(true);
        }
        String[] hobbies = person.getHobbies().split(", ");
        int point = 0;
        if (hobbies[point].equalsIgnoreCase(binding.chkDocSach.getText().toString())) {
            binding.chkDocSach.setChecked(true);
            point++;
        }
        if (hobbies[point].equalsIgnoreCase(binding.chkDuLich.getText().toString())) {
            binding.chkDuLich.setChecked(true);
            point++;
        }
        String others = "";
        for (int i = point; i < hobbies.length; ++i) {
            if (others.isEmpty()) {
                if (!hobbies[i].equalsIgnoreCase(getResources().getString(R.string.noHobbies))) {
                    others += hobbies[i];
                }
            } else {
                others += ", " + hobbies[i];
            }
        }
        binding.edtSoThichKhac.setText(others);
    }

    // Hien thi Option Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    // Bat su kien khi tap vao Option Menu

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnuUpdate) {
            // Thuc hien update
            update();
        }
        else if (item.getItemId() == R.id.mnuDelete) {
            // Thuc hien update
            delete();
        }
        else if (item.getItemId() == R.id.mnuFind) {
            // Thuc hien update
            find(binding.edtName.getText().toString(), persons);
        }
        return super.onOptionsItemSelected(item);
    }
    // Dinh nghia cac ham xu ly su kien
    private void update() {
        //Log.d("test", "Update");
        if (selectedRow != -1) {
            MyPerson person = getPerson();
            person.setId(persons.get(selectedRow).getId());
            // Cap nhat vao database
            databaseAPIs.updatePerson(person);
            persons.set(selectedRow, person);
        }
    }
    private void delete() {
        //Log.d("test", "Delete");
        if (selectedRow != -1) {
            // Xoa trong database
            databaseAPIs.deletePerson(persons.get(selectedRow));
        }
    }
    private void find(String name, ArrayList<MyPerson> persons) {
        //Log.d("test", "Find");
        databaseAPIs.findPersonByName(name, persons);
    }
}