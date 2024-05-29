package vn.edu.tdc.xifood.staffProcessing;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.activities.AccountActivity;
import vn.edu.tdc.xifood.activities.SettingActivity;
import vn.edu.tdc.xifood.databinding.AccountLayoutBinding;
import vn.edu.tdc.xifood.models.User;

public class AccountStaffActivity extends AppCompatActivity {
    private AccountLayoutBinding binding;
    private User user = new User();
    private Boolean isEditable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = vn.edu.tdc.xifood.databinding.AccountLayoutBinding.inflate(
                getLayoutInflater()
        );
        setContentView(binding.getRoot());
        user = dataUser();

        setUser(user);
//        binding.imageUser.setImageResource(user.getImage());


        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()){
                    isEditable = true;

                    setEnableEdit(true);


                }else {
                    isEditable = false;
                    //gan lai du lieu tu edit text sang data
                    setUserInEditText();

                    //vo hieu hoa chinh sua
                    setEnableEdit(false);
                }
            }
        });

        binding.backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditable){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                    builder1.setMessage("Do you want to save change? ");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    setUserInEditText();
                                    binding.editBtn.setSelected(false);
                                    setEnableEdit(false);
                                    isEditable = false;
                                    Intent intent = new Intent( AccountStaffActivity.this, MainStaffActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    // chuyen
                                    startActivity(intent);
                                    dialog.cancel();
                                }
                            });
                    builder1.setNeutralButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    binding.editBtn.setSelected(false);
                                    isEditable = false;
                                    setEnableEdit(false);
                                    setUser(user);
                                    Intent intent = new Intent( AccountStaffActivity.this, MainStaffActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    // chuyen
                                    startActivity(intent);
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder1.create();
                    alert.show();
                }
                else{
                    Intent intent = new Intent( AccountStaffActivity.this, MainStaffActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    // chuyen
                    startActivity(intent);
                }
            }
        });
    }

    public User dataUser(){
        User user1 = new User();
        user1.setId(1);
        user1.setAvatar("");
        user1.setName("Dylan");
        user1.setBio("2019 kết hôn với Khoai lang Thang 2022 kết hôn với Quân Ap, 2023 kết hôn với Mono");
        user1.setGender("Bisexcent");
        user1.setDayBorn("28/01/2004");
        user1.setEmail("vandupluss@gmail.com");
        user1.setPhoneNumber("085850234");
        return user1;
    }

    private void setUserInEditText(){
        user.setName(binding.nameUser.getText().toString());
//        user.setGender(binding.genderUser.getText().toString());
        user.setDayBorn(binding.textViewDate.getText().toString());
        user.setEmail(binding.emailUser.getText().toString());
        user.setPhoneNumber(binding.phoneNumberUser.getText().toString());
    }
    private void setUser(User user){
        binding.nameUser.setText(user.getName());
//        binding.genderUser.setText(user.getGender());
        binding.textViewDate.setText(user.getDayBorn());
        binding.emailUser.setText(user.getEmail());
        binding.phoneNumberUser.setText(user.getPhoneNumber());
    }
    private void setEnableEdit(boolean isEditable){
        binding.nameUser.setEnabled(isEditable);
        binding.genderUser.setEnabled(isEditable);
        binding.textViewDate.setEnabled(isEditable);
        binding.emailUser.setEnabled(isEditable);
        binding.phoneNumberUser.setEnabled(isEditable);
    }
}