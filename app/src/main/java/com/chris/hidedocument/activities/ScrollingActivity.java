package com.chris.hidedocument.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chris.hidedocument.R;
import com.chris.hidedocument.adapter.MainContentViewPagerAdapter;
import com.chris.hidedocument.fragments.CallMsgFragment;
import com.chris.hidedocument.fragments.ImageFragment;
import com.chris.hidedocument.fragments.SMSFragment;
import com.chris.hidedocument.fragments.VideoFragment;
import com.chris.hidedocument.frame.MainApplication;
import com.chris.hidedocument.model.TabInfoBean;
import com.chris.hidedocument.services.CallMsgService;
import com.chris.hidedocument.services.SmsService;
import com.chris.hidedocument.tools.HideUtils;
import com.chris.hidedocument.widgets.AppBarLayoutBehavior;
import com.chris.hidedocument.widgets.TitleIndicator;
import com.chris.hidedocument.widgets.ViewPagerCompat;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    public static final String EXTRA_TAB = "tab";
    public static final String EXTRA_QUIT = "extra.quit";
    protected int mCurrentTab = 0;
    protected int mLastTab = -1;
    //存放选项卡信息的列表
    private ArrayList<TabInfoBean> mTabs = new ArrayList<>();
    //viewpager com.activities.zoulx.homeactivity.adapter
    protected MainContentViewPagerAdapter mMainContentViewPagerAdapter = null;
    //选项卡控件
    private TitleIndicator mTitleIndicator;
    private ViewPagerCompat mViewPagerCompat;
    private TextView refresh;
    private ImageFragment mImageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.newInstance().addActivity(this);//添加到Activity列表进行统一管理
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterSnackbar(v, getString(R.string.is_hide));
            }
        });
        //修复Google的Snackbar在CoordinatorLayout中的Bug
        ((CoordinatorLayout.LayoutParams) findViewById(R.id.app_bar).getLayoutParams()).setBehavior(new AppBarLayoutBehavior());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterSnackbar(view, getString(R.string.is_hide));
            }
        });
        init();
        initView();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= 19) {
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName());
            startActivity(intent);
        }
    }

    private void initView() {

        mCurrentTab = supplyTabs(mTabs);
        Intent intent = getIntent();
        if (intent != null) {
            mCurrentTab = intent.getIntExtra(EXTRA_TAB, mCurrentTab);
        }
        refresh = (TextView) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainContentViewPagerAdapter.notifyDataSetChanged();
            }
        });
        mMainContentViewPagerAdapter = new MainContentViewPagerAdapter(this, getSupportFragmentManager(), mTabs);

        mViewPagerCompat = (ViewPagerCompat) findViewById(R.id.tabContent);
        mViewPagerCompat.setAdapter(mMainContentViewPagerAdapter);
        mViewPagerCompat.setOnPageChangeListener(this);
        mViewPagerCompat.setOffscreenPageLimit(mTabs.size());

        mTitleIndicator = (TitleIndicator) findViewById(R.id.tabTitle);
        mTitleIndicator.init(mCurrentTab, mTabs, mViewPagerCompat);

        mViewPagerCompat.setCurrentItem(mCurrentTab);
        mLastTab = mCurrentTab;
    }

    /**
     * 初始化Tabs
     *
     * @param tabs
     * @return
     */
    private int supplyTabs(List<TabInfoBean> tabs) {
        mImageFragment = new ImageFragment();
        tabs.add(new TabInfoBean(0, "Images", ImageFragment.class));
        tabs.add(new TabInfoBean(1, "Video", VideoFragment.class));
        tabs.add(new TabInfoBean(2, "SMS", SMSFragment.class));
        tabs.add(new TabInfoBean(3, "CallMsg", CallMsgFragment.class));
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.alreadyHide:
                startActivity(new Intent(this, AlreadyHideActivity.class));
                return true;
            case R.id.alreadyHideSms:
                startActivity(new Intent(this, AlreadyHideSmsActivity.class));
                return true;
            case R.id.alreadyHideCallMsg:
                startActivity(new Intent(this, AlreadyHideCallMsgActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mTitleIndicator.onScrolled((mViewPagerCompat.getWidth() + mViewPagerCompat.getPageMargin()) * position + positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        mTitleIndicator.onSwitched(position);
        mCurrentTab = position;
        HideUtils.newInstance().setCurrentTab(mCurrentTab);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            mLastTab = mCurrentTab;
        }
    }

    private void hide(int position) {
        switch (position) {

            case 0://图片
                if (ImageFragment.getData(mCurrentTab) != null) {
                    HideUtils.newInstance().isHideFile(true, ImageFragment.getData(mCurrentTab));
                }
                break;
            case 1://视频
                if (VideoFragment.getData(mCurrentTab) != null) {
                    HideUtils.newInstance().isHideFile(true, VideoFragment.getData(mCurrentTab));
                }
                break;
            case 2://短信
                if (SMSFragment.getData(mCurrentTab) != null) {
                    SmsService.newInstance().deleteSmss(this, SMSFragment.getData(mCurrentTab));
                }
                break;

            case 3://通话记录
                if (CallMsgFragment.getData(mCurrentTab) != null) {
                    CallMsgService.newInstance().hideCallMsg(this, CallMsgFragment.getData(mCurrentTab));
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //区分当前展示在用户面前是哪一个Fragment
        HideUtils.newInstance().setCurrentTab(0);//标记第一个页面为当前选中的Fragment
    }

    /**
     * 弹出Toast
     *
     * @param content
     */
    private void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出Snackbar
     *
     * @param view
     * @param content
     */
    private void alterSnackbar(View view, String content) {
        Snackbar.make(view, content, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.sure), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hide(mCurrentTab);
                    }
                }).show();
    }
}
