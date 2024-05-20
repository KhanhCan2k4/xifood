package vn.edu.tdc.xifood.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import vn.edu.tdc.xfood.R;

public class ActionFooter extends LinearLayout {
    private OnActionListener actionListener = null;

    private Button button = findViewById(R.id.btnAction);

    public void setTextButton(String textButton) {
        button.setText(textButton);
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

        if(actionListener != null) {
            if (btnAction.isSelected()) {
                actionListener.onAction(btnAction);
            }
        } else {
            //ignore
        }
    }

    public interface OnActionListener {
        public void onAction(View view);
    }
}
