package com.drkhannah.concerts;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drkhannah.concerts.adapters.ConcertsRecyclerViewAdapter;
import com.drkhannah.concerts.models.Concert;

import java.util.List;


public class ConcertListFragment extends Fragment implements GetConcertsTask.GetConcertsTaskResultCallback {

    private static final String LOG_TAG = ConcertListFragment.class.getSimpleName();

    private RecyclerView mConcertsRecyclerView;
    private ConcertsRecyclerViewAdapter mConcertsRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLinearLayoutManager;
    private TextView mEmptyView;

    public ConcertListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //get a handle to the RecyclerView in activity_main.xml
        mConcertsRecyclerView = (RecyclerView) rootView.findViewById(R.id.concerts_recyclerview);
        mEmptyView = (TextView) rootView.findViewById(R.id.empty_view);

        // use a linear layout manager for the RecyclerView
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mConcertsRecyclerView.setLayoutManager(mLinearLayoutManager);

        // specify an adapter for the RecyclerView
        mConcertsRecyclerViewAdapter = new ConcertsRecyclerViewAdapter(getActivity());
        mConcertsRecyclerView.setAdapter(mConcertsRecyclerViewAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getConcerts();
    }

    //inflate options menu in MainActivity's Toolbar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    //respond to Toolbar Actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void getConcerts() {
        //Check network connection before "executing" GetConcertsTask
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //get the artist name saved in the com.drkhannah.concerts.CONCERTS_SHARED_PREFERENCE_FILE Shared Preferences file
            SharedPreferences sharedPrefs = getActivity().getSharedPreferences(getString(R.string.preference_file_key), getActivity().MODE_PRIVATE);
            String artistNameFromSharedPrefs = sharedPrefs.getString(getString(R.string.shared_prefs_artist_name), getString(R.string.default_artsit_name));
            GetConcertsTask getConcertsTask = new GetConcertsTask(getActivity());
            getConcertsTask.execute(artistNameFromSharedPrefs);
        } else {
            Log.e(LOG_TAG, "Not connected to network");
        }
    }

    //implementation of GetConcertsTaskResultCallback.getConcertsTaskResult()
    @Override
    public void getConcertsTaskResult(List<Concert> result) {
        if (result != null) {
            //update the RecyclerViewAdapter data
            mConcertsRecyclerViewAdapter.updateData(result);
            mConcertsRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mConcertsRecyclerViewAdapter.updateData(result);
            mConcertsRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }
}
