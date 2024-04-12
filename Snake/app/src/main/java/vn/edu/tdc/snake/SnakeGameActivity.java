package vn.edu.tdc.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import vn.edu.tdc.snake.views.Snack;

public class SnakeGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snack snack = new Snack(this);
        snack.setKeepScreenOn(true);
        setContentView(snack);
    }
}