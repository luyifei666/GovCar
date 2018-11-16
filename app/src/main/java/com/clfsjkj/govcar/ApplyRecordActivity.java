package com.clfsjkj.govcar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApplyRecordActivity extends AppCompatActivity {

    @BindView(R.id.btn)
    Button mBtn;
    @BindView(R.id.customer_ll)
    LinearLayout mCustomerLl;

    private int flag = 0;
    ConstraintLayout parent;
    EditText editText;
    ConstraintLayout.LayoutParams layoutParams;
    private EditText mEditText;
    private TextView mTextView;
    private ImageView mImageViewAdd,mImageViewDel;
    private Context mContext;
    private List<EditText> el = new ArrayList<>();
    private List<String> id_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_record);
        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick(R.id.btn)
    public void onViewClicked() {
        switch (flag) {
            case 0:
                CreateView();
                break;
            default:
                break;
        }
    }

    private void CreateView() {
        LinearLayout mLL = addView();
        mCustomerLl.addView(mLL);
    }

    private LinearLayout addView() {

        //1 画一个HORIZONTAL的LinearLayout，装一个VERTICAL的LinearLayout和一个ImageView
        final LinearLayout layout_sub_Lin = new LinearLayout(this);
        layout_sub_Lin.setBackgroundColor(Color.TRANSPARENT);
        layout_sub_Lin.setOrientation(LinearLayout.HORIZONTAL);
//        layout_sub_Lin.setPadding(8, 6, 8, 4);

        final LinearLayout mLeftView = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParamsOfLeft = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        mLeftView.setBackgroundColor(Color.TRANSPARENT);
        mLeftView.setOrientation(LinearLayout.VERTICAL);
        mLeftView.setLayoutParams(layoutParamsOfLeft);
        layout_sub_Lin.addView(mLeftView);

        mImageViewAdd = new ImageView(this);
        LinearLayout.LayoutParams layoutParamsOfRight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mImageViewAdd.setBackgroundResource(R.drawable.ic_add);
        mImageViewAdd.setLayoutParams(layoutParamsOfRight);
        mImageViewAdd.setPadding(10, 10, 10, 10);
        layout_sub_Lin.addView(mImageViewAdd);

        mImageViewDel = new ImageView(this);
        mImageViewDel.setBackgroundResource(R.drawable.ic_del);
        mImageViewDel.setLayoutParams(layoutParamsOfRight);
        mImageViewDel.setPadding(40, 10, 10, 10);
        layout_sub_Lin.addView(mImageViewDel);

        //2、在mLeftView里装2个HORIZONTAL的LinearLayout：随行人姓名、随行人电话
        final LinearLayout mChildOne = new LinearLayout(this);
        mChildOne.setBackgroundColor(Color.TRANSPARENT);
        mChildOne.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParamsOfmChildOne = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mChildOne.setLayoutParams(layoutParamsOfmChildOne);
        mLeftView.addView(mChildOne);

        final LinearLayout mChildTwo = new LinearLayout(this);
        mChildTwo.setBackgroundColor(Color.TRANSPARENT);
        mChildTwo.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParamsOfmChildTwo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mChildTwo.setLayoutParams(layoutParamsOfmChildTwo);
        mLeftView.addView(mChildTwo);

        //3、随行人姓名装入mChildOne，随行人电话装入mChildTwo
        mTextView = new TextView(this);
        mTextView.setGravity(Gravity.CENTER | Gravity.LEFT);
        mTextView.setText("随行人姓名");
        mTextView.setTextColor(Color.argb(0xff, 0x00, 0x00, 0x00));
        mTextView.setTextSize(px2dip(mContext, sp2px(mContext, 16)));
        mTextView.setLayoutParams(layoutParamsOfmChildOne);
        mChildOne.addView(mTextView);

        mEditText = new EditText(this);
        mEditText.setLayoutParams(layoutParamsOfmChildOne);
        mEditText.setHint("陆毅飞");
        mEditText.setPadding(10, 10, 10, 10);
        mChildOne.addView(mEditText);


        mTextView = new TextView(this);
        mTextView.setGravity(Gravity.CENTER | Gravity.LEFT);
        mTextView.setText("随行人电话");
        mTextView.setTextColor(Color.argb(0xff, 0x00, 0x00, 0x00));
        mTextView.setTextSize(px2dip(mContext, sp2px(mContext, 16)));
        mTextView.setLayoutParams(layoutParamsOfmChildTwo);
        mChildTwo.addView(mTextView);

        mEditText = new EditText(this);
        mEditText.setLayoutParams(layoutParamsOfmChildTwo);
        mEditText.setHint("17608755277");
        mEditText.setPadding(10, 10, 10, 10);
        mChildTwo.addView(mEditText);

        mImageViewDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_sub_Lin.removeAllViews();
            }
        });
        mImageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateView();
            }
        });

        return layout_sub_Lin;
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
