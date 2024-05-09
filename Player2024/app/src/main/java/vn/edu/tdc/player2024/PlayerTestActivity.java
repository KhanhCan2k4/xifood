package vn.edu.tdc.player2024;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import vn.edu.tdc.player2024.databinding.PlayerTestLayoutBinding;
import vn.edu.tdc.player2024.views.Player;

public class PlayerTestActivity extends AppCompatActivity {

    private PlayerTestLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PlayerTestLayoutBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

//        binding.player.setPlayerClickListener(new Player.OnPlayerClickListener() {
//            @Override
//            public void onPrevButtonClick(View view) {
//                Log.d("TAG", "onPrevButtonClick: ");
//            }
//
//            @Override
//            public void onPlayButtonClick(View view) {
//                Log.d("TAG", "onPlayButtonClick: ");
//            }
//
//            @Override
//            public void onPauseButtonClick(View view) {
//                Log.d("TAG", "onPauseButtonClick: ");
//            }
//
//            @Override
//            public void onStopButtonClick(View view) {
//                Log.d("TAG", "onStopButtonClick: ");
//            }
//
//            @Override
//            public void onNextButtonClick(View view) {
//                Log.d("TAG", "onNextButtonClick: ");
//            }
//        });
    }
}