package vn.edu.tdc.player2024.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import vn.edu.tdc.player2024.R;

public class Player extends LinearLayout {
    //    FIELDS
    private View prevView = null;
    private ViewGroup playLayout;
    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            //1
//            if (prevView != null) {
//                if (prevView == view) {
//                    view.setSelected(!view.isSelected());
//                } else {
//                    prevView.setSelected(false);
//                    view.setSelected(true);
//                }
//            } else {
//                view.setSelected(!view.isSelected());
//            }
//
//            prevView = view;

            //2
            for (int i = 0; i < playLayout.getChildCount(); i++) {
                View childView = playLayout.getChildAt(i);
                childView.setSelected((childView == view) ? !childView.isSelected() : false);
            }

            if(playerClickListener != null) {
                if(view.isSelected()) {
                    if(view.getId() == R.id.btnPrev) {
                        playerClickListener.onPrevButtonClick(view);
                    } else if(view.getId() == R.id.btnPlay) {
                        playerClickListener.onPlayButtonClick(view);
                    } else if(view.getId() == R.id.btnPause) {
                        playerClickListener.onPauseButtonClick(view);
                    } else if(view.getId() == R.id.btnStop) {
                        playerClickListener.onStopButtonClick(view);
                    } else if(view.getId() == R.id.btnNext) {
                        playerClickListener.onNextButtonClick(view);
                    }
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Warning");
                builder.setCancelable(false);
                builder.setMessage("playerClickListener is null");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }
                );

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
    };
    private OnPlayerClickListener playerClickListener;

//    GETTER - SETTER
    public void setPlayerClickListener(OnPlayerClickListener playerClickListener) {
        this.playerClickListener = playerClickListener;
    }

    //    CONSTRUCTOR
    public Player(Context context) {
        super(context);
        setUpView(context);
    }

    public Player(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUpView(context);
    }

    public Player(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpView(context);
    }

    //    METHODS
    private void setUpView(Context context) {
        inflate(context, R.layout.player_layout, this);

        playLayout = (LinearLayout) getChildAt(0);

        //get view from layout
        ImageView btnPrev = findViewById(R.id.btnPrev);
        ImageView btnPlay = findViewById(R.id.btnPlay);
        ImageView btnPause = findViewById(R.id.btnPause);
        ImageView btnStop = findViewById(R.id.btnStop);
        ImageView btnNext = findViewById(R.id.btnNext);

        //set onclick for all buttons
        btnPrev.setOnClickListener(onClickListener);
        btnPlay.setOnClickListener(onClickListener);
        btnPause.setOnClickListener(onClickListener);
        btnStop.setOnClickListener(onClickListener);
        btnNext.setOnClickListener(onClickListener);

//        btnPrev.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (playerClickListener != null) {
//                    playerClickListener.onPrevButtonClick(view);
//                } else {
//                    Log.d("TAG-item-click", "onClick: not set item listener yet");
//                }
//            }
//        });
//        btnPlay.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (playerClickListener != null) {
//                    playerClickListener.onPrevButtonClick(view);
//                } else {
//                    Log.d("TAG-item-click", "onClick: not set item listener yet");
//                }
//            }
//        });
//        btnPause.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (playerClickListener != null) {
//                    playerClickListener.onPrevButtonClick(view);
//                } else {
//                    Log.d("TAG-item-click", "onClick: not set item listener yet");
//                }
//            }
//        });
//        btnStop.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (playerClickListener != null) {
//                    playerClickListener.onPrevButtonClick(view);
//                } else {
//                    Log.d("TAG-item-click", "onClick: not set item listener yet");
//                }
//            }
//        });
//        btnNext.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (playerClickListener != null) {
//                    playerClickListener.onPrevButtonClick(view);
//                } else {
//                    Log.d("TAG-item-click", "onClick: not set item listener yet");
//                }
//            }
//        });
    }

    public interface OnPlayerClickListener {
        public  void onPrevButtonClick(View view);
        public  void onPlayButtonClick(View view);
        public  void onPauseButtonClick(View view);
        public  void onStopButtonClick(View view);
        public  void onNextButtonClick(View view);
    }
}
