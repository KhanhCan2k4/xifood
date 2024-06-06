package vn.edu.tdc.xifood.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.springframework.security.crypto.bcrypt.BCrypt;

import vn.edu.tdc.xifood.API;
import vn.edu.tdc.xifood.SharePreference;
import vn.edu.tdc.xifood.databinding.ChangePasswordLayoutBinding;
import vn.edu.tdc.xifood.mydatamodels.User;

public class ChangePasswordActivity extends AppCompatActivity {

    private ChangePasswordLayoutBinding binding;
    private API<User> userAPI = new API<>(User.class, API.USER_TABLE_NAME);
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ChangePasswordLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Ẩn mật khẩu
        binding.newPassword.setTransformationMethod(new PasswordTransformationMethod());
        binding.confirmNewPassword.setTransformationMethod(new PasswordTransformationMethod());

        intent = new Intent(ChangePasswordActivity.this, AccountActivity.class);
        //khoi tao thong bao cho nguoi dung
        //xac nhan mat khau
        binding.btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                String password = binding.newPassword.getText().toString().trim().toLowerCase();
                String confirmPassword = binding.confirmNewPassword.getText().toString().trim().toLowerCase();
                //kiem tra do dai cua pass
                if(password.length() <6 ){
                    builder.setTitle("Thông báo !!!");
                    builder.setMessage("Mật khẩu phải ít nhất có 6 kí tự");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else{
                    //kiem tra mat khau co khop voi xac nhan mat khau
                    if(!password.contains(confirmPassword)){
                        builder.setTitle("Thông báo !!!");
                        builder.setMessage("Mật khẩu được nhập không khớp với nhau");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else{
                        final User user = new User();
                        String userKey = SharePreference.find(SharePreference.USER_TOKEN_KEY);
                        user.setPassword(hashPassword(password));

                        user.setPhoneNumber(SharePreference.find(SharePreference.USER_PHONE));
                        user.setFullName(SharePreference.find(SharePreference.USER_NAME));
                        user.setEmail(SharePreference.find(SharePreference.USER_EMAIL));
                        user.setGender(SharePreference.find(SharePreference.USER_GENDER));
                        user.setPermistion(SharePreference.find(SharePreference.USER_PERMISSION));
                        user.setDayOfBirth(SharePreference.find(SharePreference.USER_DOB));
                        user.setKey(SharePreference.find(SharePreference.USER_TOKEN_KEY));

                        //cập nhật user mới vào firebase
                        userAPI.update(user, userKey, new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                builder.setMessage("Cập nhật mật khẩu thành công!");
                                builder.setCancelable(true);
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                    intent = new Intent(ChangePasswordActivity.this, AccountActivity.class);
                                        Log.d("verify", "Cập nhật mật khẩu mới thành công!");
                                        finish();
                                        startActivity(intent);
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
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

            }
        });
        // quay lai man hinh truoc do
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}