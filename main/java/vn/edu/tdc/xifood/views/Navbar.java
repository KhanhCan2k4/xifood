package vn.edu.tdc.xifood.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import vn.edu.tdc.xifood.R;

public class Navbar extends LinearLayout {
    private View prevView = null;
    private OnNavClickListener navClickListener = null;

    public OnNavClickListener getNavClickListener() {
        return navClickListener;
    }

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
                    } else if(view.getId() == R.id.btnDiscount) {
                        navClickListener.onDiscountButtonClick(view);
                    } else if(view.getId() == R.id.btnOrder) {
                        navClickListener.onOrderButtonClick(view);
                    } else if(view.getId() == R.id.btnAccount) {
                        navClickListener.onAccountButtonClick(view);
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle("Warning");
                    builder.setCancelable(false);
                    builder.setMessage("navClickListener is null");
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
        inflate(context, R.layout.navbar_layout, this);

        ImageButton btnHome = findViewById(R.id.btnHome);
        ImageButton btnDiscount = findViewById(R.id.btnDiscount);
        ImageButton btnOrder = findViewById(R.id.btnOrder);
        ImageButton btnAccount = findViewById(R.id.btnAccount);

        btnHome.setOnClickListener(onClickListener);
        btnDiscount.setOnClickListener(onClickListener);
        btnOrder.setOnClickListener(onClickListener);
        btnAccount.setOnClickListener(onClickListener);
    }

    public interface OnNavClickListener {
        public void onHomeButtonClick(View view);
        public void onDiscountButtonClick(View view);
        public void onOrderButtonClick(View view);
        public void onAccountButtonClick(View view);
    }
}
