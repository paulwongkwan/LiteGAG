package mememe.litegag.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mememe.litegag.R;
import mememe.litegag.object.GagObject;
import mememe.litegag.utility.ImageViewer;
import mememe.litegag.utility.VideoPlayerDialog;

/**
 * Created by Wong Kwan on 18/2/2016.
 * Infinit scroll view adapter
 * Hoziontal recycleview on the top
 * as header bar. Header bar can set
 * display or not by using different
 * constructor.
 * Custom footer view at the bottom for
 * loading purpose.
 */
public class GagAdapter extends RecyclerView.Adapter<GagAdapter.GagViewHolder>  {
	final static int TYPE_GAG = 2, TYPE_FOOTER = 3;

	private Context c;
	private LayoutInflater mInflater;
	private List<GagObject> gaglist = new ArrayList<>();

	public GagAdapter(Context c, List<GagObject> gaglist) {
		this.c = c;
		this.mInflater = LayoutInflater.from(c);
		this.gaglist = gaglist;
	}

	/*
	 *  position -1 if header exist
	 */
	public GagObject getItem(int position) {
		if (gaglist.size() == 0 || position >= gaglist.size()) {
			return null;
		}else {
			return gaglist.get(position);
		}
	}

	@Override
	public GagAdapter.GagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if(viewType == TYPE_FOOTER){
			return new GagViewHolder(mInflater.inflate(R.layout.footerview, parent, false), c);
		}else {
			return new GagViewHolder(mInflater.inflate(R.layout.gagview, parent, false), c);
		}
	}

	@Override
	public void onBindViewHolder(GagAdapter.GagViewHolder holder, int position) {
		holder.setupView(getItem(position));
	}

	public long getItemId(int position) {
		return position;
	}

	/*
	 *  plus 1 for footer
	 *  plus 2 for header
	 */
	@Override
	public int getItemCount() {
		return gaglist.size() + 1;
	}

	/*
	 *  Three view type:
	 *  - HEADER
	 *  - FOOTER
	 *  - NORMAL VIEW
	 */
	@Override
	public int getItemViewType(int position) {
		if (position == gaglist.size()) {
			return TYPE_FOOTER;
		}else {
			return TYPE_GAG;
		}
	}

	public static class GagViewHolder extends RecyclerView.ViewHolder {
		@Nullable
		@BindView(R.id.gagTitle)
		TextView gagTitle;
		@Nullable
		@BindView(R.id.gagImage)
		ImageView gagImage;
		@Nullable
		@BindView(R.id.gagCount)
		TextView gagCount;

		private Context c;
		private GagObject gag;

		GagViewHolder(View view, Context context) {
			super(view);

			this.c = context;

			ButterKnife.bind(this, view);
		}

		public void setupView(GagObject g) {
			if (g != null) {
				this.gag = g;

				gagTitle.setText(gag.caption);
				if (gagCount != null)
					gagCount.setText(gag.votes + " " + c.getString(R.string.Points) + "  " +
							gag.comments + " " + c.getString(R.string.Comments));

				Picasso.with(c)
						.load(gag.images.get("cover"))
						.into(gagImage);

				gagImage.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (gag.media.size() > 0)
							new VideoPlayerDialog(c, gag.media.get("mp4")).show();
						else
							new ImageViewer(c, gag.images.get("normal")).show();
					}
				});
			}
		}
	}
}
