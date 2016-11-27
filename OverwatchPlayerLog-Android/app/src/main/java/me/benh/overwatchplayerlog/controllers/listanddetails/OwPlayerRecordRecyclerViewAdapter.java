package me.benh.overwatchplayerlog.controllers.listanddetails;

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

    private final List<OwPlayerRecord> allRecords = new ArrayList<>();
    private final List<OwPlayerRecord> filteredRecords = new ArrayList<>();

    private String filterQuery = "";

    private OwPlayerItemDetailFragment detailFragment;
    private int currentDetailFragmentItemPosition;

    private boolean isFavoriteOnly = false;

    OwPlayerRecordRecyclerViewAdapter(@NonNull OwPlayerItemListActivity activity, @NonNull List<OwPlayerRecord> records) {
        this.activity = activity;
        this.allRecords.addAll(records);
        this.filteredRecords.addAll(this.allRecords);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.owplayeritem_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = filteredRecords.get(position);
        holder.playerBattleTag.setText(holder.item.getBattleTag());
        holder.playerFavorite.setVisibility(holder.item.isFavorite() ? View.VISIBLE : View.GONE);
        holder.playerRatingLike.setVisibility(holder.item.getRating() == OwPlayerRecord.Rating.Like ? View.VISIBLE : View.GONE);
        holder.playerRatingDislike.setVisibility(holder.item.getRating() == OwPlayerRecord.Rating.Dislike ? View.VISIBLE : View.GONE);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.isTwoPane()) {
                    currentDetailFragmentItemPosition = holder.getAdapterPosition();
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(OwPlayerItemDetailFragment.ARG_OWPLAYERRECORD, new OwPlayerRecordWrapper(holder.item));
                    detailFragment = new OwPlayerItemDetailFragment();
                    detailFragment.setArguments(arguments);
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.owplayeritem_detail_container, detailFragment)
                            .commit();
                } else {
                    Intent intent = new Intent(activity, OwPlayerItemDetailActivity.class);
                    intent.putExtra(OwPlayerItemDetailFragment.ARG_OWPLAYERRECORD, new OwPlayerRecordWrapper(holder.item));

                    activity.startActivityForResult(intent, OwPlayerItemListActivity.REQUEST_VIEW_RECORD_DETAIL);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredRecords.size();
    }

    public OwPlayerRecord getItem(int position) {
        return filteredRecords.get(position);
    }

    public void removeItem(int position) {
        // remove fragment
        if (activity.isTwoPane() && position == currentDetailFragmentItemPosition) {
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .remove(this.detailFragment)
                    .commit();
        }

        // remove from container
        OwPlayerRecord record = this.filteredRecords.remove(position);
        this.allRecords.remove(record);

        // refresh view
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, this.filteredRecords.size());
    }

    public void swapData(List<OwPlayerRecord> items) {
        this.allRecords.clear();
        this.allRecords.addAll(items);

        this.filter(this.filterQuery);

        this.notifyDataSetChanged();
    }

    public void filter(@NonNull String queryText) {
        filterQuery = queryText;
        filteredRecords.clear();

        if (queryText.isEmpty()) {
            filteredRecords.addAll(allRecords);
        } else {
            for (OwPlayerRecord record : allRecords) {
                if (record.getBattleTag().toLowerCase().contains(queryText)) {
                    filteredRecords.add(record);
                }
            }
        }

        if (isFavoriteOnly) {
            filterFavorite();
        }
    }

    // remove non-favorites from filtered.
    private void filterFavorite() {
        List<OwPlayerRecord> removalList = new ArrayList<>();
        for (OwPlayerRecord record : filteredRecords) {
            if (!record.isFavorite()) {
                removalList.add(record);
            }
        }

        for (OwPlayerRecord record : removalList) {
            filteredRecords.remove(record);
        }
    }

    public boolean isFavoriteOnly() {
        return isFavoriteOnly;
    }

    public void setFavoriteOnly(boolean favoriteOnly) {
        isFavoriteOnly = favoriteOnly;
        if (isFavoriteOnly()) {
            filterFavorite();
        } else {
            filter(this.filterQuery);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        final TextView playerBattleTag;
        final ImageView playerFavorite;
        final ImageView playerRatingLike;
        final ImageView playerRatingDislike;
        public OwPlayerRecord item;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            playerBattleTag = (TextView) view.findViewById(R.id.player_battletag);
            playerFavorite = (ImageView) view.findViewById(R.id.player_favorite);
            playerRatingLike = (ImageView) view.findViewById(R.id.player_rating_like);
            playerRatingDislike = (ImageView) view.findViewById(R.id.player_rating_dislike);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + playerBattleTag.getText() + "'";
        }
    }
}
