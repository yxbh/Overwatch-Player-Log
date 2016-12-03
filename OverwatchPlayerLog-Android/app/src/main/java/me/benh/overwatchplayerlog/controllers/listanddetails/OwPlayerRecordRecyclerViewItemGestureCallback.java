package me.benh.overwatchplayerlog.controllers.listanddetails;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import junit.framework.Assert;

import me.benh.overwatchplayerlog.R;
import me.benh.overwatchplayerlog.common.Arguements;
import me.benh.overwatchplayerlog.common.Requests;
import me.benh.overwatchplayerlog.controllers.OwPlayerRecordEditActivity;
import me.benh.overwatchplayerlog.data.OwPlayerRecord;
import me.benh.overwatchplayerlog.data.OwPlayerRecordWrapper;
import me.benh.overwatchplayerlog.data.source.DataSource;
import me.benh.overwatchplayerlog.helpers.ActivityHelper;

/**
 * Created by Benjamin Huang on 22/11/2016.
 *
 * reference: https://www.learn2crack.com/2016/02/custom-swipe-recyclerview.html
 * reference: http://nemanjakovacevic.net/blog/english/2016/01/12/recyclerview-swipe-to-delete-no-3rd-party-lib-necessary/
 */

public class OwPlayerRecordRecyclerViewItemGestureCallback extends ItemTouchHelper.SimpleCallback {

    private static String TAG = OwPlayerRecordRecyclerViewItemGestureCallback.class.getSimpleName();

    private OwPlayerItemListActivity activity;
    private Paint paint = new Paint();
    private Drawable iconDelete;
    private Drawable iconEdit;
    private OwPlayerRecordRecyclerViewAdapter adapter;

    private Handler recordRemovalHandler = new Handler();

    public OwPlayerRecordRecyclerViewItemGestureCallback(OwPlayerItemListActivity activity, OwPlayerRecordRecyclerViewAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.activity = activity;
        this.adapter = adapter;
        paint.setColor(Color.RED);
        iconEdit = ContextCompat.getDrawable(activity, R.drawable.ic_edit);
        Assert.assertNotNull(iconEdit);
        iconDelete = ContextCompat.getDrawable(activity, R.drawable.ic_delete);
        Assert.assertNotNull(iconDelete);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // ignore drag and drop.
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        adapter.notifyItemChanged(position); // reset the item view.

        switch (direction) {
            case ItemTouchHelper.LEFT: {
                Log.v(TAG, "onSwiped:LEFT");
                final OwPlayerRecord record = adapter.getItem(position);
                adapter.removeItem(position);

                // setup for removal
                final Runnable recordDeleteRunnable = new Runnable() {
                    @Override
                    public void run() {
                        new DataSource(activity).removeOwPlayerRecord(record);
                    }
                };

                final Snackbar snackbar = Snackbar.make(viewHolder.itemView, R.string.snackbar_record_deleted, activity.getResources().getInteger(R.integer.timeOutBeforeDelete));
                snackbar
                    .setAction(R.string.snackbar_undo, new View.OnClickListener() {
                        final Runnable runnable = recordDeleteRunnable;
                        @Override
                        public void onClick(View view) {
                            recordRemovalHandler.removeCallbacks(runnable);
                            activity.refreshRecordsWithUi();
                            snackbar.dismiss();
                        }
                    })
                    .show();
                activity.setSnackbar(snackbar);

                recordRemovalHandler.postDelayed(
                    recordDeleteRunnable,
                    activity.getResources().getInteger(R.integer.timeOutBeforeDelete));

                break;
            }
            case ItemTouchHelper.RIGHT: {
                Log.v(TAG, "onSwiped:RIGHT");
                ActivityHelper.startEditActivity(activity, adapter.getItem(position));
                break;
            }
        }
    }

    /**
     * Called by ItemTouchHelper on RecyclerView's onDraw callback.
     * <p>
     * If you would like to customize how your View's respond to user interactions, this is
     * a good place to override.
     * <p>
     * Default implementation translates the child by the given <code>dX</code>,
     * <code>dY</code>.
     * ItemTouchHelper also takes care of drawing the child after other children if it is being
     * dragged. This is done using child re-ordering mechanism. On platforms prior to L, this
     * is
     * achieved via {@link android.view.ViewGroup#getChildDrawingOrder(int, int)} and on L
     * and after, it changes View's elevation value to be greater than all other children.)
     *
     * @param canvas            The canvas which RecyclerView is drawing its children
     * @param recyclerView      The RecyclerView to which ItemTouchHelper is attached to
     * @param viewHolder        The ViewHolder which is being interacted by the User or it was
     *                          interacted and simply animating to its original position
     * @param dX                The amount of horizontal displacement caused by user's action
     * @param dY                The amount of vertical displacement caused by user's action
     * @param actionState       The type of interaction on the View. Is either {@link
     *                          ItemTouchHelper#ACTION_STATE_DRAG} or
     *                          {@link ItemTouchHelper#ACTION_STATE_SWIPE}.
     * @param isCurrentlyActive True if this view is currently being controlled by the user or
     *                          false it is simply animating back to its original state.
     * @see #onChildDrawOver(Canvas, RecyclerView, RecyclerView.ViewHolder, float, float, int,
     * boolean)
     */
    @Override
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;
            float height = itemView.getHeight(); //(float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;

            float viewLeft = itemView.getLeft();
            float viewRight = itemView.getRight();
            float viewTop = itemView.getTop();
            float viewBottom = itemView.getBottom();

            boolean isSwipingRight = dX > 0;
            @ColorInt int backgroundColor = isSwipingRight ?
                    ContextCompat.getColor(activity, R.color.colorSwipeToEditBackground) :
                    ContextCompat.getColor(activity, R.color.colorSwipeToDeleteBackground);
            paint.setColor(backgroundColor);
            float backgroundLeft   = isSwipingRight ? viewLeft : viewRight + dX;
            float backgroundRight  = isSwipingRight ? dX : viewRight;
            float backgroundTop    = viewTop;
            float backgroundBottom = viewBottom;

            // float left, float top, float right, float bottom
            RectF backgroundRect = new RectF(backgroundLeft, backgroundTop, backgroundRight, backgroundBottom);
            canvas.drawRect(backgroundRect, paint);

            float iconLeft   = isSwipingRight ? viewLeft + width : viewRight - 2 * width;
            float iconRight  = isSwipingRight ? viewLeft + 2 * width : viewRight - width;
            float iconTop    = viewTop + width;
            float iconBottom = viewBottom - width;

            // float left, float top, float right, float bottom
            Rect iconRect = new Rect((int)iconLeft, (int)iconTop, (int)iconRight, (int)iconBottom);
            Drawable icon = isSwipingRight ? iconEdit : iconDelete;
            icon.setBounds(iconRect);
            icon.draw(canvas);
        }

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}