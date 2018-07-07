package com.example.android.bigscreen.userInterface;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.android.bigscreen.R;
import com.example.android.bigscreen.data.MovieInfo;
import com.example.android.bigscreen.network.NetworkApi;
import com.example.android.bigscreen.viewModel.MainActivityViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String RECYCYLER_VIEW_STATE_KEY = "recycler_view_state_key";
    private static Parcelable mRecyclerViewState;

    private MainActivityViewModel mMainActivityViewModel;
    private RecyclerView mRecyclerView;
    private MainActivityRecyclerViewAdapter mRecyclerViewAdapter;
    private GridLayoutManager mGridLayoutManager;
    private android.support.v7.widget.Toolbar mToolbar;
    private Spinner mSortBySpinner;
    private String sortBy = NetworkApi.SORT_BY_POPULARITY;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectViewModel();
        connectToolBar();
        connectRecyclerView();

    }
    @Override
    protected void onStart() {
        super.onStart();
        refreshFavoriesUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mRecyclerViewState != null){
            mGridLayoutManager.onRestoreInstanceState(mRecyclerViewState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mRecyclerViewState = mGridLayoutManager.onSaveInstanceState();
        outState.putParcelable(RECYCYLER_VIEW_STATE_KEY, mRecyclerViewState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null){
            mRecyclerViewState = savedInstanceState.getParcelable(RECYCYLER_VIEW_STATE_KEY);
        }
    }


    private void refreshFavoriesUi(){
        if(mSortBySpinner.getSelectedItem().toString().equals(getResources().getString(R.string.favorites))) {
            mMainActivityViewModel.setFavoritedMovieList();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MainActivityRecyclerViewAdapter.RESULT_CODE_FOR_CHANGED_MOVIE && resultCode == RESULT_OK){
            boolean isFavorite = data.getBooleanExtra(MainActivityRecyclerViewAdapter.INTENT_ON_RESULT_IS_FAVORITE, false);
            int clickedPosition = data.getIntExtra(MainActivityRecyclerViewAdapter.INTENT_EXTRA_CLICKED_POSITION, 0);
            mMainActivityViewModel.updateSingleMovieInfo(clickedPosition, isFavorite);
        }
    }


    private void connectViewModel(){
        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mMainActivityViewModel.getMovieInfos().observe(this, new Observer<ArrayList<MovieInfo>>() {
            @Override
            public void onChanged(@Nullable ArrayList<MovieInfo> movieInfos) {
                updateUi(movieInfos);
            }
        });

    }

    private void connectToolBar(){
        mToolbar = findViewById(R.id.toolbar);
        mSortBySpinner = findViewById(R.id.spinner_sort_by);
        mSortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mSortBySpinner.getSelectedItem().toString().equals(getResources().getString(R.string.popular))) {
                    sortBy = NetworkApi.SORT_BY_POPULARITY;
                    page = 1;
                    mMainActivityViewModel.setMovieList(sortBy, page);
                    mRecyclerView.addOnScrollListener(getEndlessOnScrollListener());


                }
                else if (mSortBySpinner.getSelectedItem().toString().equals(getResources().getString(R.string.top_rated))) {
                    sortBy = NetworkApi.SORT_BY_RATING;
                    page = 1;
                    mMainActivityViewModel.setMovieList(sortBy, page);
                    mRecyclerView.addOnScrollListener(getEndlessOnScrollListener());

                }
                else if (mSortBySpinner.getSelectedItem().toString().equals(getResources().getString(R.string.favorites))){
                    mMainActivityViewModel.setFavoritedMovieList();
                    mRecyclerView.clearOnScrollListeners();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.sort_by_array));
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortBySpinner.setAdapter(mAdapter);
    }


    private void connectRecyclerView(){
        int spanCount;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            spanCount = 3;
        }else{
            spanCount = 5;
        }

        ArrayList<MovieInfo> placeHolder = new ArrayList<>();
        mRecyclerView = findViewById(R.id.rv_movie);
        mGridLayoutManager = new GridLayoutManager(MainActivity.this, spanCount);
        mRecyclerViewAdapter = new MainActivityRecyclerViewAdapter(MainActivity.this, placeHolder);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        RecyclerView.OnScrollListener mEndlessScrollListener = getEndlessOnScrollListener();
        mRecyclerView.addOnScrollListener(mEndlessScrollListener);


    }

    private RecyclerView.OnScrollListener getEndlessOnScrollListener(){
        return new RecyclerView.OnScrollListener() {
            final int visibilityThreshold = 20;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastVisibleItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
                int currentListLength = mRecyclerViewAdapter.getItemCount();
                boolean isLoadingMoreData = mMainActivityViewModel.ismIsLoadingData();
                if((currentListLength - lastVisibleItemPosition < visibilityThreshold) && !isLoadingMoreData){
                    page++;
                    mMainActivityViewModel.loadMoreData(sortBy, page);
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        };
    }

    private void updateUi(ArrayList<MovieInfo> movieInfos)
    {
        mRecyclerViewAdapter = new MainActivityRecyclerViewAdapter(MainActivity.this, movieInfos);
        mRecyclerView.swapAdapter(mRecyclerViewAdapter, false);
    }

}
