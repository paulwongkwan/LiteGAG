package mememe.litegag.utility;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import mememe.litegag.R;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Wong Kwan on 28/8/2015.
 * For bitmap viewing, have draging
 * and zoom function.
 *
 * 3rd library:
 * PhotoView
 */
public class ImageViewer {

    File file;
    Context c;
    ImageView iv;
    Dialog d;
    PhotoViewAttacher mAttacher;
    ProgressBar pb;
    String url;

    boolean openable = false;
    boolean webloading = false;

    public ImageViewer() {
    }

    public ImageViewer(Context c, File f) {
        this.c = c;
        this.file = f;

        openable = checkFile();
    }

    public ImageViewer(Context c, String url) {
        this.c = c;
        this.url = url;

        webloading = true;
    }

    public static ImageViewer makeViewer(Context c, File f){
        ImageViewer iv = new ImageViewer(c, f);
        return iv;
    }

    public static ImageViewer makeViewer(Context c, String url) {
        ImageViewer iv = new ImageViewer(c, url);
        return iv;
    }

    private boolean checkFile() {
        return file != null && file.exists() && file.isFile();
    }

    public void show() {
        if (openable || webloading) {
            d = new Dialog(c, android.R.style.Theme_Black_NoTitleBar);
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.imageviewerlayout);

            iv = (ImageView) d.findViewById(R.id.imageviewer_image);
            pb = (ProgressBar) d.findViewById(R.id.imageviewer_loading);

            Callback cb = new Callback() {
                @Override
                public void onSuccess() {
                    mAttacher = new PhotoViewAttacher(iv);
                    pb.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    dismiss();
                }
            };

            if (webloading) {
                Picasso.with(c)
                        .load(url)
                        .into(iv, cb);
            } else {
                Picasso.with(c)
                        .load(file)
                        .into(iv, cb);
            }

            d.show();
        }
    }

    public void dismiss() {
        if (d.isShowing())
            d.dismiss();
    }
}
