package vn.edu.tdc.xifood;

import java.security.SecureRandom;
import java.util.Random;

public class OTPGenerator {
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