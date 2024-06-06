package vn.edu.tdc.xifood.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import vn.edu.tdc.xifood.API;
import vn.edu.tdc.xifood.SharePreference;
import vn.edu.tdc.xifood.databinding.VerificationLayoutBinding;
import vn.edu.tdc.xifood.mydatamodels.User;
import vn.edu.tdc.xifood.views.CancelHeader;

public class VerificationActivity extends AppCompatActivity {
    private VerificationLayoutBinding binding;
    private int verificationCode;
    private String phoneNumber;
    private int status;
    private String curentOTP;
    private API<User> userAPI = new API<>(User.class, API.USER_TABLE_NAME);
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.verification_layout);
        binding = VerificationLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        verificationCode = getIntent().getIntExtra("otp", 00000);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        status = getIntent().getIntExtra("status", -1);

        switch (status) {
            case 0:
                binding.cancelHeader.setTitle("Xác Nhận số Điện thoại");
                break;
            case 1:
                binding.cancelHeader.setTitle("Đổi Số điện thoại");
                break;
            default:
                Log.e("verify", "can not verify! ");
                break;
        }
        setOTPInput();

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curentOTP = getOTPInput();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                if (curentOTP.length() != 6) { // nếu OTP chưa đủ 6 số
                    builder.setMessage("OTP is not filled");
                    builder.setCancelable(true);
                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkInputEmpty();
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if (curentOTP.equals(verificationCode + "")) { // nếu OTP đã trùng khóp
                    Log.d("clicked", verificationCode + "");
                    if (status == 0) {
                        intent = new Intent(VerificationActivity.this, ChangePasswordActivity.class);
                        startActivity(intent);
                    }
                    else /*if(status == 1) */ {
                        phoneNumber = phoneNumber.replace("84", "0");
                        SharePreference.store(SharePreference.USER_PHONE, phoneNumber);
                        final User user = new User();
                        String userKey = SharePreference.find(SharePreference.USER_TOKEN_KEY);

                        user.setPhoneNumber(phoneNumber);
                        user.setFullName(SharePreference.find(SharePreference.USER_NAME));
                        user.setEmail(SharePreference.find(SharePreference.USER_EMAIL));
                        user.setPassword(SharePreference.find(SharePreference.USER_PASS));
                        user.setGender(SharePreference.find(SharePreference.USER_GENDER));
                        user.setPermistion(SharePreference.find(SharePreference.USER_PERMISSION));
                        user.setDayOfBirth(SharePreference.find(SharePreference.USER_DOB));
                        user.setKey(SharePreference.find(SharePreference.USER_TOKEN_KEY));

                        //cập nhật user mới vào firebase
                        userAPI.update(user, userKey, new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                builder.setMessage("Cập nhật số điện thoại thành công!");
                                builder.setCancelable(true);
                                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        intent = new Intent(VerificationActivity.this, AccountActivity.class);
                                        Log.d("verify", "Cap nhat so dien thoai thanh cong ");
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
//                    startActivity(intent);
                } else /* sai mã OTP */{
                    builder.setMessage("OTP is incorrect!");
                    builder.setCancelable(true);
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkInputEmpty();
                            dialog.cancel();
                        }
                    });
                }
            }
        });
        //cancelled header
        binding.cancelHeader.setCancelListener(new CancelHeader.OnCancelListener() {
            @Override
            public void onCancel(View view) {
                finish();
            }
        });

    }//end onCreate

    //tao OTP input
    private void setOTPInput() {
        ArrayList<EditText> editTexts = new ArrayList<>();
        editTexts.add(binding.inputCode1);
        editTexts.add(binding.inputCode2);
        editTexts.add(binding.inputCode3);
        editTexts.add(binding.inputCode4);
        editTexts.add(binding.inputCode5);
        editTexts.add(binding.inputCode6);

        for (int i = 0; i < editTexts.size(); i++) {
            EditText editText = editTexts.get(i);
            editText.addTextChangedListener(new VerifyTextWatcher(editTexts, i));
        }
    }

    private String getOTPInput() {
        String otp = "";
        otp += binding.inputCode1.getText().toString().trim();
        otp += binding.inputCode2.getText().toString().trim();
        otp += binding.inputCode3.getText().toString().trim();
        otp += binding.inputCode4.getText().toString().trim();
        otp += binding.inputCode5.getText().toString().trim();
        otp += binding.inputCode6.getText().toString().trim();
        return otp.trim();
    }

    private void checkInputEmpty() {
        if (binding.inputCode1.getText().toString().isEmpty()) {
            binding.inputCode1.requestFocus();
        } else if (binding.inputCode2.getText().toString().isEmpty()) {
            binding.inputCode2.requestFocus();
        } else if (binding.inputCode3.getText().toString().isEmpty()) {
            binding.inputCode3.requestFocus();
        } else if (binding.inputCode4.getText().toString().isEmpty()) {
            binding.inputCode4.requestFocus();
        } else if (binding.inputCode5.getText().toString().isEmpty()) {
            binding.inputCode5.requestFocus();
        } else {
            binding.inputCode6.requestFocus();
        }
    }

    //sub class
    public class VerifyTextWatcher implements TextWatcher {
        private ArrayList<EditText> editTexts;
        private int currentIndex;

        public VerifyTextWatcher(ArrayList<EditText> editTexts, int currentIndex) {
            this.editTexts = editTexts;
            this.currentIndex = currentIndex;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 1 && currentIndex < editTexts.size() - 1) {
                // Di chuyển đến EditText tiếp theo nếu có một ký tự được nhập và chưa phải là EditText cuối
                editTexts.get(currentIndex + 1).requestFocus();
            } else if (s.length() == 0 && currentIndex > 0) {
                // Di chuyển đến EditText trước đó nếu không có ký tự nào và không phải là EditText đầu tiên
                editTexts.get(currentIndex - 1).requestFocus();
            }
        }
    }

}//end class