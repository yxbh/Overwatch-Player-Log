package me.benh.lib.helpers;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import me.benh.overwatchplayerlog.R;

/**
 * Created by Benjamin Huang on 3/12/2016.
 */

public final class DrawableHelper {
    private DrawableHelper() {}

    public static Drawable getDrawableWithTint(Context context, @DrawableRes int resId, @ColorRes int colorId) {
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), resId, null);
        drawable = DrawableCompat.wrap(drawable);
        drawable = drawable.mutate();
        int color = ResourcesCompat.getColor(context.getResources(), colorId, null);
        DrawableCompat.setTint(drawable, color);
        return drawable;
    }

//    public static Drawable getDrawableWithTint(Context context, @DrawableRes int resId, @ColorRes int colorId) {
//        // http://stackoverflow.com/a/30928051/2170109
//        Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, resId));
//
//        /*
//         * need to use the filter | http://stackoverflow.com/a/30880522/2170109
//         * (even if compat should use it for pre-API21-devices | http://stackoverflow.com/a/27812472/2170109)
//         */
//        int color = ContextCompat.getColor(context, colorId);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            DrawableCompat.setTint(drawable, color);
//
//        } else {
//            drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
//        }
//
//        return drawable;
//    }
}
