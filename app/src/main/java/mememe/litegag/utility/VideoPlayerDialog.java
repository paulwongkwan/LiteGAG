package mememe.litegag.utility;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;

import mememe.litegag.R;


/**
 * Created by Wong Kwan on 14/1/2016.
 * Editing to play streaming video
 * inside a full screen dialog
 */
public class VideoPlayerDialog {

    private Dialog d;
    private Context c;
    private String url;
    private VideoView videoView;
	private ProgressDialog pDialog;
    private boolean loop = false;
	private int stopPosition;

	public VideoPlayerDialog(){
    }

    public VideoPlayerDialog(Context c, String url) {
        this.c = c;
        this.url = url;
    }

    public static VideoPlayerDialog makePlayer(Context c, String url) {
        VideoPlayerDialog vp = new VideoPlayerDialog(c, url);
        return vp;
    }

    public void setLoop(boolean set){
        loop = set;
    }

    public boolean show(){
	    pDialog = new ProgressDialog(c);

	    // Set progressbar message
	    pDialog.setMessage("Buffuring...");
	    pDialog.setIndeterminate(false);
	    pDialog.setCancelable(false);
	    // Show progressbar
	    pDialog.show();

        d = new Dialog(c, android.R.style.Theme_Black_NoTitleBar);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.videoplayerdialog);

        videoView = (VideoView) d.findViewById(R.id.videoplayer_view);

        d.show();

        Uri uri = Uri.parse(url);

        MediaController mc = new MediaController(c);
        videoView.setMediaController(mc);

        videoView.setVideoURI(uri);

        videoView.requestFocus();
        //videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                if(loop){
                    mp.setLooping(true);
                }else {
                    pDialog.dismiss();
                    videoView.start();
                }
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                dismiss();
            }
        });

	    videoView.setOnTouchListener(new View.OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
			    if(videoView.isPlaying()){
				    stopPosition = videoView.getCurrentPosition();
				    videoView.pause();
			    }else {
				    videoView.seekTo(stopPosition);
				    videoView.resume();
			    }
			    return false;
		    }
	    });

        return true;
    }

    public boolean dismiss(){
        if(d != null && d.isShowing()){
            if(videoView.isPlaying())
                videoView.stopPlayback();

            d.dismiss();

            return true;
        }

        return false;
    }
}
