package vn.edu.tdc.xifood.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.edu.tdc.xifood.apis.ImageStorageReference;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.apis.UserAPI;
import vn.edu.tdc.xifood.databinding.AccountLayoutBinding;
import vn.edu.tdc.xifood.datamodels.User;
import vn.edu.tdc.xifood.views.DayDialogFragment;

public class AccountActivity extends AppCompatActivity {

    private AccountLayoutBinding binding;
    private User user = new User();
    private Boolean isEditable = false;
    private Uri image;
    ActivityResultLauncher<Intent> activityResultLauncher;
    public static final String GENDER_DEFAULT = "de";
    public static final String GENDER_FEMALE = "fe";
    public static final String GENDER_MALE = "me";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern1 = Pattern.compile(EMAIL_PATTERN);
    private static final String NUMERIC_PATTERN = "^\\d{10}$";

    private static final Pattern pattern2 = Pattern.compile(NUMERIC_PATTERN);
    private TextView textViewDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = vn.edu.tdc.xifood.databinding.AccountLayoutBinding.inflate(
                getLayoutInflater()
        );
        setContentView(binding.getRoot());
        textViewDate = binding.textViewDate;
        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if ( isEditable)
               {
                   showDatePickerDialog();
               }

            }
        });


        //update user
        update();

        //setup hide password
        binding.oldPassword.setTransformationMethod(new PasswordTransformationMethod());
        binding.newPassword.setTransformationMethod(new PasswordTransformationMethod());
        binding.confirmNewPassword.setTransformationMethod(new PasswordTransformationMethod());

//         setUser(user);
//        binding.imageUser.setImageResource(user.getImage());

        //edit button
        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    isEditable = true;

                    setEnableEdit(true);


                } else {
                    if (binding.newPassword.isEnabled()) {
                        String newPassword = binding.newPassword.getText().toString();
                        if(newPassword.length()<6||newPassword.length()>24)
                        {
                            Toast.makeText(AccountActivity.this, "Mật khẩu phải từ 6 đến 24 kí tự", Toast.LENGTH_SHORT).show();

                        }
                        else {


                        String confirmNewPassword = binding.confirmNewPassword.getText().toString();
                        //nếu như mật khẩu xác nhận không khớp với mật khẩu mới
                        if (!newPassword.equals(confirmNewPassword)) {
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
                        } else {
                            saveNewPassword(user);
                            isEditable = false;
                            //gan lai du lieu tu edit text sang data
                            setUserInEditText();
                            //vo hieu hoa chinh sua
                            setEnableEdit(false);
                        }
                        }
                    } else {
                        isEditable = false;
                        //gan lai du lieu tu edit text sang data
                        setUserInEditText();
                        //vo hieu hoa chinh sua
                        setEnableEdit(false);
                    }
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
        //button xác nhận mk cũ mới cho phép thay đổi mật khẩu mới
        binding.btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = binding.oldPassword.getText().toString();
                String storehash = SharePreference.find(SharePreference.USER_PASS);
                if (checkPassword(password, storehash)) {
                    binding.newPassword.setEnabled(true);
                    binding.confirmNewPassword.setEnabled(true);
                } else {
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

    }//end onCreate()


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("images/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        activityResultLauncher.launch(intent);
        startActivity(intent);

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
        if (isValidEmail(binding.emailUser.getText().toString()) == true) {
            UserAPI.all(new UserAPI.FirebaseCallbackAll() {
                @Override
                public void onCallback(ArrayList<User> users) {
                    Boolean checkemail=true;
                    for (User u:users) {
                        if(binding.emailUser.getText().toString().equals(u.getEmail())){
                            checkemail= false;
                        }
                    }
                    if(checkemail== true)
                    {
                        user.setEmail(binding.emailUser.getText().toString());
                    }
                    else {
                        if(user.getEmail().equals(binding.emailUser.getText().toString()))
                        {
                            binding.emailUser.setText(user.getEmail());
                        }
                        else {
                            if (!user.getEmail().equals(binding.emailUser.getText())) {
                                Toast.makeText(AccountActivity.this, "Email đã được tạo bởi tài khoản khác", Toast.LENGTH_LONG).show();
                                if (user.getEmail() == null) {
                                    binding.emailUser.setText("");
                                } else {
                                    binding.emailUser.setText(user.getEmail());
                                }
                            } else {
                                binding.emailUser.setText(user.getEmail());
                            }
                        }
                    }

                }
            });

        } else {
            Toast.makeText(AccountActivity.this, "Email vừa nhập không đúng định dạng", Toast.LENGTH_LONG).show();

            if (user.getEmail() == null) {
                binding.emailUser.setText("");
            } else {
                binding.emailUser.setText(user.getEmail());
            }
        }
        if (isValidNumericString(binding.phoneNumberUser.getText().toString())) {
            user.setPhoneNumber(binding.phoneNumberUser.getText().toString());
        } else {
            if(user.getPhoneNumber().equals(binding.phoneNumberUser.getText().toString()))
            {
                binding.phoneNumberUser.setText(user.getPhoneNumber());
            }
            else {
                Toast.makeText(AccountActivity.this, "Số điện thoại vừa nhập không đúng định dạng", Toast.LENGTH_LONG).show();
                if (user.getPhoneNumber() == null) {
                    binding.phoneNumberUser.setText("");
                } else {
                    binding.phoneNumberUser.setText(user.getPhoneNumber());

                }
            }

        }


        user.setDayOfBirth(binding.textViewDate.getText().toString());
      //  saveNewPassword(user);
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
        binding.textViewDate.setText(user.getDayOfBirth());
        binding.emailUser.setText(user.getEmail());
        binding.phoneNumberUser.setText(user.getPhoneNumber());
    }

    private void setEnableEdit(boolean isEditable) {
        binding.nameUser.setEnabled(isEditable);
//        binding.bioUser.setEnabled(isEditable);
        binding.genderUser.setEnabled(isEditable);
        binding.textViewDate.setEnabled(isEditable);
        binding.emailUser.setEnabled(isEditable);
        binding.phoneNumberUser.setEnabled(isEditable);
        binding.oldPassword.setEnabled(isEditable);
        binding.newPassword.setEnabled(false);
        binding.confirmNewPassword.setEnabled(false);

    }

    public boolean checkPassword(String inputPassword, String storedHash) {
        return BCrypt.checkpw(inputPassword, storedHash);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void saveNewPassword(User user) {
        String password = binding.newPassword.getText().toString().trim();
        String hashedPassword = hashPassword(password);
        user.setPassword(hashedPassword);

//        UserAPI.store(user);
    }

    @Override
    protected void onResume() {
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

    //kiem tra dinh dang email
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern1.matcher(email);
        return matcher.matches();
    }

    //kiem tra dinh dang so dien thoai
    public static boolean isValidNumericString(String str) {
        if (str == null) {
            return false;
        }
        Matcher matcher = pattern2.matcher(str);
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
                        textViewDate.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }
}