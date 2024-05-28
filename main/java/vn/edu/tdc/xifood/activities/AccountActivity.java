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
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import org.springframework.security.crypto.bcrypt.BCrypt;
import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;

import vn.edu.tdc.xifood.apis.ImageStorageReference;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.apis.UserAPI;
import vn.edu.tdc.xifood.databinding.AccountLayoutBinding;
import vn.edu.tdc.xifood.datamodels.User;
import vn.edu.tdc.xifood.views.DayDialogFragment;

public class AccountActivity extends AppCompatActivity {

    public static final String GENDER_MALE = "ma";
    public static final String GENDER_FEMALE = "fe";
    public static final String GENDER_DEFAULT = "de";
    private AccountLayoutBinding binding;
    private User user = new User();
    private Boolean isEditable = false;
    private static final int REQ_CODE = 191019;
    private ActivityResultLauncher<String> imagePicker;

    private String uriImage = "avatars/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = vn.edu.tdc.xifood.databinding.AccountLayoutBinding.inflate(
                getLayoutInflater()
        );

        setContentView(binding.getRoot());

        //update user
        update();

        //setup hide password
        binding.oldPassword.setTransformationMethod(new PasswordTransformationMethod());
        binding.newPassword.setTransformationMethod(new PasswordTransformationMethod());
        binding.confirmNewPassword.setTransformationMethod(new PasswordTransformationMethod());


        //edit button
        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    isEditable = true;

                    setEnableEdit(true);


                } else {
                    if(binding.newPassword.isEnabled()){
                        String newPassword = binding.newPassword.getText().toString();
                        String confirmNewPassword = binding.confirmNewPassword.getText().toString();
                        //nếu như mật khẩu xác nhận không khớp với mật khẩu mới
                        if(!newPassword.equals(confirmNewPassword)){
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(AccountActivity.this);
                            builder1.setMessage("Mật Khẩu xác nhận không khớp với mật khẩu vừa nhập !!!");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Close",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder1.create();
                            alert.show();
                        }else{
                            isEditable = false;
                            //gan lai du lieu tu edit text sang data
                            setUserInEditText();
                            //vo hieu hoa chinh sua
                            setEnableEdit(false);
                        }
                    }else{
                        isEditable = false;
                        //gan lai du lieu tu edit text sang data
                        setUserInEditText();
                        //vo hieu hoa chinh sua
                        setEnableEdit(false);
                    }
                }
            }
        });

        imagePicker = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                if (uri != null) {
//                    uploadImage(result);
                    // Display the image or get the image path
                    String avatarName = "avatars/" + SharePreference.find(SharePreference.USER_TOKEN_KEY);

                    //Update into server
                    ImageStorageReference.upload(avatarName, uri);
                    user.setAvatar(avatarName);
                    UserAPI.update(user);

                    try {
                        ImageStorageReference.setImageInto(binding.imageAvatar, avatarName);
                    } catch (Exception e) {
                        //ignore
                    }
                    //Update into local
                    SharePreference.store(SharePreference.USER_AVATAR, avatarName);
                }
            }
        });


        // chon avatar
        binding.imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        binding.backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditable) {
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
                                    //clear password
                                    binding.oldPassword.setText("");
                                    binding.newPassword.setText("");
                                    binding.confirmNewPassword.setText("");

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
                                  
                                    update();
                                  
                                    Intent intent = new Intent(AccountActivity.this, SettingActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                                    //clear password
                                    binding.oldPassword.setText("");
                                    binding.newPassword.setText("");
                                    binding.confirmNewPassword.setText("");

                                    // chuyen
                                    startActivity(intent);
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder1.create();
                    alert.show();
                } else {
                    //update into server
                    UserAPI.update(user);

                    //update into local
                    SharePreference.setSharedPreferences(AccountActivity.this);

                    SharePreference.store(SharePreference.USER_TOKEN_KEY, user.getKey());
                    SharePreference.store(SharePreference.USER_NAME, user.getFullName());
                    SharePreference.store(SharePreference.USER_EMAIL, user.getEmail());
                    SharePreference.store(SharePreference.USER_GENDER, user.getGender());
                    SharePreference.store(SharePreference.USER_DOB, user.getDayOfBirth());
                    SharePreference.store(SharePreference.USER_AVATAR, user.getAvatar());
                    SharePreference.store(SharePreference.USER_PHONE, user.getPhoneNumber());
                    SharePreference.store(SharePreference.USER_PASS, user.getPassword());

                    //clear password
                    binding.oldPassword.setText("");
                    binding.newPassword.setText("");
                    binding.confirmNewPassword.setText("");
                    Intent intent = new Intent(AccountActivity.this, SettingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    // chuyen
                    startActivity(intent);
                }
            }
        });

        ImageStorageReference.setImageInto(binding.imageAvatar, uriImage);

        //button xác nhận mk cũ mới cho phép thay đổi mật khẩu mới
        binding.btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = binding.oldPassword.getText().toString();
                String storehash = SharePreference.find(SharePreference.USER_PASS);
                if(checkPassword(password, storehash)){
                    binding.newPassword.setEnabled(true);
                    binding.confirmNewPassword.setEnabled(true);
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AccountActivity.this);
                    builder1.setMessage("Mật Khẩu không khớp đúng !!!");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Close",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder1.create();
                    alert.show();
                }
            }
        });

        binding.btnChangePhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, SendOTPActivity.class);
                intent.putExtra("phone", SharePreference.find(SharePreference.USER_PHONE));
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        /* calendar button
        binding.calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DayDialogFragment().show(getSupportFragmentManager(), "datePicker");
            }
        });
        */
    }//end onCreate()


    private void chooseImage() {
        imagePicker.launch("image/*");
    }

    private void update() {

        user = new User();

        //lay user tu local
        user.setKey(SharePreference.find(SharePreference.USER_TOKEN_KEY));
        user.setFullName(SharePreference.find(SharePreference.USER_NAME));
        user.setEmail(SharePreference.find(SharePreference.USER_EMAIL));
        user.setAvatar(SharePreference.find(SharePreference.USER_AVATAR));
        user.setDayOfBirth(SharePreference.find(SharePreference.USER_DOB));
        user.setGender(SharePreference.find(SharePreference.USER_GENDER));
        user.setPhoneNumber(SharePreference.find(SharePreference.USER_PHONE));
        user.setPassword(SharePreference.find(SharePreference.USER_PASS));
        //permission cua user dau mat roi
//        user.setPermistion(SharePreference.findPermission());

        if (user.getGender().equalsIgnoreCase(GENDER_MALE)) {
            binding.genderUser.setSelection(0);
        } else if (user.getGender().equalsIgnoreCase(GENDER_FEMALE)) {
            binding.genderUser.setSelection(1);
        } else {
            binding.genderUser.setSelection(2);
        }

        try {
            ImageStorageReference.setImageInto(binding.imageAvatar, user.getAvatar());
        } catch (Exception e) {
            ImageStorageReference.setImageInto(binding.imageAvatar, "avatars/default.jpg");
        }

        binding.nameUser.setText(user.getFullName());
        binding.dayBornUser.setText(user.getDayOfBirth());
        binding.emailUser.setText(user.getEmail());
        binding.phoneNumberUser.setText(user.getPhoneNumber());
    }

    private void setEnableEdit(boolean isEditable) {
        binding.nameUser.setEnabled(isEditable);
//        binding.bioUser.setEnabled(isEditable);
        binding.genderUser.setEnabled(isEditable);
        binding.dayBornUser.setEnabled(isEditable);
        binding.emailUser.setEnabled(isEditable);
        binding.phoneNumberUser.setEnabled(isEditable);
        binding.oldPassword.setEnabled(isEditable);
        binding.newPassword.setEnabled(false);
        binding.confirmNewPassword.setEnabled(false);
        //calendar button
//        binding.calendarButton.setEnabled(isEditable);
    }

    public boolean checkPassword(String inputPassword, String storedHash) {
        return BCrypt.checkpw(inputPassword, storedHash);
    }
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    public void saveNewPassword(User user){
        String password = binding.newPassword.getText().toString().trim();
        String hashedPassword = hashPassword(password);
        user.setPassword(hashedPassword);

//        UserAPI.store(user);
    }

    // lấy từ giao diện lưu về local
    private void setUserInEditText() {
        user.setKey(SharePreference.find(SharePreference.USER_TOKEN_KEY));
        user.setPassword(SharePreference.find(SharePreference.USER_PASS));
        user.setFullName(binding.nameUser.getText().toString());
        switch (binding.genderUser.getSelectedItemPosition()) {
            case 0:
                user.setGender(GENDER_MALE);
                break;
            case 1:
                user.setGender(GENDER_FEMALE);
                break;
            default:
                user.setGender(GENDER_DEFAULT);
                break;
        }
        user.setAvatar(binding.imageAvatar.getResources().toString());
        user.setDayOfBirth(binding.dayBornUser.getText().toString());
        user.setEmail(binding.emailUser.getText().toString());
        user.setPhoneNumber(binding.phoneNumberUser.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }
}