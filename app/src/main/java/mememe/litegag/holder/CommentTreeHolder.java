package mememe.litegag.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.unnamed.b.atv.model.TreeNode;

import butterknife.BindView;
import butterknife.ButterKnife;
import mememe.litegag.R;
import mememe.litegag.transform.CircleTransform;

/**
 * Created by MEMEME-iClass on 8/7/2016.
 */
public class CommentTreeHolder extends TreeNode.BaseNodeViewHolder<CommentTreeHolder.CommentItem> {
	@BindView(R.id.comment_profilepic)
	ImageView comment_profilepic;
	@BindView(R.id.username_text)
	TextView username_text;
	@BindView(R.id.commtetn_content_text)
	TextView commtetn_content_text;
	@BindView(R.id.comment_expand)
	ImageView comment_expand;

	public CommentTreeHolder(Context context) {
		super(context);
	}

	@Override
	public View createNodeView(TreeNode node, CommentItem value) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.comment_layout, null, false);

		ButterKnife.bind(this, view);

		username_text.setText(value.username);
		commtetn_content_text.setText(value.comment);

		Picasso.with(context).load(value.imagePath).transform(new CircleTransform()).error(R.drawable.anonymous).into(comment_profilepic);

		if(node.getChildren().size() == 0){
			comment_expand.setVisibility(View.GONE);
		}

		return view;
	}

	@Override
	public void toggle(boolean active) {
		comment_expand.setImageResource(active ? R.mipmap.ic_expand_less_black_48dp : R.mipmap.ic_expand_more_black_48dp);
	}

	public static class CommentItem {
		public String imagePath;
		public String username;
		public String comment;

		public CommentItem(String imagePath, String username, String comment){
			this.comment = comment;
			this.imagePath = imagePath;
			this.username = username;
		}

	}
}
