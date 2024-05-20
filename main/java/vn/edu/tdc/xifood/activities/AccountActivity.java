package vn.edu.tdc.xifood.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import vn.edu.tdc.xfood.databinding.AccountLayoutBinding;
import vn.edu.tdc.xifood.apis.ImageStorageReference;
import vn.edu.tdc.xifood.apis.SharePreference;
import vn.edu.tdc.xifood.apis.UserAPI;
import vn.edu.tdc.xifood.datamodels.User;

public class AccountActivity extends AppCompatActivity {

    public static final String GENDER_MALE = "ma";
    public static final String GENDER_FEMALE = "fe";
    public static final String GENDER_DEFAULT = "de";
    private AccountLayoutBinding binding;
    private User user = new User();
    private Boolean isEditable = false;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private static final int REQ_CODE = 191019;

    // nhi

    private ActivityResultLauncher<String> imagePicker;

    private String uriImage = "avatars/";

    // nhi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AccountLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // nhi

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

        binding.imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        // nhi

        //update user
        updatde();

        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    // hiện dấu tích cho phép sửa
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


        // change avatar
//        binding.imageAvatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ContextCompat.checkSelfPermission(AccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
//                } else {
//                    chooseImage();
//                }
//            }
//        });

//        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//            if (isGranted) {
//                chooseImage();
//            } else {
//                // Handle the case where permission is denied.
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(AccountActivity.this);
//                builder1.setMessage("Bạn không có quyền chọn ảnh!!!");
//                builder1.setCancelable(true);
//
//                builder1.setPositiveButton(
//                        "Close",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                AlertDialog alert = builder1.create();
//                alert.show();
//            }
//        });

        binding.backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditable) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                    builder1.setMessage("Do you want to save change? ");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            setUserInEditText();
                            binding.editBtn.setSelected(false);
                            setEnableEdit(false);
                            isEditable = false;

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

                            Intent intent = new Intent(AccountActivity.this, SettingActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            // chuyen
                            startActivity(intent);
                            dialog.cancel();
                        }
                    });
                    builder1.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            binding.editBtn.setSelected(false);
                            isEditable = false;
                            setEnableEdit(false);
                            updatde();
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

                    Intent intent = new Intent(AccountActivity.this, SettingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    // chuyen
                    startActivity(intent);
                }
            }
        });

        ImageStorageReference.setImageInto(binding.imageAvatar, uriImage);
    }


    private void chooseImage() {
        imagePicker.launch("image/*");
    }



    private void updateImageView(String imageUrl) {
        Glide.with(this).load(imageUrl).into(binding.imageAvatar);
    }

    //    private void chooseImage() {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, REQ_CODE);
//    }

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            chooseImage();
        }
    }
     */

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            // Display the image or get the image path
            String avatarName = "avatars/" + SharePreference.find(SharePreference.USER_TOKEN_KEY);
            try {
                ImageStorageReference.setImageInto(binding.imageAvatar, avatarName);
            } catch (Exception e) {
                //ignore
            }

            //Update into server
            ImageStorageReference.upload(avatarName, uri);
            user.setAvatar(avatarName);
            UserAPI.update(user);

            //Update into local
            SharePreference.store(SharePreference.USER_AVATAR, avatarName);
        }
    }
     */

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

    // lấy từ local in lên giao diện
    private void updatde() {
        user = new User();
        user.setKey(SharePreference.find(SharePreference.USER_TOKEN_KEY));
        user.setFullName(SharePreference.find(SharePreference.USER_NAME));
        user.setEmail(SharePreference.find(SharePreference.USER_EMAIL));
        user.setAvatar(SharePreference.find(SharePreference.USER_AVATAR));
        user.setDayOfBirth(SharePreference.find(SharePreference.USER_DOB));
        user.setGender(SharePreference.find(SharePreference.USER_GENDER));
        user.setPassword(SharePreference.find(SharePreference.USER_PASS));
        user.setPermistion(SharePreference.findPermission());

        binding.nameUser.setText(user.getFullName());
        if (user.getGender().equalsIgnoreCase(GENDER_MALE)) {
            binding.genderUser.setSelection(0);
        } else if (user.getGender().equalsIgnoreCase(GENDER_FEMALE)) {
            binding.genderUser.setSelection(1);
        } else {
            binding.genderUser.setSelection(2);
        }
        binding.dayBornUser.setText(user.getDayOfBirth());
        binding.emailUser.setText(user.getEmail());
        binding.phoneNumberUser.setText(user.getPhoneNumber());
        try {
            ImageStorageReference.setImageInto(binding.imageAvatar, user.getAvatar());
        } catch (Exception e) {
            ImageStorageReference.setImageInto(binding.imageAvatar, "avatars/default.jpg");
        }
    }


    private void setEnableEdit(boolean isEditable) {
        binding.nameUser.setEnabled(isEditable);
        binding.genderUser.setClickable(isEditable);
        binding.dayBornUser.setEnabled(isEditable);
        binding.emailUser.setEnabled(isEditable);
        binding.phoneNumberUser.setEnabled(isEditable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatde();
    }
}