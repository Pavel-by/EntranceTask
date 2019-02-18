package com.example.entrancetask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AvatarImageView extends android.support.v7.widget.AppCompatImageView {

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;
    private int gender;
    private Bitmap mResultBitmap;
    private Bitmap starBitmap;
    private Bitmap heartBitmap;
    private Paint mDrawPaint = new Paint();

    public AvatarImageView(Context context) {
        super(context);
        init();
    }

    public AvatarImageView(
            Context context,
            @Nullable AttributeSet attrs
    )
    {
        super(context, attrs);
        init();
    }

    public AvatarImageView(
            Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr
    )
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        starBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.mask_star);
        heartBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.mask_heart);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mResultBitmap == null) {
            super.onDraw(canvas);
            return;
        }
        canvas.drawBitmap(mResultBitmap, 0, 0, mDrawPaint);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initializeBitmap();
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    private void initializeBitmap() {
        this.mResultBitmap = getBitmapFromDrawable(getDrawable());
        setupMask();
    }

    private Bitmap getMask() {
        return gender == Contact.GENDER_MALE ? starBitmap : heartBitmap;
    }

    private void setupMask() {
        if (mResultBitmap == null) return;
        Bitmap mask = getMask();
        Canvas tempCanvas = new Canvas(mResultBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        tempCanvas.drawBitmap(mask, null, new Rect(0, 0, mResultBitmap.getWidth(), mResultBitmap.getHeight()), paint);
        paint.setXfermode(null);
        invalidate();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null || getWidth() == 0 || getHeight() == 0) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), BITMAP_CONFIG);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
