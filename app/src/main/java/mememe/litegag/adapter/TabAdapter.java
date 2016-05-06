package mememe.litegag.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import mememe.litegag.InfiniteScrollFrag;

/**
 * Created by Wong Kwan on 17/2/2016.
 * Include three tab:
 * HOT; TRENDING; FRESH
 */
public class TabAdapter extends FragmentPagerAdapter {
	private final String[] TITLES = { "HOT", "TRENDING", "FRESH" };

	public TabAdapter(FragmentManager fm) {
		super(fm);
	}

	public CharSequence getPageTitle(int position) {
		return TITLES[position];
	}

	@Override
	public int getCount() {
		return TITLES.length;
	}

	@Override
	public Fragment getItem(int position) {
		return InfiniteScrollFrag.newInstance(position);
	}

}
