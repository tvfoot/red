package com.benoitquenaudon.tvfoot.red.app.domain.matches;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.util.Pair;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import com.benoitquenaudon.tvfoot.red.R;
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchDisplayable;
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.HeaderRowDisplayable;
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.LoadingRowDisplayable;
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchesItemDisplayable;
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchesItemDisplayableDiffUtilCallback;
import com.benoitquenaudon.tvfoot.red.app.injection.scope.ActivityScope;
import com.benoitquenaudon.tvfoot.red.databinding.MatchesRowHeaderBinding;
import com.benoitquenaudon.tvfoot.red.databinding.MatchesRowMatchBinding;
import com.benoitquenaudon.tvfoot.red.databinding.RowLoadingBinding;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

@ActivityScope public class MatchesAdapter
    extends RecyclerView.Adapter<MatchesAdapter.MatchesItemViewHolder> {
  private List<MatchesItemDisplayable> matchesItems = Collections.emptyList();
  @SuppressWarnings("WeakerAccess") PublishSubject<Pair<MatchDisplayable, TextView>>
      matchRowClickObservable = PublishSubject.create();

  @Inject MatchesAdapter() {
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

  Observable<Pair<MatchDisplayable, TextView>> getMatchRowClickObservable() {
    return matchRowClickObservable;
  }

  @SuppressWarnings("unchecked") @Override
  public void onBindViewHolder(MatchesItemViewHolder holder, int position) {
    holder.bind(matchesItems.get(position));
  }

  @Override public void onViewRecycled(MatchesItemViewHolder holder) {
    super.onViewRecycled(holder);
    holder.unbind();
  }

  @Override public int getItemCount() {
    return matchesItems.size();
  }

  @Override public int getItemViewType(int position) {
    MatchesItemDisplayable item = matchesItems.get(position);
    if (item instanceof MatchDisplayable) {
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

  private MatchesItemDisplayableDiffUtilCallback diffUtilCallback =
      new MatchesItemDisplayableDiffUtilCallback();

  void setMatchesItems(List<MatchesItemDisplayable> newItems) {
    List<MatchesItemDisplayable> oldItems = this.matchesItems;
    this.matchesItems = newItems;

    diffUtilCallback.bindItems(oldItems, newItems);
    DiffUtil.calculateDiff(diffUtilCallback, true).dispatchUpdatesTo(this);
  }

  static abstract class MatchesItemViewHolder<B extends ViewDataBinding, T extends MatchesItemDisplayable>
      extends RecyclerView.ViewHolder {
    final B binding;

    MatchesItemViewHolder(B binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    abstract void bind(T item);

    abstract void unbind();
  }

  static private class MatchHeaderViewHolder
      extends MatchesItemViewHolder<MatchesRowHeaderBinding, HeaderRowDisplayable> {
    MatchHeaderViewHolder(MatchesRowHeaderBinding binding) {
      super(binding);
    }

    @Override void bind(HeaderRowDisplayable header) {
      binding.setDayHeader(header);
      binding.executePendingBindings();
    }

    @Override void unbind() {
      binding.setDayHeader(null);
      binding.executePendingBindings();
    }
  }

  public class MatchRowViewHolder
      extends MatchesItemViewHolder<MatchesRowMatchBinding, MatchDisplayable> {
    MatchRowViewHolder(MatchesRowMatchBinding binding) {
      super(binding);
    }

    @Override void bind(MatchDisplayable match) {
      binding.setMatch(match);
      binding.setHandler(this);
      binding.executePendingBindings();

      setBroadcastsAdapter(match);
    }

    @Override void unbind() {
      binding.setMatch(null);
      binding.setHandler(null);
      binding.matchBroadcasters.setAdapter(null);
      binding.executePendingBindings();
    }

    private void setBroadcastsAdapter(MatchDisplayable match) {
      RecyclerView recyclerView = binding.matchBroadcasters;

      BroadcastersAdapter broadcastersAdapter = new BroadcastersAdapter();
      broadcastersAdapter.addAll(match.broadcasters());

      //if (broadcastersAdapter.getItemCount() == 0) {
      //  broadcastersAdapter.add(
      //      BroadcasterRowDisplayable.builder().code("ic_tv_black_18px").build());
      //}
      recyclerView.setAdapter(broadcastersAdapter);
    }

    public void onClick(MatchDisplayable match) {
      MatchesRowMatchBinding binding = this.binding;
      matchRowClickObservable.onNext(Pair.create(match, binding.matchHeadline));
    }
  }

  static private class LoadingRowViewHolder
      extends MatchesItemViewHolder<RowLoadingBinding, LoadingRowDisplayable> {
    LoadingRowViewHolder(RowLoadingBinding binding) {
      super(binding);
    }

    @Override void bind(LoadingRowDisplayable item) {
      // nothing to do
    }

    @Override void unbind() {
      // nothing to do
    }
  }
}
