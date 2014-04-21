package com.vee.healthplus.common;

import android.support.v4.app.Fragment;

public interface IFragmentMsg {
	public void replaceFragment(FragmentMsg fMsg);
    public void updateHeaderTitle(String title);
}
