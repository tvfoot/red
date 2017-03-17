package io.oldering.tvfoot.red.matches;

import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.databinding.MatchesRowBinding;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import java.util.Collections;
import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchRowViewHolder> {
  private List<MatchRowDisplayable> matches = Collections.emptyList();
  private PublishSubject<MatchRowDisplayable> matchRowClickObservable = PublishSubject.create();

  @Override public MatchRowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

    MatchesRowBinding binding =
        DataBindingUtil.inflate(layoutInflater, R.layout.matches_row, parent, false);

    return new MatchRowViewHolder(binding);
  }

  Observable<MatchesIntent> getMatchRowClickObservable() {
    return matchRowClickObservable.map(MatchesIntent.MatchRowClick::new);
  }

  @Override public void onBindViewHolder(MatchRowViewHolder holder, int position) {
    holder.bind(matches.get(position));
  }

  @Override public int getItemCount() {
    return matches.size();
  }

  public void onClick(MatchRowDisplayable match) {
    matchRowClickObservable.onNext(match);
  }

  private MatchRowDisplayableDiffUtilCallback diffUtilCallback =
      new MatchRowDisplayableDiffUtilCallback();

  public void setMatches(List<MatchRowDisplayable> newItems) {
    List<MatchRowDisplayable> oldItems = this.matches;
    this.matches = newItems;

    diffUtilCallback.bindItems(oldItems, newItems);
    DiffUtil.calculateDiff(diffUtilCallback, true).dispatchUpdatesTo(this);
  }

  class MatchRowViewHolder extends RecyclerView.ViewHolder {
    private final MatchesRowBinding binding;

    MatchRowViewHolder(MatchesRowBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(MatchRowDisplayable match) {
      binding.setMatch(match);
      binding.setHandler(MatchesAdapter.this);
      binding.executePendingBindings();

      RecyclerView recyclerView = binding.matchBroadcasters;

      BroadcastersAdapter broadcastersAdapter = new BroadcastersAdapter();
      match.broadcasters().forEach(broadcastersAdapter::add);
      if (broadcastersAdapter.getItemCount() == 0) {
        broadcastersAdapter.add(
            BroadcasterRowDisplayable.builder().code("ic_tv_black_18px").build());
      }
      recyclerView.setAdapter(broadcastersAdapter);
    }
  }
}
