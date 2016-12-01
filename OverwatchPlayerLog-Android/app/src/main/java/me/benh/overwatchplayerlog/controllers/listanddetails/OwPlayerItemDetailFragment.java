package me.benh.overwatchplayerlog.controllers.listanddetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import junit.framework.Assert;

import me.benh.overwatchplayerlog.R;
import me.benh.overwatchplayerlog.common.Arguements;
import me.benh.overwatchplayerlog.data.OwPlayerRecord;
import me.benh.overwatchplayerlog.data.OwPlayerRecordWrapper;
import me.benh.overwatchplayerlog.helpers.LogHelper;

import static android.app.Activity.RESULT_OK;

/**
 * A fragment representing a single OwPlayerItem detail screen.
 * This fragment is either contained in a {@link OwPlayerItemListActivity}
 * in two-pane mode (on tablets) or a {@link OwPlayerItemDetailActivity}
 * on handsets.
 */
public class OwPlayerItemDetailFragment extends Fragment {
    public static final String TAG = OwPlayerItemDetailFragment.class.getSimpleName();

    /**
     * The fragment argument representing the {@link OwPlayerRecord} that this fragment
     * represents.
     */
    public static final String ARG_OWPLAYERRECORD = "owplayerrecord";

    /**
     * The dummy content this fragment is presenting.
     */
    private OwPlayerRecord item;

    private static final int REQUEST_EDIT_RECORD = 999;

    CollapsingToolbarLayout appBarLayout;
    TextView playerNote;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OwPlayerItemDetailFragment() {
    }

    public void setupViewContent(@NonNull OwPlayerRecord record) {
        if (appBarLayout != null) {
            appBarLayout.setTitle(record.getBattleTag());
        }

        if (playerNote != null) {
            playerNote.setText(record.getNote());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_OWPLAYERRECORD)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            item = ((OwPlayerRecordWrapper) getArguments().getParcelable(ARG_OWPLAYERRECORD)).getRecord();

            Activity activity = this.getActivity();
            appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.owplayeritem_detail, container, false);
        playerNote = (TextView) rootView.findViewById(R.id.player_note);
        Assert.assertNotNull(playerNote);

        setupViewContent(item);

//        // setup floating action buttons
//        FloatingActionButton fabEdit = (FloatingActionButton) rootView.findViewById(R.id.fab_edit);
//        if (null != fabEdit) {
//            fabEdit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Log.v(TAG, "onClick");
//                    Intent intent = new Intent(getActivity(), OwPlayerRecordEditActivity.class);
//                    intent.putExtra(OwPlayerRecordEditActivity.ARG_OWPLAYERRECORD, new OwPlayerRecordWrapper(item));
//                    startActivityForResult(intent, REQUEST_EDIT_RECORD);
//                }
//            });
//        }

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult");
        LogHelper.d_resultCode(TAG, resultCode);

        switch (requestCode) {
            case REQUEST_EDIT_RECORD: {
                Log.v(TAG, "REQUEST_EDIT_RECORD");
                if (resultCode == RESULT_OK) {
                    item = ((OwPlayerRecordWrapper) data.getParcelableExtra(Arguements.OWPLAYERRECORD)).getRecord();
                    setupViewContent(item);
                }
                break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
