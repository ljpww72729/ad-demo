package cc.lkme.addemo;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by LinkedME06 on 12/04/2017.
 */

public class ImageBinding {
    @BindingAdapter({"app:srcDrawable"})
    public static void setSrcDrawable(final ImageView view, final Drawable imgDrawable) {
        view.setImageDrawable(imgDrawable);
    }
}
