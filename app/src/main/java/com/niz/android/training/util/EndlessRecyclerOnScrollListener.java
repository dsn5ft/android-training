package com.niz.android.training.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 3; // The minimum amount of items to have below your current scroll position before loading more.

    private int currentPage = 1;

    private StaggeredGridLayoutManager layoutManager;

    public EndlessRecyclerOnScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);


        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int[] firstVisibleItems = null;
        firstVisibleItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItems);
        if(firstVisibleItems != null && firstVisibleItems.length > 0) {
            previousTotal = firstVisibleItems[0];
        }

        if (loading) {
            if ((visibleItemCount + previousTotal) >= totalItemCount) {
                loading = false;
                onLoadMore(1);
            }
        }


//        int visibleItemCount = recyclerView.getChildCount();
//        int totalItemCount = layoutManager.getItemCount();
//        int[] firstVisibleItems = layoutManager.findFirstVisibleItemPositions(null);
//        if (firstVisibleItems.length > 0) {
//            previousTotal = firstVisibleItems[0];
//        }
//
//        if (loading) {
//            if ((visibleItemCount + previousTotal) >= totalItemCount) {
//                loading = false;
//                onLoadMore(currentPage);
//            }
//        }

//        if (loading) {
//            if (totalItemCount > previousTotal) {
//                loading = false;
//                previousTotal = totalItemCount;
//            }
//        }

//        if (!loading && (totalItemCount - visibleItemCount) <= (first + visibleThreshold)) {
//            currentPage++;
//            onLoadMore(currentPage);
//            loading = true;
//        }
    }

    public abstract void onLoadMore(int currentPage);
}