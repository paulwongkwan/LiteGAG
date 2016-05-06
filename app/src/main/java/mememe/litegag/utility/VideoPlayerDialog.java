package mememe.litegag.utility;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
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

	public VideoPlayerDialog(){
    }

    public VideoPlayerDialog(Context c){
        this.c = c;
    }

    public VideoPlayerDialog(Context c, String url) {
        this.c = c;
        this.url = url;
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
	            pDialog.dismiss();
                videoView.start();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                dismiss();
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
