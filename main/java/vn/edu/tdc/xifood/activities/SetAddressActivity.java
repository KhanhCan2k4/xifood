package vn.edu.tdc.xifood.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.apis.UserAPI;
import vn.edu.tdc.xifood.databinding.PurchaseLayoutBinding;
import vn.edu.tdc.xifood.databinding.SetAddressLayoutBinding;
import vn.edu.tdc.xifood.datamodels.User;

public class SetAddressActivity extends AppCompatActivity {
    private SetAddressLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        SharePreference.setSharedPreferences(this);
        binding = SetAddressLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String key = SharePreference.find(SharePreference.USER_TOKEN_KEY);
        final String[] keyWord = {""};
        final ArrayList<String>[] listAddressSearch = new ArrayList[]{new ArrayList<>()};
        ListView listAddressAvaiable = binding.userAddressList;
        ListView listAddressSearchUserSearch = binding.listAddress;
        EditText search = binding.keyword;
        Geocoder geocoder = new Geocoder(this);
        UserAPI.find(key, new UserAPI.FirebaseCallback() {
            @Override
            public void onCallback(User user) {
              //  Log.d("sodiachinguoidung", user.getAddress().size() + "");
                //hiển thị danh sách địa chỉ đã có của người dùng
                if (user.getAddress().size() > 1) {
                    Log.d("vong1", "onCallback: yes" );
                    String s = "bạn chưa có địa chỉ!";
                    ArrayList<String> addressOfUsser = user.getAddress();
                    for(int i=1; i<user.getAddress().size();i++)
                   {
                       Log.d("vong1a", "onCallback: "+i);
                    if(user.getAddress().get(i).equals(s)==true)
                    {
//                        Log.d("thuchiensosanhss", "stt"+i+"-"+user.getAddress().get(i));
//                        Log.d("thuchiensosanh", "onItemClick: "+user.getAddress().get(0).equals(s));
                        addressOfUsser.remove(i);
                  }
                }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SetAddressActivity.this, android.R.layout.simple_list_item_1, addressOfUsser);
                    listAddressAvaiable.setAdapter(arrayAdapter);
                }
                else {
                    String s = "bạn chưa có địa chỉ!";
                    ArrayList<String> addressOfUsser = new ArrayList<>();
                    addressOfUsser.add(s);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SetAddressActivity.this, android.R.layout.simple_list_item_1, addressOfUsser);
                    listAddressAvaiable.setAdapter(arrayAdapter);
                }
                search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        keyWord[0] = search.getText() + "";
                        if (keyWord[0].length() > 0) {
                            listAddressSearch[0] = getAddress(keyWord[0], geocoder);
                        }
                        if (listAddressSearch[0].size() == 0) {
                            listAddressSearch[0].add(keyWord[0]);
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SetAddressActivity.this, android.R.layout.simple_list_item_1, listAddressSearch[0]);
                        listAddressSearchUserSearch.setAdapter(arrayAdapter);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        keyWord[0] = search.getText() + "";
                        if (keyWord[0].length() > 0) {
                            listAddressSearch[0] = getAddress(keyWord[0], geocoder);

                        }
                        if (listAddressSearch[0].size() == 0) {
                            listAddressSearch[0].add(keyWord[0]);
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SetAddressActivity.this, android.R.layout.simple_list_item_1, listAddressSearch[0]);
                        listAddressSearchUserSearch.setAdapter(arrayAdapter);
                    }
                });
                listAddressSearchUserSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String address = listAddressSearchUserSearch.getItemAtPosition(position).toString();
                        user.getAddress().add(0, address);
                        UserAPI.update(user);
                        Log.d("userAddress", user.getAddress().toString());
                        Log.d("totaluserAddress", user.getAddress().size() + "");
                        finish();

                    }
                });
                listAddressAvaiable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // user.getAddress().remove(0);

                        swapElements(user.getAddress(),0,position);
                        //user.getAddress().add(position,a2);
//                        Intent intent = new Intent(SetAddressActivity.this, PurchaseActivity.class);
                        UserAPI.update(user);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//                        startActivity(intent);

                        finish();


                    }
                });
            }
        });

        // chuyen lai mang hinh truoc do
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public ArrayList<String> getAddress(String key, Geocoder geocoder) {
        ArrayList<String> addressFind = new ArrayList<>();
        try {
            List<Address> addressList = geocoder.getFromLocationName(key, 15);
            for (Address a : addressList) {
                StringBuilder fullAddress = new StringBuilder();

                // Lấy số nhà
                if (a.getSubThoroughfare() != null) {
                    fullAddress.append(a.getSubThoroughfare()).append(" ");
                }

                // Lấy tên đường
                if (a.getThoroughfare() != null) {
                    fullAddress.append(a.getThoroughfare()).append(", ");
                }

                // Lấy quận/huyện
                if (a.getSubAdminArea() != null) {
                    fullAddress.append(a.getSubAdminArea()).append(", ");
                }

                // Lấy thành phố
                if (a.getLocality() != null) {
                    fullAddress.append(a.getLocality()).append(", ");
                }

                // Lấy tỉnh/thành phố
                if (a.getAdminArea() != null) {
                    fullAddress.append(a.getAdminArea()).append(", ");
                }

                // Lấy quốc gia
                if (a.getCountryName() != null) {
                    fullAddress.append(a.getCountryName());
                }

                // Thêm địa chỉ đầy đủ vào danh sách
                addressFind.add(fullAddress.toString());
                Log.d("ADDRESS", "onCreate: " + fullAddress.toString());

                //       addressFind.add(a.getAddressLine(0));
                //       Log.d("ADDRESS", "onCreate: " + a.getAddressLine(0));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return addressFind;
    }

    // Phương thức đổi chỗ hai phần tử trong ArrayList
    public static void swapElements(ArrayList<String> list, int index1, int index2) {
        // Kiểm tra các chỉ số hợp lệ
        if (index1 < 0 || index1 >= list.size() || index2 < 0 || index2 >= list.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        // Đổi chỗ hai phần tử
        String temp = list.get(index1);  // Lưu giá trị phần tử tại index1 vào biến tạm
        list.set(index1, list.get(index2));  // Đặt giá trị phần tử tại index2 vào index1
        list.set(index2, temp);  // Đặt giá trị tạm vào index2
    }
}
