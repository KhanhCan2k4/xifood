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

import com.google.android.gms.tasks.OnCompleteListener;
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
    private String curentOTP;
    private boolean isFromPurchase = false;
    public  static  final  String FROM_PURCHASE_KEY = "FROM_PURCHASE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = VerificationLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharePreference.setSharedPreferences(this);

        verificationCode = getIntent().getIntExtra("otp", 00000);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        setOTPInput();

        Intent intent = getIntent();
        isFromPurchase = intent.getBooleanExtra(FROM_PURCHASE_KEY, false);

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curentOTP = getOTPInput();
                Log.d("click", curentOTP);
                Log.d("clicked", verificationCode + "");
                if (curentOTP.length() != 6) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
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
                } else if (curentOTP.equals(verificationCode + "")) {
                    //update user
                    String userKey = SharePreference.find(SharePreference.USER_TOKEN_KEY);

                    API<User> userAPI = new API<User>(User.class, API.USER_TABLE_NAME);
                    userAPI.find(userKey, new API.FirebaseCallback() {
                        @Override
                        public void onCallback(Object object) {
                            if (object != null) {
                                User user = (User) object;

                                user.setPhoneNumber(phoneNumber);

                                userAPI.update(user, userKey, null, null, new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                       Intent intent;

                                       if (isFromPurchase) {
                                           intent = new Intent(VerificationActivity.this, PurchaseActivity.class);
                                       } else {
                                           intent = new Intent(VerificationActivity.this, AccountActivity.class);
                                       }

                                       intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                                       startActivity(intent );
                                    }
                                });
                            }
                        }
                    }, true);
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