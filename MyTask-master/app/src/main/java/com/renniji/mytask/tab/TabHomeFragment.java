package com.renniji.mytask.tab;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.renniji.mytask.R;
import com.renniji.mytask.base.BaseFragment;
import com.renniji.mytask.constants.MyConstants;
import com.renniji.mytask.dialog.SelectAreaDialog;
import com.renniji.mytask.fragment.HomeLeftFragment;
import com.renniji.mytask.fragment.HomeMiddleFragment;
import com.renniji.mytask.fragment.HomeRightFragment;
import com.renniji.mytask.utils.FileUtil;


public class TabHomeFragment extends BaseFragment implements View.OnClickListener {

    private static final int VIEW_LEFT = 0;
    private static final int VIEW_MIDDLE = 1;
    private static final int VIEW_RIGHT = 2;
    private int mSign = VIEW_LEFT;
    private int deBg = R.color.black;
    private int deText = R.color.white;
    private int peBg = R.color.white;
    private int peText = R.color.black;
    private View mView;
    private TextView mHomeLeftTv;
    private TextView mHomeMiddleTv;
    private TextView mHomeRightTv;
    private ImageView mSaveIv;

    private HomeLeftFragment mHomeLeftFragment;
    private HomeMiddleFragment mHomeMiddleFragment;
    private HomeRightFragment mHomeRightFragment;

    private String mAreaData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_tab_home, null);
        }
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        initView(mView);
        initData();
        return mView;
    }

    public void initView(View view) {
        mHomeLeftTv = (TextView) view.findViewById(R.id.home_left_tv);
        mHomeMiddleTv = (TextView) view.findViewById(R.id.home_middle_tv);
        mHomeRightTv = (TextView) view.findViewById(R.id.home_right_tv);
        mSaveIv = (ImageView) view.findViewById(R.id.save);

        mHomeLeftTv.setOnClickListener(this);
        mHomeMiddleTv.setOnClickListener(this);
        mHomeRightTv.setOnClickListener(this);
        mSaveIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectAreaDialogShow();
            }
        });

        setDefaultFragment();

    }

    public void initData() {
        initAreaViewData();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();

        switch (v.getId()) {
            case R.id.home_left_tv:
                if (mHomeLeftFragment == null) {
                    mHomeLeftFragment = new HomeLeftFragment();
                }
                transaction.replace(R.id.home_content, mHomeLeftFragment);
                mSign = VIEW_LEFT;
                changButUI(mSign);
                break;
            case R.id.home_middle_tv:
                if (mHomeMiddleFragment == null) {
                    mHomeMiddleFragment = new HomeMiddleFragment();
                }
                transaction.replace(R.id.home_content, mHomeMiddleFragment);
                mSign = VIEW_MIDDLE;
                changButUI(mSign);
                break;
            case R.id.home_right_tv:
                if (mHomeRightFragment == null) {
                    mHomeRightFragment = new HomeRightFragment();
                }
                transaction.replace(R.id.home_content, mHomeRightFragment);
                mSign = VIEW_RIGHT;
                changButUI(mSign);
                break;
            default:
                break;
        }
        transaction.commit();

    }

    private void changButUI(int mSign) {
        setDefaultAllUI();
        switch (mSign) {
            case VIEW_LEFT:
                mHomeLeftTv.setBackgroundColor(getResources().getColor(peBg));
                mHomeLeftTv.setTextColor(getResources().getColor(peText));

                break;
            case VIEW_MIDDLE:
                mHomeMiddleTv.setBackgroundColor(getResources().getColor(peBg));
                mHomeMiddleTv.setTextColor(getResources().getColor(peText));

                break;
            case VIEW_RIGHT:
                mHomeRightTv.setBackgroundColor(getResources().getColor(peBg));
                mHomeRightTv.setTextColor(getResources().getColor(peText));

                break;
            default:
                break;
        }
    }

    private void setDefaultAllUI() {
        mHomeLeftTv.setBackgroundColor(getResources().getColor(deBg));
        mHomeLeftTv.setTextColor(getResources().getColor(deText));

        mHomeMiddleTv.setBackgroundColor(getResources().getColor(deBg));
        mHomeMiddleTv.setTextColor(getResources().getColor(deText));

        mHomeRightTv.setBackgroundColor(getResources().getColor(deBg));
        mHomeRightTv.setTextColor(getResources().getColor(deText));
    }

    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        switch (mSign) {
            case VIEW_LEFT:
                mHomeLeftFragment = new HomeLeftFragment();
                transaction.replace(R.id.home_content, mHomeLeftFragment);
                break;

            case VIEW_MIDDLE:
                mHomeMiddleFragment = new HomeMiddleFragment();
                transaction.replace(R.id.home_content, mHomeMiddleFragment);
                break;

            case VIEW_RIGHT:
                mHomeRightFragment = new HomeRightFragment();
                transaction.replace(R.id.home_content, mHomeRightFragment);
                break;

            default:
                break;
        }
        changButUI(mSign);
        transaction.commit();
    }

    /**
     *
     * 显示选择地址对话框
     */
    private void SelectAreaDialogShow() {
        SelectAreaDialog dialog = new SelectAreaDialog(getContext(),
                mAreaData);
        dialog.onCreateDialog();
        dialog.setCallBack(new SelectAreaDialog.SelectCallBack() {
            @Override
            public void isConfirm(String callBackData) {
//                mAreaNetStr = callBackData ;
//                String[] arr = mAreaNetStr.split("\\:");
//                String arr1 = arr[1];
//                mAreaStr = arr1.replaceAll("/", " ");
//                mArea.setText(mAreaStr);
            }
        });
    }


    private void initAreaViewData() {
        mAreaData = FileUtil.readFile(MyConstants.AREA_DATA_DIR,
                MyConstants.AREA_DATA, MyConstants.TXT);

    }


}
