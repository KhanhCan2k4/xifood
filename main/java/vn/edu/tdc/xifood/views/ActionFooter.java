package vn.edu.tdc.xifood.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import vn.edu.tdc.xifood.R;
import vn.edu.tdc.xifood.mydatamodels.Product;

public class ActionFooter extends LinearLayout {
    private OnActionListener actionListener = null;

    public OnActionListener getActionListener() {
        return actionListener;
    }

    public void setActionListener(OnActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public ActionFooter(Context context) {
        super(context);
        setUp(context);
    }

    public ActionFooter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUp(context);
    }

    public ActionFooter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp(context);
    }

    protected void setUp(Context context) {
        inflate(context, R.layout.action_footer_layout, this);

        Button btnAction = findViewById(R.id.btnAction);

        btnAction.setVisibility(GONE);

        btnAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(actionListener != null) {
                    actionListener.onAction(btnAction);
                }
            }
        });
    }

    public void setPriceText(long price) {
        Button btnAction = findViewById(R.id.btnAction);
        TextView txtPrice = findViewById(R.id.txtTotal);

        txtPrice.setText(Product.getPriceInFormat(price));

        btnAction.setVisibility(price > 0 ? VISIBLE : GONE);
    }

    public void setButtonText(String text) {
        Button btnAction = findViewById(R.id.btnAction);
        btnAction.setText(text);
    }

    public interface OnActionListener {
        public void onAction(View view);
    }
}
