package daynight.daynnight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by sebastien on 18-02-28.
 */

public class ObjetGridView extends android.support.v7.widget.AppCompatImageView {

    public ObjetGridView(Context context) {
        super(context);
    }

    public ObjetGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObjetGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec); // This is the key that will make the height equivalent to its width
    }
}