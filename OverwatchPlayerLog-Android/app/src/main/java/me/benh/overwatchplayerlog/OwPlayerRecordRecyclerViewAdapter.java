package me.benh.overwatchplayerlog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.benh.overwatchplayerlog.data.OwPlayerRecord;

/**
 * Created by Benjamin Huang on 22/11/2016.
 */

class OwPlayerRecordRecyclerViewAdapter
        extends RecyclerView.Adapter<OwPlayerRecordRecyclerViewAdapter.ViewHolder> {

    private final OwPlayerItemListActivity activity;
    private final List<OwPlayerRecord> records;

    OwPlayerRecordRecyclerViewAdapter(OwPlayerItemListActivity activity, List<OwPlayerRecord> records) {
        this.activity = activity;
        this.records = records;
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
        holder.playerBattleTag.setText(records.get(position).getBattleTag());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.isTwoPane()) {
                    Bundle arguments = new Bundle();
                    arguments.putString(OwPlayerItemDetailFragment.ARG_ITEM_ID, holder.item.getId());
                    OwPlayerItemDetailFragment fragment = new OwPlayerItemDetailFragment();
                    fragment.setArguments(arguments);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.owplayeritem_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, OwPlayerItemDetailActivity.class);
                    intent.putExtra(OwPlayerItemDetailFragment.ARG_ITEM_ID, holder.item.getId());

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView playerBattleTag;
        public OwPlayerRecord item;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            playerBattleTag = (TextView) view.findViewById(R.id.playerBattleTag);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + playerBattleTag.getText() + "'";
        }
    }
}
