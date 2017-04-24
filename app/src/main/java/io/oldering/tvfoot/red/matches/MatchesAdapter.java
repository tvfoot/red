package io.oldering.tvfoot.red.matches;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.databinding.MatchesRowHeaderBinding;
import io.oldering.tvfoot.red.databinding.MatchesRowMatchBinding;
import io.oldering.tvfoot.red.databinding.RowLoadingBinding;
import io.oldering.tvfoot.red.matches.displayable.BroadcasterRowDisplayable;
import io.oldering.tvfoot.red.matches.displayable.HeaderRowDisplayable;
import io.oldering.tvfoot.red.matches.displayable.LoadingRowDisplayable;
import io.oldering.tvfoot.red.matches.displayable.MatchRowDisplayable;
import io.oldering.tvfoot.red.matches.displayable.MatchesItemDisplayable;
import io.oldering.tvfoot.red.matches.displayable.MatchesItemDisplayableDiffUtilCallback;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import timber.log.Timber;

/**
 * TODO(benoit) find a better scope, or how to
 * clear the instance once the activity ends?
 */
@Singleton public class MatchesAdapter
    extends RecyclerView.Adapter<MatchesAdapter.MatchesItemViewHolder> {
  private List<MatchesItemDisplayable> matchesItems = Collections.emptyList();
  private PublishSubject<MatchRowDisplayable> matchRowClickObservable = PublishSubject.create();

  @Inject MatchesAdapter() {
    Timber.d("CONNARD");
  }

  @Override public MatchesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);

    switch (viewType) {
      case R.layout.matches_row_header:
        return new MatchHeaderViewHolder((MatchesRowHeaderBinding) binding);
      case R.layout.matches_row_match:
        return new MatchRowViewHolder((MatchesRowMatchBinding) binding);
      case R.layout.row_loading:
        return new LoadingRowViewHolder((RowLoadingBinding) binding);
      default:
        throw new UnsupportedOperationException(
            "don't know how to deal with this viewType: " + viewType);
    }
  }

  Observable<MatchesIntent.MatchRowClickIntent> getMatchRowClickObservable() {
    return matchRowClickObservable.map(MatchesIntent.MatchRowClickIntent::create);
  }

  @SuppressWarnings("unchecked") @Override
  public void onBindViewHolder(MatchesItemViewHolder holder, int position) {
    holder.bind(matchesItems.get(position));
  }

  @Override public int getItemCount() {
    return matchesItems.size();
  }

  @Override public int getItemViewType(int position) {
    MatchesItemDisplayable item = matchesItems.get(position);
    if (item instanceof MatchRowDisplayable) {
      return R.layout.matches_row_match;
    }
    if (item instanceof HeaderRowDisplayable) {
      return R.layout.matches_row_header;
    }
    if (item instanceof LoadingRowDisplayable) {
      return R.layout.row_loading;
    }
    throw new UnsupportedOperationException("Don't know how to deal with this item: " + item);
  }

  public void onClick(MatchRowDisplayable match) {
    matchRowClickObservable.onNext(match);
  }

  private MatchesItemDisplayableDiffUtilCallback diffUtilCallback =
      new MatchesItemDisplayableDiffUtilCallback();

  void setMatchesItems(List<MatchesItemDisplayable> newItems) {
    List<MatchesItemDisplayable> oldItems = this.matchesItems;
    this.matchesItems = newItems;

    diffUtilCallback.bindItems(oldItems, newItems);
    DiffUtil.calculateDiff(diffUtilCallback, true).dispatchUpdatesTo(this);
  }

  abstract class MatchesItemViewHolder<B extends ViewDataBinding, T extends MatchesItemDisplayable>
      extends RecyclerView.ViewHolder {
    final B binding;

    MatchesItemViewHolder(B binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    abstract void bind(T item);
  }

  private class MatchHeaderViewHolder
      extends MatchesItemViewHolder<MatchesRowHeaderBinding, HeaderRowDisplayable> {
    MatchHeaderViewHolder(MatchesRowHeaderBinding binding) {
      super(binding);
    }

    @Override void bind(HeaderRowDisplayable header) {
      binding.setDayHeader(header);
    }
  }

  private class MatchRowViewHolder
      extends MatchesItemViewHolder<MatchesRowMatchBinding, MatchRowDisplayable> {
    MatchRowViewHolder(MatchesRowMatchBinding binding) {
      super(binding);
    }

    @Override void bind(MatchRowDisplayable match) {
      binding.setMatch(match);
      binding.setHandler(MatchesAdapter.this);
      binding.executePendingBindings();

      setBroadcastsAdapter(match);
    }

    private void setBroadcastsAdapter(MatchRowDisplayable match) {
      RecyclerView recyclerView = binding.matchBroadcasters;

      BroadcastersAdapter broadcastersAdapter = new BroadcastersAdapter();
      broadcastersAdapter.addAll(match.broadcasters());

      if (broadcastersAdapter.getItemCount() == 0) {
        broadcastersAdapter.add(
            BroadcasterRowDisplayable.builder().code("ic_tv_black_18px").build());
      }
      recyclerView.setAdapter(broadcastersAdapter);
    }
  }

  private class LoadingRowViewHolder
      extends MatchesItemViewHolder<RowLoadingBinding, LoadingRowDisplayable> {
    LoadingRowViewHolder(RowLoadingBinding binding) {
      super(binding);
    }

    @Override void bind(LoadingRowDisplayable item) {
      // nothing to do
    }
  }
}
