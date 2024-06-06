package vn.edu.tdc.xifood.activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.edu.tdc.xifood.API;
import vn.edu.tdc.xifood.ImageStorageReference;
import vn.edu.tdc.xifood.SharePreference;
import vn.edu.tdc.xifood.databinding.AccountLayoutBinding;
import vn.edu.tdc.xifood.mydatamodels.User;

public class AccountActivity extends AppCompatActivity {

    private AccountLayoutBinding binding;
    private User user = new User();
    private Boolean isEditable = false;
    public static final String GENDER_DEFAULT = "US_DE";
    public static final String GENDER_FEMALE = "US_FE";
    public static final String GENDER_MALE = "US_MA";
    //regex
//    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
//    private static final Pattern email_regex = Pattern.compile(EMAIL_PATTERN);
//    private static final String NUMERIC_PATTERN = "^\\d{10}$";
//    private static final Pattern phone_regex = Pattern.compile(NUMERIC_PATTERN);
//    private TextView textViewDate;
    private String imageUri = "avatars/";
    private ActivityResultLauncher<String> imagePicker;

    private API<User> userAPI = new API<User>(User.class, API.USER_TABLE_NAME);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AccountLayoutBinding.inflate(
                getLayoutInflater()
        );
        setContentView(binding.getRoot());
        binding.textViewDate.setActivated(isEditable);
        binding.genderUser.setActivated(isEditable);

        //update user
        update();
        //edit button
        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    isEditable = true;
                    setEnableEdit(true);
                }else{
                    isEditable = false;
                    setEnableEdit(false);
                    setUserInEditText();
                }
            }
        });
        binding.textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditable) {
                    showDatePickerDialog();
                }
            }
        });
        // chon avatar
        binding.imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
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

                                    // chuyen
                                    startActivity(intent);
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder1.create();
                    alert.show();
                } else {
                    //update into server
                    userAPI.update(user, SharePreference.find(SharePreference.USER_TOKEN_KEY), new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            //update into local
                            Log.d("account", "onSuccess: ");
                            SharePreference.setSharedPreferences(AccountActivity.this);
                            SharePreference.store(SharePreference.USER_TOKEN_KEY, user.getKey());
                            SharePreference.store(SharePreference.USER_NAME, user.getFullName());
                            SharePreference.store(SharePreference.USER_EMAIL, user.getEmail());
                            SharePreference.store(SharePreference.USER_GENDER, user.getGender());
                            SharePreference.store(SharePreference.USER_DOB, user.getDayOfBirth());
                            SharePreference.store(SharePreference.USER_PHONE, user.getPhoneNumber());
                            SharePreference.store(SharePreference.USER_PASS, user.getPassword());

                            Intent intent = new Intent(AccountActivity.this, SettingActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            // chuyen
                            startActivity(intent);
                        }
                    }, new OnCanceledListener() {
                        @Override
                        public void onCanceled() {

                        }
                    }, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                        }
                    });


                }
            }
        });

        // Nút Chuyển đến trang thay đổi số điện thoại
        binding.btnChangePhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, SendOTPActivity.class);
                intent.putExtra("phone", SharePreference.find(SharePreference.USER_PHONE));
                // chuyen sang xac nhan so dien thoai de thay doi so dien thoai moi
                intent.putExtra("status", 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        // Nút chuyển đến trang thay đổi mật khẩu
        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, EnterPasswordActivity.class);
                intent.putExtra("phone", SharePreference.find(SharePreference.USER_PHONE));
                // chuyen sang nhap mat khau cu de gui otp ve so dien thoai de thay doi mat khau moi
//                intent.putExtra("status", 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        //chọn avatar mới
        imagePicker = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                if (uri != null) {
//                    uploadImage(result);
                    // Display the image or get the image path
                    String avatarName = "avatars/" + SharePreference.find(SharePreference.USER_TOKEN_KEY);

                    //Update into server
                    ImageStorageReference.upload(avatarName, uri);

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

        binding.imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        //Nút Chuyển đến trang thay đổi email
        binding.btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AccountActivity.this, "Coming soon..." , Toast.LENGTH_LONG).show();
            }
        });

    }//end onCreate()

    private void chooseImage() {
        imagePicker.launch("image/*");
    }


    private void setUserInEditText() {
        user.setKey(SharePreference.find(SharePreference.USER_TOKEN_KEY));
        if (binding.nameUser.getText().toString().length() < 6 || binding.nameUser.getText().toString().length() > 36 || containsSpecialCharacter(binding.nameUser.getText().toString())) {
            Toast.makeText(AccountActivity.this, "Tên phải từ 6 đến 36 kí tự và không có kí tự đặc biệc", Toast.LENGTH_LONG).show();
            binding.nameUser.setText(user.getFullName());
        } else {
            user.setFullName(binding.nameUser.getText().toString());
        }

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
        user.setDayOfBirth(binding.textViewDate.getText().toString());
    }

    private void update() {

//        user = new User();
        //lay user tu local
        user.setKey(SharePreference.find(SharePreference.USER_TOKEN_KEY));
        user.setFullName(SharePreference.find(SharePreference.USER_NAME));
        user.setEmail(SharePreference.find(SharePreference.USER_EMAIL));
        user.setDayOfBirth(SharePreference.find(SharePreference.USER_DOB));
        user.setGender(SharePreference.find(SharePreference.USER_GENDER));
        user.setPhoneNumber(SharePreference.find(SharePreference.USER_PHONE));
        user.setPassword(SharePreference.find(SharePreference.USER_PASS));
        user.setPermistion(SharePreference.findPermission());

        if (user.getGender().equalsIgnoreCase(GENDER_MALE)) {
            binding.genderUser.setSelection(0);
        } else if (user.getGender().equalsIgnoreCase(GENDER_FEMALE)) {
            binding.genderUser.setSelection(1);
        } else {
            binding.genderUser.setSelection(2);
        }

        ImageStorageReference.setImageInto(binding.imageAvatar, "avatars/" + user.getKey());
        binding.nameUser.setText(user.getFullName());
        binding.textViewDate.setText(user.getDayOfBirth());
        binding.emailUser.setText(user.getEmail());
        binding.phoneNumberUser.setText(user.getPhoneNumber());
    }

    private void setEnableEdit(boolean isEditable) {
        binding.nameUser.setEnabled(isEditable);
        binding.genderUser.setEnabled(isEditable);
        binding.genderUser.setActivated(isEditable);
        binding.textViewDate.setEnabled(isEditable);
        binding.textViewDate.setActivated(isEditable);
    }

    @Override
    protected void onResume() {
        ImageStorageReference.setImageInto(binding.imageAvatar, imageUri);
        super.onResume();
        update();
    }

    public static boolean containsSpecialCharacter(String text) {
        // Biểu thức chính quy để tìm các ký tự đặc biệt !@#$%^&*()
        String specialCharacterRegex = ".*[!@#$%^&*()].*";
        Pattern pattern = Pattern.compile(specialCharacterRegex);
        Matcher matcher = pattern.matcher(text);

        // Kiểm tra chuỗi text với biểu thức chính quy
        return matcher.matches();
    }


    //cho người dùng chọn ngày
    private void showDatePickerDialog() {
        // Lấy ngày hiện tại
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AccountActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        binding.textViewDate.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }
}