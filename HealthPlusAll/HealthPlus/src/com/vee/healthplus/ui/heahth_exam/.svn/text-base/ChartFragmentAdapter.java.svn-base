package com.vee.healthplus.ui.heahth_exam;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.vee.healthplus.R;
import com.vee.healthplus.widget.tabpage.IconPagerAdapter;

class ChartFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    protected static final String[] CONTENT = new String[]{"This", "Is", "A", "Test"};
    protected static final int[] ICONS = new int[]{
            R.drawable.perm_group_calendar,
            R.drawable.perm_group_camera,
            R.drawable.perm_group_device_alarms,
            R.drawable.perm_group_location,
            R.drawable.perm_group_location,
            R.drawable.perm_group_location
    };
    public static Fragment[] fragments = new Fragment[4];
    private int mCount = CONTENT.length;

    public ChartFragmentAdapter(FragmentManager fm) {
        super(fm);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (fragments[i] == null)
                continue;
            fragmentTransaction.remove(fragments[i]);
        }
        fragmentTransaction.commit();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = AnalyzeChartFragment.newInstance(position);
        fragments[position] = fragment;
        return fragment;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ChartFragmentAdapter.CONTENT[position % CONTENT.length];
    }

    @Override
    public int getIconResId(int index) {
        return ICONS[index % ICONS.length];
    }

    @Override
    public int getItemPosition(Object object){
        Log.e("xuxuxu","getItemPosition");
        return POSITION_NONE;
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}