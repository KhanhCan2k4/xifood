package vn.edu.tdc.xifood.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import vn.edu.tdc.xifood.R;

public class CancelHeader extends LinearLayout {
    private OnCancelListener cancelListener = null;
    public CancelHeader(Context context) {
        super(context);
        setUp(context);
    }

    public CancelHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUp(context);
    }

    public CancelHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp(context);
    }

    protected void setUp(Context context) {
        inflate(context, R.layout.cancel_header_layout, this);

        ImageButton btnCancel = findViewById(R.id.btnCancel);

        if(cancelListener != null) {
            if (btnCancel.isSelected()) {
                cancelListener.onCancel(btnCancel);
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setTitle("Warning");
            builder.setCancelable(false);
            builder.setMessage("cancelListener is null");
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

    public void setTitle (String title) {
        TextView txtTitle = findViewById(R.id.txtTitle);

        txtTitle.setText(title);
    }

    public interface OnCancelListener {
        public void onCancel(View view);
    }
}
