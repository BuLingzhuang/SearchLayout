package com.bulingzhuang.searchlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ScrollView mScrollView;
    private ImageView mIvImg;
    private Toolbar mToolbar;
    private LinearLayout mLlSearch;
    private TextView mTvSearch;
    private AutoTransition mSet;

    private boolean mIsExpand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mIvImg = (ImageView) findViewById(R.id.iv_img);
        mLlSearch = (LinearLayout) findViewById(R.id.ll_search);
        mTvSearch = (TextView) findViewById(R.id.tv_search);

        mToolbar.getBackground().mutate().setAlpha(0);

        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                //改变Toolbar透明度
                changeToolbarAlpha();
                //滚动距离>=大图高度-toolbar高度，即toolbar完全盖住大图的时候，且不是伸展状态，进行伸展操作
                if (mScrollView.getScrollY()>=mIvImg.getHeight() -mToolbar.getHeight() && !mIsExpand){
                    expand();
                    mIsExpand = true;
                }else if (mScrollView.getScrollY()<=0 && mIsExpand){
                    reduce();
                    mIsExpand = false;
                }
            }
        });
    }

    private void changeToolbarAlpha() {
        int scrollY = mScrollView.getScrollY();
        //快速下拉会引起瞬间scrollY<0
        if(scrollY<0){
            mToolbar.getBackground().mutate().setAlpha(0);
            return;
        }
        //计算当前透明度比率
        float radio= Math.min(1,scrollY/(mIvImg.getHeight()-mToolbar.getHeight()*1f));
        //设置透明度
        mToolbar.getBackground().mutate().setAlpha( (int)(radio * 0xFF));
    }

    private void expand(){
        //设置伸展状态的布局
        beginDelayedTransition(mLlSearch);
        mTvSearch.setText("搜索内容和类型");
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLlSearch.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.setMargins(dip2px(16),dip2px(12),dip2px(16),dip2px(12));
        mLlSearch.setLayoutParams(layoutParams);
        //设置动画
    }

    private void reduce(){
        //设置收缩状态时的布局
        beginDelayedTransition(mLlSearch);
        mTvSearch.setText("搜索");
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLlSearch.getLayoutParams();
        layoutParams.width = dip2px(80);
        layoutParams.setMargins(dip2px(16),dip2px(12),dip2px(16),dip2px(12));
        mLlSearch.setLayoutParams(layoutParams);
        //设置动画
    }

    void beginDelayedTransition(ViewGroup view) {
        mSet = new AutoTransition();
        //设置动画持续时间
        mSet.setDuration(300);
        // 开始表演
        TransitionManager.beginDelayedTransition(view, mSet);
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
