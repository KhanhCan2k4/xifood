package vn.edu.tdc.xifood.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vn.edu.tdc.xifood.API;
import vn.edu.tdc.xifood.SharePreference;
import vn.edu.tdc.xifood.databinding.SetAddressLayoutBinding;
import vn.edu.tdc.xifood.mydatamodels.User;

public class SetAddressActivity extends AppCompatActivity {
    private SetAddressLayoutBinding binding;
    private ArrayAdapter<String> searchAddressAdapter;
    private ArrayAdapter<String> userAddressAdapter;
    private ArrayList<String> addressList;
    private ArrayList<String> userAddressList;
    private Geocoder geocoder;
    private API<User> userAPI;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        SharePreference.setSharedPreferences(this);
        binding = SetAddressLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addressList = new ArrayList<>();
        userAddressList = new ArrayList<>();

        String key = SharePreference.find(SharePreference.USER_TOKEN_KEY);
        geocoder = new Geocoder(this, Locale.getDefault());

        searchAddressAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, addressList);
        userAddressAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userAddressList);

        binding.listAddress.setAdapter(searchAddressAdapter);
        binding.userAddressList.setAdapter(userAddressAdapter);

        binding.listAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String address = addressList.get(i);

                user.getAddress().add(0, address);

                //just allow 10 address
                while (user.getAddress().size() > 10) {
                    user.getAddress().remove(user.getAddress().size() - 1);
                }

                //update database
                userAPI.update(user, user.getKey(),
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(SetAddressActivity.this, "Bạn vừa chọn địa chỉ", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                Toast.makeText(SetAddressActivity.this, "Không thể cập nhật dịa chỉ", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Intent intent = new Intent(SetAddressActivity.this, PurchaseActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                                SetAddressActivity.this.startActivity(intent);
                                SetAddressActivity.this.finish();
                            }
                        }
                );
            }
        });

        binding.userAddressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //active address and back
                String address = userAddressList.get(i);
                userAddressList.remove(i);
                userAddressList.add(0, address);
                user.setAddress(userAddressList);

                //update database
                userAPI.update(user, user.getKey(),
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(SetAddressActivity.this, "Bạn vừa chọn địa chỉ", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                Toast.makeText(SetAddressActivity.this, "Không thể cập nhật dịa chỉ", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Intent intent = new Intent(SetAddressActivity.this, PurchaseActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                                SetAddressActivity.this.startActivity(intent);
                                SetAddressActivity.this.finish();
                            }
                        }
                );
            }
        });

        binding.userAddressList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //active address and remove
                if (i <= userAddressList.size() - 1) {
                    userAddressList.remove(i);
                    userAddressAdapter.notifyDataSetChanged();
                    Toast.makeText(SetAddressActivity.this, "Bạn vừa xoá địa chỉ", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        userAPI = new API<>(User.class, API.USER_TABLE_NAME);

        userAPI.find(key, new API.FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                if (object != null) {
                    user = (User) object;
                    userAddressList.clear();
                    userAddressList.addAll(user.getAddress());
                    userAddressAdapter.notifyDataSetChanged();
                }
            }
        });

        binding.btnBack.setOnClickListener(view -> finish());

        binding.keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addressList.clear();
                try {
                    List<Address> searchedAddresses = geocoder.getFromLocationName(charSequence.toString(), 15);
                    for (Address a : searchedAddresses) {
                        StringBuilder fullAddress = new StringBuilder();

                        if (a.getSubThoroughfare() != null) {
                            fullAddress.append(a.getSubThoroughfare()).append(" ");
                        }
                        if (a.getThoroughfare() != null) {
                            fullAddress.append(a.getThoroughfare()).append(", ");
                        }
                        if (a.getSubAdminArea() != null) {
                            fullAddress.append(a.getSubAdminArea()).append(", ");
                        }
                        if (a.getLocality() != null) {
                            fullAddress.append(a.getLocality()).append(", ");
                        }
                        if (a.getAdminArea() != null) {
                            fullAddress.append(a.getAdminArea()).append(", ");
                        }
                        if (a.getCountryName() != null) {
                            fullAddress.append(a.getCountryName());
                        }

                        addressList.add(fullAddress.toString());
                        searchAddressAdapter.notifyDataSetChanged();
                    }
                } catch (IOException e) {
                    //ignore
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
