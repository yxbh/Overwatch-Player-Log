package me.benh.overwatchplayerlog.controllers.listanddetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.benh.overwatchplayerlog.R;
import me.benh.overwatchplayerlog.data.OwPlayerRecord;
import me.benh.overwatchplayerlog.data.OwPlayerRecordWrapper;

/**
 * Created by Benjamin Huang on 22/11/2016.
 */

class OwPlayerRecordRecyclerViewAdapter
        extends RecyclerView.Adapter<OwPlayerRecordRecyclerViewAdapter.ViewHolder> {

    private final OwPlayerItemListActivity activity;
    private final List<OwPlayerRecord> records = new ArrayList<>();
    private OwPlayerItemDetailFragment detailFragment;
    private int currentDetailFragmentItemPosition;

    OwPlayerRecordRecyclerViewAdapter(@NonNull OwPlayerItemListActivity activity, @NonNull List<OwPlayerRecord> records) {
        this.activity = activity;
        this.records.addAll(records);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.owplayeritem_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = records.get(position);
        holder.playerBattleTag.setText(holder.item.getBattleTag());
        holder.playerFavorite.setVisibility(holder.item.isFavorite() ? View.VISIBLE : View.INVISIBLE);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.isTwoPane()) {
                    currentDetailFragmentItemPosition = holder.getAdapterPosition();
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(OwPlayerItemDetailFragment.ARG_OWPLAYERRECORD, new OwPlayerRecordWrapper(holder.item));
                    detailFragment = new OwPlayerItemDetailFragment();
                    detailFragment.setArguments(arguments);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.owplayeritem_detail_container, detailFragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, OwPlayerItemDetailActivity.class);
                    intent.putExtra(OwPlayerItemDetailFragment.ARG_OWPLAYERRECORD, new OwPlayerRecordWrapper(holder.item));

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public OwPlayerRecord getItem(int position) {
        return records.get(position);
    }

    public void removeItem(int position) {
        // remove fragment
        if (activity.isTwoPane() && position == currentDetailFragmentItemPosition) {
            activity.getSupportFragmentManager().beginTransaction()
                    .remove(this.detailFragment).commit();
        }

        // remove from container and refresh view
        this.records.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, this.records.size());
    }

    public void swapData(List<OwPlayerRecord> items) {
        this.records.clear();
        this.records.addAll(items);
        this.notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView playerBattleTag;
        public final ImageView playerFavorite;
        public OwPlayerRecord item;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            playerBattleTag = (TextView) view.findViewById(R.id.player_battletag);
            playerFavorite = (ImageView) view.findViewById(R.id.player_favorite);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + playerBattleTag.getText() + "'";
        }
    }
}
