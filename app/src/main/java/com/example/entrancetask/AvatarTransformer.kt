package com.example.entrancetask

import android.graphics.*
import com.squareup.picasso.Transformation
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.Color.parseColor
import android.graphics.Bitmap



class AvatarTransformer(private val mask: Bitmap) : Transformation {

    private val key = "gender_" + mask.toString()

    override fun transform(source: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(
                source.width, source.height,
                Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)

        val paint = Paint()
        val rect = Rect(0, 0, source.width, source.height)

        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true

        canvas.drawARGB(0, 0, 0, 0)

        canvas.drawBitmap(mask, null, rect, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        canvas.drawBitmap(source, null, rect, paint)

        if (source != output) {
            source.recycle()
        }
        return output
    }

    override fun key(): String {
        return key
    }
}