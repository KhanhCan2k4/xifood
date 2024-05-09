package vn.edu.tdc.xifood.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.apis.ImageStorageReference;
import vn.edu.tdc.xifood.apis.UserAPI;
import vn.edu.tdc.xifood.datamodels.User;
import vn.edu.tdc.xifood.databinding.AccountLayoutBinding;

public class AccountActivity extends AppCompatActivity {

    private AccountLayoutBinding binding;
    private User user = new User();
    private Boolean isEditable = false;
    private Uri image;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = vn.edu.tdc.xifood.databinding.AccountLayoutBinding.inflate(
                getLayoutInflater()
        );
        setContentView(binding.getRoot());
        user = dataUser();

        setUser(user);

        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()){
                    isEditable = true;

                    setEnableEdit(true);

                } else {

                    isEditable = false;
                    //gan lai du lieu tu edit text sang data
                    setUserInEditText();

                    //vo hieu hoa chinh sua
                   setEnableEdit(false);
                }
            }
        });

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            if (result.getData() != null) {
                                image = result.getData().getData();
                                Glide.with(getApplicationContext()).load(image).into(binding.imageAvatar);
                            } else {
                                Toast.makeText(AccountActivity.this, "Vui long chon anh", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );


        // chon avatar
        binding.imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
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
                                    Intent intent = new Intent(AccountActivity.this, SettingActivity.class);
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
                                    Intent intent = new Intent(AccountActivity.this, SettingActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    // chuyen
                                    startActivity(intent);
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder1.create();
                    alert.show();

                } else {
                    Intent intent = new Intent(AccountActivity.this, SettingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    // chuyen
                    startActivity(intent);
                }
            }
        });
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("images/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivity(intent);

    }

    private void setUserInEditText() {
        user.setName(binding.nameUser.getText().toString());
        user.setGender(binding.genderUser.getText().toString());
        user.setDayBorn(binding.dayBornUser.getText().toString());
        user.setEmail(binding.emailUser.getText().toString());
        user.setPhoneNumber(binding.phoneNumberUser.getText().toString());
    }

    private void setUser(User user) {
        binding.nameUser.setText(user.getName());
//        binding.genderUser.setText(user.getGender());
        binding.dayBornUser.setText(user.getDayBorn());
        binding.emailUser.setText(user.getEmail());
        binding.phoneNumberUser.setText(user.getPhoneNumber());
    }
    private void setEnableEdit(boolean isEditable){
        binding.nameUser.setEnabled(isEditable);
        binding.genderUser.setEnabled(isEditable);
        binding.dayBornUser.setEnabled(isEditable);
        binding.emailUser.setEnabled(isEditable);
        binding.phoneNumberUser.setEnabled(isEditable);
    }
}