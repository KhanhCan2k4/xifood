package vn.edu.tdc.xifood.activities;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.apis.UserAPI;
import vn.edu.tdc.xifood.databinding.SetAddressLayoutBinding;
import vn.edu.tdc.xifood.datamodels.User;

public class SetAddressActivity extends AppCompatActivity {
    private SetAddressLayoutBinding binding;
    private ArrayAdapter<String> availableAddressAdapter;
    private ArrayAdapter<String> searchAddressAdapter;
    private ArrayList<String> addressList = new ArrayList<>();
    private ArrayList<String> listAddressSearch = new ArrayList<>();
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        SharePreference.setSharedPreferences(this);
        binding = SetAddressLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String key = SharePreference.find(SharePreference.USER_TOKEN_KEY);
        geocoder = new Geocoder(this);
        ListView listAddressAvailable = binding.userAddressList;
        ListView listAddressSearchUserSearch = binding.listAddress;
        EditText search = binding.keyword;

        availableAddressAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, addressList);
        searchAddressAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listAddressSearch);
        listAddressAvailable.setAdapter(availableAddressAdapter);
        listAddressSearchUserSearch.setAdapter(searchAddressAdapter);

        UserAPI.find(key, new UserAPI.FirebaseCallback() {
            @Override
            public void onCallback(User user) {
                loadUserAddresses(user);
                setupSearchListener(user);
                setupListClickListeners(user);
            }
        });

        binding.btnBack.setOnClickListener(view -> finish());
    }

    private void loadUserAddresses(User user) {
        addressList.clear();
        if (user.getAddress().size() > 1) {
            for (String address : user.getAddress()) {
                if (!address.equals("bạn chưa có địa chỉ!")) {
                    addressList.add(address);
                }
            }
        } else {
            addressList.add("bạn chưa có địa chỉ!");
        }
        availableAddressAdapter.notifyDataSetChanged();
    }

    private void setupSearchListener(User user) {
        binding.keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAddresses(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchAddresses(s.toString());
            }
        });
    }

    private void searchAddresses(String keyword) {
        listAddressSearch.clear();
        if (!keyword.isEmpty()) {
            listAddressSearch.addAll(getAddress(keyword, geocoder));
        }
        if (listAddressSearch.isEmpty()) {
            listAddressSearch.add("No addresses found");
        }
        searchAddressAdapter.notifyDataSetChanged();
    }

    private void setupListClickListeners(User user) {
        binding.listAddress.setOnItemClickListener((parent, view, position, id) -> {
            String address = listAddressSearch.get(position);
            if (!address.equals("No addresses found") && !user.getAddress().contains(address)) {
                user.getAddress().add(0, address);
                UserAPI.update(user);
                loadUserAddresses(user);
                finish();
            }
        });

        binding.userAddressList.setOnItemClickListener((parent, view, position, id) -> {
            if (!addressList.get(position).equals("bạn chưa có địa chỉ!")) {
                swapElements(user.getAddress(), 0, position);
                UserAPI.update(user);
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

                addressFind.add(fullAddress.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return addressFind;
    }

    public static void swapElements(ArrayList<String> list, int index1, int index2) {
        if (index1 < 0 || index1 >= list.size() || index2 < 0 || index2 >= list.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        String temp = list.get(index1);
        list.set(index1, list.get(index2));
        list.set(index2, temp);
    }
}
