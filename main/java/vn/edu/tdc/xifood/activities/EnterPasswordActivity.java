package vn.edu.tdc.xifood.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.springframework.security.crypto.bcrypt.BCrypt;
import vn.edu.tdc.xifood.SharePreference;
import vn.edu.tdc.xifood.databinding.EnterPasswordLayoutBinding;
import vn.edu.tdc.xifood.views.CancelHeader;

public class EnterPasswordActivity extends AppCompatActivity {

    private EnterPasswordLayoutBinding binding;
    private static final String BASE_URL = "https://9lyjp3.api.infobip.com/";
    private static final String API_KEY = "600c58d73f3dde7e5a78ea6eb15beeb6-96a60301-a270-4fb8-840c-8c6eb0df5e66";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = EnterPasswordLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cancelHeader.setTitle("Xác Nhận Mật Khẩu");
        binding.cancelHeader.setCancelListener(new CancelHeader.OnCancelListener() {
            @Override
            public void onCancel(View view) {
                finish();
            }
        });

        binding.btnComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loading
                binding.btnComfirm.setVisibility(View.INVISIBLE);
                binding.processBar.setVisibility(View.VISIBLE);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                if (binding.password.getText().toString().trim().isEmpty()){
                    builder.setMessage("Mật khẩu không được để trống");
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    String password = binding.password.getText().toString().trim();
                    String stredHash = SharePreference.find(SharePreference.USER_PASS);
                    if (checkPassword(password, stredHash)){
                        Intent intent = new Intent(EnterPasswordActivity.this, ChangePasswordActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        //nhập đúng mật khẩu cũ
                        finish();
                        startActivity(intent);
                    }else{
                        builder.setMessage("Mật khẩu không khớp");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            }
        });

        binding.lblforgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterPasswordActivity.this, SendOTPActivity.class);
                intent.putExtra("status", 0);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                finish();
                startActivity(intent);

            }
        });

    }// end onCreate

    //sub function
    public boolean checkPassword(String inputPassword, String storedHash) {
        return BCrypt.checkpw(inputPassword, storedHash);
    }

   }//end class