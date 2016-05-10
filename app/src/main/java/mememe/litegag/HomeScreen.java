package mememe.litegag;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.astuetz.PagerSlidingTabStrip;

import butterknife.BindView;
import butterknife.ButterKnife;
import mememe.litegag.adapter.TabAdapter;
import mememe.litegag.networking.OkHttpStack;

/**
 * 2016-02-17
 * Wong Kwan
 * This is app entry and tab holder for the three tab.
 * Home page hold three tab by PagerSlidingTabStrip.
 * Each page is using same fragment class "InfinitScrollFrag"
 * and have refresh and infinit scroll function.
 * All data is fetch from InfiniGAG API.
 *
 * External Library used:
 * - PagerSlidingTabStrip
 * - PhotoView
 * - Picasso
 * - InfiniGAG API
 * - Volley
 * - ButterKnife
 * - OkHttp3
 */

public class HomeScreen extends AppCompatActivity {
	//Page Widgets
	@BindView(R.id.tabs)
	PagerSlidingTabStrip tabs;
	@BindView(R.id.pager)
	ViewPager pager;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.headerbar)
	LinearLayout headerbar;
	RequestQueue mQueue;
	private TabAdapter tabAdapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);

		ButterKnife.bind(this);
		initActionbar();
		setTabs();

		mQueue = Volley.newRequestQueue(this, new OkHttpStack());

		// Initialize the ViewPager and set an adapter
		tabAdapter = new TabAdapter(getSupportFragmentManager());
		pager.setAdapter(tabAdapter);

		// Bind the tabs to the ViewPager
		tabs.setViewPager(pager);
	}

	protected void addQueue(Request request) {
		mQueue.add(request);
	}

	/*
	 *  Header bar controller
	 *  For hide and show the header bar
	 */
	public void showbar(){
		headerbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
	}
	public void hidebar(){
		headerbar.animate().translationY(-headerbar.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
	}

	private void setTabs() {
		if (tabs != null){
			tabs.setShouldExpand(true);
			tabs.setDividerColorResource(R.color.MidNightBlue);
			tabs.setIndicatorColorResource(R.color.Sliver);
			tabs.setIndicatorHeight(getResources().getDimensionPixelOffset(R.dimen.IndicatorHeight));
			tabs.setTextColorResource(R.color.Cloud);

			pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

				}

				@Override
				public void onPageSelected(int position) {
					showbar();
				}

				@Override
				public void onPageScrollStateChanged(int state) {
				}
			});
		}
	}

	private void initActionbar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(getResources().getString(R.string.app_name));
		toolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(toolbar);
	}

	protected void onConfigurationChange(){
	}
}
