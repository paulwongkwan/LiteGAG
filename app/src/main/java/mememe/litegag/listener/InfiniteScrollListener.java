package mememe.litegag.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Wong Kwan on 16/2/2016.
 */
public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {
	private static final int HIDE_THRESHOLD = 20;
	private int scrolledDistance = 0;
	private boolean controlsVisible = true;
	private boolean hidetoggle = true;//Set header bar hide show control

	private int bufferItemCount = 10;
	private int currentPage = 0;
	private int itemCount = 0;
	private boolean isLoading = true;
	private LinearLayoutManager manager;
	private int firstVisibleItem, visibleItemCount, totalItemCount;

	public InfiniteScrollListener(int bufferItemCount, RecyclerView.LayoutManager manager) {
		this.bufferItemCount = bufferItemCount;
		this.manager = (LinearLayoutManager) manager;
	}

	//Use this contructor to set header display or not
	public InfiniteScrollListener(int bufferItemCount, RecyclerView.LayoutManager manager, Boolean hidetoggle) {
		this.hidetoggle = hidetoggle;
		this.bufferItemCount = bufferItemCount;
		this.manager = (LinearLayoutManager) manager;
	}

	//Fuction for overide
	public abstract void loadMore(int page, int totalItemsCount);
	public abstract void onHide();
	public abstract void onShow();

	public void onScrolled(RecyclerView recyclerView, int dx, int dy){
		/*
		 * Infinite Scroll listener
		 */
	    visibleItemCount = manager.getChildCount();
		totalItemCount = manager.getItemCount();
		firstVisibleItem = manager.findFirstVisibleItemPosition();

		if (totalItemCount < itemCount) {
			this.itemCount = totalItemCount;
			if (totalItemCount == 0) {
				this.isLoading = true;
			}
		}

		if (isLoading && (totalItemCount > itemCount)) {
			isLoading = false;
			itemCount = totalItemCount;
			currentPage++;
		}

		if (!isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + bufferItemCount)) {
			loadMore(currentPage + 1, totalItemCount);
			isLoading = true;
		}

		/*
		 *  Hide the Toobar
		 */
		if(hidetoggle) {
			if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
				onHide();
				controlsVisible = false;
				scrolledDistance = 0;
			} else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
				onShow();
				controlsVisible = true;
				scrolledDistance = 0;
			}
			if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
				scrolledDistance += dy;
			}
		}
	}
}

