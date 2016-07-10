package mememe.litegag.comment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mememe.litegag.R;
import mememe.litegag.holder.CommentTreeHolder;
import mememe.litegag.object.Comments;
import mememe.litegag.object.Data;
import mememe.litegag.service.CommentService;
import mememe.litegag.singleton.networking.RetrofitClient;
import mememe.litegag.singleton.setting.CommentSetting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Wong Kwan on 8/7/2016.
 */
public class CommentDialog extends DialogFragment {

	private static Boolean isOpened = false;

	@BindView(R.id.comment_loading_bar)
	ProgressBar comment_loading_bar;
	@BindView(R.id.comments_container)
	RelativeLayout comments_container;

	private String id;
	private Comments comments;

	public static synchronized CommentDialog make(String id){
		if(!isOpened) {
			isOpened = true;

			CommentDialog commentDialog = new CommentDialog();

			Bundle bundle = new Bundle();
			bundle.putString("ID", id);
			commentDialog.setArguments(bundle);

			return commentDialog;
		}else {
			return null;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		View v = inflater.inflate(R.layout.comment_dialog_layout, container);
		ButterKnife.bind(this, v);

		id = getArguments().getString("ID");

		new Thread(new Runnable() {
			@Override
			public void run() {
				getComments(id);
			}
		}).start();

		return v;
	}

	@Override
	public void onDismiss(final DialogInterface dialog) {
		super.onDismiss(dialog);
		isOpened = false;
	}

	private void getComments(String id){
		CommentService service = RetrofitClient.getInstance().create(CommentService.class);

		Call<Comments> call = service.getComments(id, CommentSetting.getInstance().getCommentLevel(), "score", CommentSetting.getInstance().getCommentAmount());

		call.enqueue(new Callback<Comments>() {
			@Override
			public void onResponse(Call<Comments> call, Response<Comments> response) {
				comments = response.body();

				Log.e("TreeBuild", call.request().url().toString());
				Log.e("TreeBuild", comments.getMessage());

				buildTreeView();
			}

			@Override
			public void onFailure(Call<Comments> call, Throwable t) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}

	private void buildTreeView(){
		if(comments != null && comments.getData() != null && comments.getData().size() > 0){
			List<Data> commentslist = comments.getData();

			TreeNode root = buildTreeRelation(TreeNode.root(), commentslist);

			AndroidTreeView tView = new AndroidTreeView(getActivity(), root);
			tView.setDefaultAnimation(true);
			tView.setDefaultContainerStyle(R.style.TreeNodeStyle);
			tView.setDefaultViewHolder(CommentTreeHolder.class);
			comments_container.addView(tView.getView());

			comment_loading_bar.setVisibility(View.GONE);
		}else{
			dismiss();
		}
	}

	private TreeNode buildTreeRelation(TreeNode node, List<Data> list){

		Log.e("TreeNode", list.size()+"");

		ArrayList<TreeNode> nodes = new ArrayList<>();
		for(Data comment : list){
			Log.e("TreeNode", comment.getChildren().getCount()+"");
			TreeNode child = new TreeNode(new CommentTreeHolder.CommentItem(comment.getUser().getAvatar()
					, comment.getUser().getDisplayName()
					, comment.getText()));
			List<Data> childlist = comment.getChildren().getData();
			if(childlist != null) {
				child = buildTreeRelation(child, childlist);
			}
			nodes.add(child);
		}

		if(nodes.size()>0){
			node.addChildren(nodes);
		}

		return node;
	}

}
