package vn.edu.tdc.xifood.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import vn.edu.tdc.xifood.R;

public class Navbar extends LinearLayout {
    private View prevView = null;
    private ImageButton btnHome;
    private ImageButton btnOrder;
    private ImageButton btnAccount;
    private Context context;
    public void setActiveIndex(int activeIndex) {
        ImageButton activeButton = btnHome;

        switch (activeIndex) {
            case 1:
                activeButton = btnOrder;
                break;
            case 2:
                activeButton = btnAccount;
                break;
        }

        activeButton.setBackgroundTintList(ContextCompat.getColorStateList(this.context, R.color.active_button));
        activeButton.setScaleX(0.8f);
        activeButton.setScaleY(0.8f);

    }

    private OnNavClickListener navClickListener = null;

    public void setNavClickListener(OnNavClickListener navClickListener) {
        this.navClickListener = navClickListener;
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (prevView != null) {
                if (prevView == view) {
                    view.setSelected(!view.isSelected());
                } else {
                    prevView.setSelected(false);
                    view.setSelected(true);
                }
            } else {
                view.setSelected(!view.isSelected());
            }

            prevView = view;

            if(navClickListener != null) {
                if (view.isSelected()) {
                    if(view.getId() == R.id.btnHome) {
                        navClickListener.onHomeButtonClick(view);
                    } else if(view.getId() == R.id.btnOrder) {
                        navClickListener.onOrderButtonClick(view);
                    } else if(view.getId() == R.id.btnAccount) {
                        navClickListener.onAccountButtonClick(view);
                    }
                } else {
                    //ignore
                }
            }
        }
    };

    public Navbar(Context context) {
        super(context);
        setUp(context);
    }

    public Navbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUp(context);
    }

    public Navbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp(context);
    }

    protected void setUp(Context context) {
        this.context = context;
        inflate(context, R.layout.navbar_layout, this);

        btnHome = findViewById(R.id.btnHome);
        btnOrder = findViewById(R.id.btnOrder);
        btnAccount = findViewById(R.id.btnAccount);

        btnHome.setOnClickListener(onClickListener);
        btnOrder.setOnClickListener(onClickListener);
        btnAccount.setOnClickListener(onClickListener);
    }

    public interface OnNavClickListener {
        public void onHomeButtonClick(View view);
        public void onOrderButtonClick(View view);
        public void onAccountButtonClick(View view);
    }
}
