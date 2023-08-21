package com.ch4vi.topRatedShows.featureTVShows.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ch4vi.topRatedShows.R
import com.ch4vi.topRatedShows.databinding.FragmentHomeBinding
import com.ch4vi.topRatedShows.featureTVShows.domain.model.TVShow
import com.ch4vi.topRatedShows.featureTVShows.domain.util.EventObserver
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<HomeViewModel>()
    private val adapter by lazy { TVShowListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.home_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.action_sort -> {
                            viewModel.dispatch(HomeEvent.ToggleSorting)
                            true
                        }

                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewState()
        initUI()
    }

    private fun observeViewState() {
        viewModel.viewState.observe(viewLifecycleOwner, EventObserver { onViewStateChanged(it) })
    }

    private fun onViewStateChanged(state: HomeViewState) {
        onLoading(false)
        when (state) {
            HomeViewState.Loading -> onLoading(true)
            HomeViewState.GenericError -> showError(null)
            is HomeViewState.NetworkError -> showError(state.message)
            is HomeViewState.BindTVShows -> bindShows(state.items)
        }
    }

    private fun initUI() {
        if (adapter.itemCount == 0) viewModel.dispatch(HomeEvent.Init)

        with(binding) {
            tvShowsList.adapter = adapter
            tvShowsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    if (linearLayoutManager?.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                        viewModel.dispatch(HomeEvent.GetNextPage)
                    }
                }
            })
        }
    }

    private fun onLoading(showLoader: Boolean) {
        with(binding) {
            loadingIndicatorGroup.visibility = if (!showLoader) View.GONE else View.VISIBLE
        }
    }

    private fun bindShows(items: List<TVShow>) {
        adapter.addShows(items)
    }

    private fun showError(message: String?) {
        binding.root.let {
            Snackbar.make(it, message ?: "error", Snackbar.LENGTH_SHORT).show()
        }
    }
}
