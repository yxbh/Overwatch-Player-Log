package me.benh.overwatchplayerlog.controllers.listanddetails;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.benh.overwatchplayerlog.R;
import me.benh.overwatchplayerlog.controllers.OwPlayerRecordCreateActivity;
import me.benh.overwatchplayerlog.controllers.OwPlayerRecordEditActivity;
import me.benh.overwatchplayerlog.data.OwPlayerRecord;
import me.benh.overwatchplayerlog.dummy.DummyContent;

/**
 * A fragment representing a single OwPlayerItem detail screen.
 * This fragment is either contained in a {@link OwPlayerItemListActivity}
 * in two-pane mode (on tablets) or a {@link OwPlayerItemDetailActivity}
 * on handsets.
 */
public class OwPlayerItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private OwPlayerRecord item;

    private static final int REQUEST_EDIT_RECORD = 999;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OwPlayerItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            item = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(item.getBattleTag());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.owplayeritem_detail, container, false);

        // setup floating action buttons
        FloatingActionButton fabEdit = (FloatingActionButton) rootView.findViewById(R.id.fab_edit);
        if (null != fabEdit) {
            fabEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), OwPlayerRecordEditActivity.class);
                    startActivityForResult(intent, REQUEST_EDIT_RECORD);
                }
            });
        }

        // Show the dummy content as text in a TextView.
        if (item != null) {
            ((TextView) rootView.findViewById(R.id.owplayeritem_detail)).setText(item.getBattleTag());
        }

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_EDIT_RECORD: {
                // TODO: update this fragment.
                break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
