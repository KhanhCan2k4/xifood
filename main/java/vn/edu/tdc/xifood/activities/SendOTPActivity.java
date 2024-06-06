package vn.edu.tdc.xifood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.SecureRandom;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import vn.edu.tdc.xifood.ClientAPI;
import vn.edu.tdc.xifood.databinding.SendOtpLayoutBinding;
import vn.edu.tdc.xifood.mydatamodels.SmsRequest;
import vn.edu.tdc.xifood.views.CancelHeader;

public class SendOTPActivity extends AppCompatActivity {
    private SendOtpLayoutBinding binding;
    private static final String BASE_URL = "https://9lyjp3.api.infobip.com/";
    private static final String API_KEY = "600c58d73f3dde7e5a78ea6eb15beeb6-96a60301-a270-4fb8-840c-8c6eb0df5e66";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SendOtpLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //send button
        binding.btnsendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.phonenumber.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SendOTPActivity.this, "please enter your Phone", Toast.LENGTH_SHORT).show();
                    return;
                }
                //loading
                binding.btnsendOtp.setVisibility(View.INVISIBLE);
                binding.processBar.setVisibility(View.VISIBLE);

//                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
                String number = binding.phonenumber.getText().toString();
                if(number.startsWith("0")){
                    number = number.substring(1);
                }
                String phoneNumber ="84" + number;
                int otp = OTPGenerator.generateOTPInt();
                String messagesBody = "Mã OTP của bạn là: " + otp;
                sendSms(phoneNumber, messagesBody);
                //chuyển trang
                Intent intent = new Intent(SendOTPActivity.this, VerificationActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("otp", otp);
                startActivity(intent);

            }
        }); //end button send OTP

        //cancelled header
        binding.cancelHeader.setCancelListener(new CancelHeader.OnCancelListener() {
            @Override
            public void onCancel(View view) {
                finish();
            }
        });

    }//end onCreate

    //method
    private void sendSms(String phoneNumber, String messageBody) {
        Retrofit retrofit = ClientAPI.getClient(BASE_URL, API_KEY);

        ClientAPI.SmsApiService smsApiService = retrofit.create(ClientAPI.SmsApiService.class);

        SmsRequest.Message.Destination destination = new SmsRequest.Message.Destination(phoneNumber);
        SmsRequest.Message message = new SmsRequest.Message("ServiceSMS", messageBody, new SmsRequest.Message.Destination[]{destination});
        SmsRequest smsRequest = new SmsRequest(new SmsRequest.Message[]{message});

        Call<Void> call = smsApiService.sendSms(smsRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("retrofit", response.toString());
                    Log.d("Retrofit", "SMS sent successfully");
                } else {
                    Log.d("Retrofit", "Failed to send SMS");
                }
                binding.btnsendOtp.setVisibility(View.VISIBLE);
                binding.processBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Retrofit", "Error: " + t.getMessage());
                binding.btnsendOtp.setVisibility(View.VISIBLE);
                binding.processBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    //orther class
    //hàm tạo 1 cái OTP để gửi sms cho số điện thoại
    public static class OTPGenerator {
        private static final int OTP_LENGTH = 6;
        public static String generateOTP() {
            SecureRandom random = new SecureRandom();
            StringBuilder otp = new StringBuilder();
            for (int i = 0; i < OTP_LENGTH; i++) {
                otp.append(random.nextInt(10));
            }
            return otp.toString();
        }
        public static int generateOTPInt(){
            Random random = new Random();
            int otp = 100000 + random.nextInt(900000);
            return otp;
        }
    }


}