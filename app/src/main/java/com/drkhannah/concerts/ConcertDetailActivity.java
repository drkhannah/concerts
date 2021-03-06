package com.drkhannah.concerts;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.drkhannah.concerts.data.ConcertsContract;

/**
 * Created by dhannah on 11/21/16.
 */

public class ConcertDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concert_detail);

        //get the Intent data that contains the concert Uri
        Uri concertUri = getIntent().getData();
        String artistName = ConcertsContract.ConcertEntry.getArtistNameFromUri(concertUri);

        setUpAppBar(artistName);

        if (savedInstanceState == null) {
            //if savedInstanceState is null,
            // then this activity isn't coming back from a pause state,
            // its being created for the first time

            // Create the ConcertDetailFragment
            // pass Concert object to  ConcertDetailFragment.newInstance() to be set as a fragment argument
            ConcertDetailFragment fragment = ConcertDetailFragment.newInstance(concertUri);

            //add it to the activity using FragmentManager
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.concert_detail_container, fragment)
                    .commit();
        }
    }

    private void setUpAppBar(String artistName) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(artistName);

        //get device orientation
        int orientation = getResources().getConfiguration().orientation;

        //get appbarlayout
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        //if device is in landscape mode, collapse the appbar
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            appBarLayout.setExpanded(false);
        }
    }
}
