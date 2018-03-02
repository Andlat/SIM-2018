package daynight.daynnight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by sebastien on 18-02-28.
 */

public class GridViewInventaireObjet extends android.support.v7.widget.AppCompatImageView {

    public GridViewInventaireObjet(Context context) {
        super(context);
    }

    public GridViewInventaireObjet(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewInventaireObjet(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec); // This is the key that will make the height equivalent to its width
    }
}