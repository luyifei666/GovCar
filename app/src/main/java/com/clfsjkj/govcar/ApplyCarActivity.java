package com.clfsjkj.govcar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.clfsjkj.govcar.adapter.ImagePickerAdapter;
import com.clfsjkj.govcar.base.BaseActivity;
import com.clfsjkj.govcar.customerview.MClearEditText;
import com.clfsjkj.govcar.imageloader.GlideImageLoader;
import com.clfsjkj.govcar.imageloader.SelectDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApplyCarActivity extends BaseActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener {

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    public static final int REQUEST_CODE_START = 0x000;
    public static final int REQUEST_CODE_PATH = 0x001;
    public static final int REQUEST_CODE_END = 0x002;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.customer_ll)
    LinearLayout mCustomerLl;
    @BindView(R.id.ck_suixing)
    CheckBox mCkSuixing;
    @BindView(R.id.spinner_car)
    Spinner mSpinnerCar;
    @BindView(R.id.btn_car_start)
    Button mBtnCarStart;
    @BindView(R.id.btn_car_path)
    Button mBtnCarPath;
    @BindView(R.id.btn_car_destination)
    Button mBtnCarDestination;
    @BindView(R.id.btn_car_apply)
    Button mBtnCarApply;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 8;               //允许选择图片最大数
    private MClearEditText mEditText;
    private TextView mTextView;
    private ImageView mImageViewAdd, mImageViewDel;
    private Context mContext;
    private List<String> mSpinnerCarList = new ArrayList<String>();
    private ArrayAdapter<String> mSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_car);
        ButterKnife.bind(this);
        mContext = this;
        initMyToolBar();
        //最好放到 Application oncreate执行
        initImagePicker();
        initWidget();
    }

    private void initMyToolBar() {
        initToolBar(mToolbar, "申请用车", R.drawable.gank_ic_back_white);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(false);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void initWidget() {
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                return false;
            }
        });
        //解决数据加载不完的问题
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
//        mRecyclerView.setFocusable(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mCkSuixing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CreateView();
                } else {
                    mCustomerLl.removeAllViews();
                }
            }
        });

        mSpinnerCar.setGravity(Gravity.CENTER);//居中
        mSpinnerCarList.add("请选择");
        mSpinnerCarList.add("越野车");
        mSpinnerCarList.add("轿车");
        mSpinnerCarList.add("商务车");
        mSpinnerCarList.add("大客车");
        mSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mSpinnerCarList);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCar.setAdapter(mSpinnerAdapter);
        mSpinnerCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                /**
                                 * 0.4.7 目前直接调起相机不支持裁剪，如果开启裁剪后不会返回图片，请注意，后续版本会解决
                                 *
                                 * 但是当前直接依赖的版本已经解决，考虑到版本改动很少，所以这次没有上传到远程仓库
                                 *
                                 * 如果实在有所需要，请直接下载源码引用。
                                 */
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(ApplyCarActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(ApplyCarActivity.this, ImageGridActivity.class);
                                /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
//                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }

                    }
                }, names);


                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    ArrayList<ImageItem> images = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
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
        LinearLayout.LayoutParams layoutParamsOfLeft = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mLeftView.setBackgroundColor(Color.TRANSPARENT);
        mLeftView.setOrientation(LinearLayout.VERTICAL);
        mLeftView.setLayoutParams(layoutParamsOfLeft);
        layout_sub_Lin.addView(mLeftView);

        mImageViewAdd = new ImageView(this);
        LinearLayout.LayoutParams layoutParamsOfRightOne = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsOfRightOne.setMargins(px2dip(mContext, 80), px2dip(mContext, 80), px2dip(mContext, 80), px2dip(mContext, 80));
        mImageViewAdd.setBackgroundResource(R.drawable.ic_add);
        mImageViewAdd.setLayoutParams(layoutParamsOfRightOne);
        mImageViewAdd.setPadding(10, 10, 10, 10);
        layout_sub_Lin.addView(mImageViewAdd);

        mImageViewDel = new ImageView(this);
        LinearLayout.LayoutParams layoutParamsOfRightTwo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsOfRightTwo.setMargins(px2dip(mContext, 20), px2dip(mContext, 80), px2dip(mContext, 80), px2dip(mContext, 80));
        mImageViewDel.setBackgroundResource(R.drawable.ic_del);
        mImageViewDel.setLayoutParams(layoutParamsOfRightTwo);
        mImageViewDel.setPadding(40, 10, 10, 10);
        layout_sub_Lin.addView(mImageViewDel);

        //2、在mLeftView里装2个HORIZONTAL的LinearLayout：随行人姓名、随行人电话
        final LinearLayout mChildOne = new LinearLayout(this);
        mChildOne.setBackgroundColor(Color.TRANSPARENT);
        mChildOne.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParamsOfmChildOne = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsOfmChildOne.setMargins(sp2px(mContext, 15), 4, 4, 4);
        mChildOne.setLayoutParams(layoutParamsOfmChildOne);
        mLeftView.addView(mChildOne);

        final LinearLayout mChildTwo = new LinearLayout(this);
        mChildTwo.setBackgroundColor(Color.TRANSPARENT);
        mChildTwo.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParamsOfmChildTwo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsOfmChildTwo.setMargins(sp2px(mContext, 15), 4, 4, 4);
        mChildTwo.setLayoutParams(layoutParamsOfmChildTwo);
        mLeftView.addView(mChildTwo);

        //3、随行人姓名装入mChildOne，随行人电话装入mChildTwo
        mTextView = new TextView(this);
        mTextView.setGravity(Gravity.CENTER | Gravity.LEFT);
        mTextView.setText("随行人姓名");
        mTextView.setTextColor(Color.argb(0xff, 0x00, 0x00, 0x00));
        mTextView.setTextSize(13);
        mTextView.setPadding(4, 4, 4, 4);
        mTextView.setLayoutParams(layoutParamsOfmChildOne);
        mChildOne.addView(mTextView);

        mEditText = new MClearEditText(this);
        mEditText.setLayoutParams(layoutParamsOfmChildOne);
        mEditText.setHint("请输入随行人姓名");
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)}); //最大输入长度
        mEditText.setBackgroundResource(R.drawable.search_bg);
        mEditText.setMinHeight(px2dip(mContext, 80));
        mEditText.setTextSize(13);
        mEditText.setPadding(10, 10, 10, 10);
        mChildOne.addView(mEditText);


        mTextView = new TextView(this);
        mTextView.setGravity(Gravity.CENTER | Gravity.LEFT);
        mTextView.setText("随行人电话");
        mTextView.setTextColor(Color.argb(0xff, 0x00, 0x00, 0x00));
        mTextView.setTextSize(13);
        mTextView.setPadding(4, 4, 4, 4);
        mTextView.setLayoutParams(layoutParamsOfmChildTwo);
        mChildTwo.addView(mTextView);

        mEditText = new MClearEditText(this);
        mEditText.setLayoutParams(layoutParamsOfmChildTwo);
        mEditText.setHint("请输入随行人电话");
        mEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)}); //最大输入长度
        mEditText.setBackgroundResource(R.drawable.search_bg);
        mEditText.setTextSize(13);
        mEditText.setPadding(10, 10, 10, 10);
        mChildTwo.addView(mEditText);

        mImageViewDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomerLl.removeView(layout_sub_Lin);
                if (mCustomerLl.getChildCount() == 0) {
                    mCkSuixing.setChecked(false);
                }
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

    @OnClick({R.id.btn_car_start, R.id.btn_car_path, R.id.btn_car_destination, R.id.btn_car_apply})
    public void onViewClicked(View view) {
        Intent it = new Intent(ApplyCarActivity.this,BaiduMapPoiActivity.class);
        switch (view.getId()) {
            case R.id.btn_car_start:
                it.putExtra("REQUEST_CODE",REQUEST_CODE_START);
                startActivityForResult(it,REQUEST_CODE_START);
                break;
            case R.id.btn_car_path:
                it.putExtra("REQUEST_CODE",REQUEST_CODE_PATH);
                startActivityForResult(it,REQUEST_CODE_PATH);
                break;
            case R.id.btn_car_destination:
                it.putExtra("REQUEST_CODE",REQUEST_CODE_END);
                startActivityForResult(it,REQUEST_CODE_END);
                break;
            case R.id.btn_car_apply:
                break;
        }
    }
}
