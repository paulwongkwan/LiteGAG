package mememe.litegag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mememe.litegag.adapter.GagAdapter;
import mememe.litegag.listener.InfiniteScrollListener;
import mememe.litegag.object.GagObject;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 2016-02-17
 * Wong Kwan
 * Fragment view
 * Include the Swipetorefreshview and
 * Recycleview. Support swipe to refresh
 * and infinite scroll.
 * <p/>
 * 2016-12-06
 * Added RxJava to improve code
 */

public class InfiniteScrollFrag extends Fragment {
	private static final String ARG_POSITION = "position";
	private static final String BASE_URL = "http://infinigag.k3min.eu/";

	@BindView(R.id.gagListView)
	RecyclerView gagListView;
	@BindView(R.id.refreshView)
	SwipeRefreshLayout refreshView;

	private int position;
	private String GAG_LINK;
	private String next = "";
	private String section = "";
	private boolean updating = false;

	private List<GagObject> gaglist = new ArrayList<>();

	private GagAdapter gAdapter;

	private HomeScreen parent;

	public InfiniteScrollFrag() {
		// Required empty public constructor
	}

	public static InfiniteScrollFrag newInstance(int position) {
		InfiniteScrollFrag f = new InfiniteScrollFrag();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	public static String getLink(int position) {
		String link = "http://infinigag.k3min.eu/";

		switch (position) {
			case 0:
				link += "hot/";
				break;
			case 1:
				link += "trending/";
				break;
			case 2:
				link += "fresh/";
				break;
		}

		return link;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		position = getArguments().getInt(ARG_POSITION);
		GAG_LINK = getLink(position);

		parent = (HomeScreen) getActivity();

		getGagList();
		gAdapter = new GagAdapter(getActivity(), gaglist);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		container = (ViewGroup) inflater.inflate(R.layout.fragment_infinite_scroll, container, false);
		ButterKnife.bind(this, container);

		gagListView = (RecyclerView) container.findViewById(R.id.gagListView);
		gagListView.setLayoutManager(new LinearLayoutManager(getActivity()));
		gagListView.setAdapter(gAdapter);
		gagListView.addOnScrollListener(new InfiniteScrollListener(1, gagListView.getLayoutManager()) {
			public void loadMore(int page, int totalItemsCount) {
				getGagList();
			}

			@Override
			public void onHide() {
				parent.hidebar();
			}

			@Override
			public void onShow() {
				parent.showbar();
			}
		});

		refreshView = (SwipeRefreshLayout) container.findViewById(R.id.refreshView);
		refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			public void onRefresh() {
				gaglist.clear();
				gAdapter.notifyDataSetChanged();
				next = "";
				getGagList();
			}
		});
		refreshView.setProgressViewOffset(true, getActivity().getResources().getDimensionPixelOffset(R.dimen.gagbox_width),
				getActivity().getResources().getDimensionPixelOffset(R.dimen.gagbox_width) * 2);

		return container;
	}

	public void getGagList() {
		if (!updating) {
			updating = true;

			JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(GAG_LINK + next, null,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject json) {
							if (json != null) {
								Observable.just(json)
										.flatMap(new Func1<JSONObject, Observable<JSONObject>>() {

											@Override
											public Observable<JSONObject> call(JSONObject jsonObject) {

												JSONArray array = null;
												ArrayList<JSONObject> jarray = new ArrayList<>();
												try {
													next = jsonObject.getJSONObject("paging").getString("next");
													array = jsonObject.getJSONArray("data");

													for (int i = 0; i < array.length(); i++) {
														jarray.add(array.getJSONObject(i));
													}
												} catch (JSONException e) {
													e.printStackTrace();
												}

												return Observable.from(jarray);
											}
										})
										.map(new Func1<JSONObject, GagObject>() {
											@Override
											public GagObject call(JSONObject oj) {
												GagObject tmp = new GagObject();
												try {
													tmp.id = oj.getString("id");
													tmp.caption = oj.getString("caption");
													tmp.link = oj.getString("link");
													tmp.setImages(oj.getJSONObject("images"));
													if (!oj.getString("media").equals("false")) {
														tmp.setMedia(oj.getJSONObject("media"));
													}
													tmp.votes = Integer.parseInt(oj.getJSONObject("votes").getString("count"));
													tmp.comments = Integer.parseInt(oj.getJSONObject("comments").getString("count"));
												} catch (JSONException e) {
													e.printStackTrace();
												}

												return tmp;
											}
										})
										.subscribeOn(Schedulers.io())
										.observeOn(AndroidSchedulers.mainThread())
										.subscribe(new Subscriber<GagObject>() {
											@Override
											public void onCompleted() {
												if (refreshView != null && refreshView.isRefreshing())
													refreshView.setRefreshing(false);
											}

											@Override
											public void onError(Throwable e) {
												Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_LONG).show();
											}

											@Override
											public void onNext(GagObject gagObject) {
												boolean unfound = true;
												for (int x = 0; x < gaglist.size(); x++) {
													GagObject tmpG = gaglist.get(x);
													if (tmpG.id.equals(gagObject.id)) {
														unfound = false;
														break;
													}
												}
												if (unfound) {
													gaglist.add(gagObject);
													gAdapter.notifyItemInserted(gaglist.size());
												}
											}
										});
							} else {
								//ajax error, show error code
								Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_LONG).show();
							}

							updating = false;
						}
					},
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							error.printStackTrace();
							Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_LONG).show();
						}
					});
			parent.addQueue(jsonObjectRequest);
		}
	}
}
