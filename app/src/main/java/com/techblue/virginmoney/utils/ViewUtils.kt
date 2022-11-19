package com.techblue.virginmoney.utils

import android.content.Context
import android.widget.Toast
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techblue.virginmoney.listeners.ItemCountListener
import com.techblue.virginmoney.listeners.NavigationListener

fun <T : Any, VH : RecyclerView.ViewHolder> RecyclerView.prepareLinearList(pagingAdapter: PagingDataAdapter<T, VH>) {
    this.apply {
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )
        adapter = pagingAdapter
    }

}

fun <T : Any, VH : RecyclerView.ViewHolder> RecyclerView.prepareGridList(pagingAdapter: PagingDataAdapter<T, VH>, columns: Int = 2) {
    this.apply {
        layoutManager = GridLayoutManager(context, columns)
        addItemDecoration(ItemOffsetDecoration(context, com.intuit.sdp.R.dimen._8sdp))
        adapter = pagingAdapter
    }
}

fun <T : Any, VH : RecyclerView.ViewHolder> PagingDataAdapter<T, VH>.observeLoader(navigationListener: NavigationListener, context: Context?, itemCountListener: ItemCountListener) {
    this.addLoadStateListener { loadState ->
        // show empty list
        if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
            navigationListener.showProgress()
        } else {
            navigationListener.hideProgress()
            // If we have an error, show a toast
            val errorState = when {
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.let {
                Toast.makeText(context, it.error.message, Toast.LENGTH_LONG).show()
            }
        }

        if (loadState.append.endOfPaginationReached) {
            itemCountListener.onItemCountChangeListener(this.itemCount)
        }
    }
}

