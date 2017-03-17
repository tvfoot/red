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

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder> {
  private List<MatchRowDisplayable> matches = Collections.emptyList();
  PublishSubject<MatchRowDisplayable> matchRowClickObservable = PublishSubject.create();

  @Override public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

    MatchesRowBinding binding =
        DataBindingUtil.inflate(layoutInflater, R.layout.matches_row, parent, false);

    return new MatchViewHolder(binding);
  }

  public Observable<MatchesIntent> getMatchRowClickObservable() {
    return matchRowClickObservable.map(MatchesIntent.MatchRowClick::new);
  }

  @Override public void onBindViewHolder(MatchViewHolder holder, int position) {
    holder.bind(matches.get(position));
  }

  @Override public int getItemCount() {
    return matches.size();
  }

  public void onClick(MatchRowDisplayable match) {
    matchRowClickObservable.onNext(match);
  }

  public void setMatches(List<MatchRowDisplayable> newItems) {
    List<MatchRowDisplayable> oldItems = this.matches;
    this.matches = newItems;

    if (oldItems.isEmpty()) {
      notifyDataSetChanged();
      return;
    }

    DiffUtil.calculateDiff(new DiffUtil.Callback() {
      @Override public int getOldListSize() {
        return oldItems.size();
      }

      @Override public int getNewListSize() {
        return newItems.size();
      }

      @Override public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        MatchRowDisplayable oldItem = oldItems.get(oldItemPosition);
        MatchRowDisplayable newItem = newItems.get(newItemPosition);

        return oldItem.matchId().equals(newItem.matchId());
      }

      @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        MatchRowDisplayable oldItem = oldItems.get(oldItemPosition);
        MatchRowDisplayable newItem = newItems.get(newItemPosition);

        return oldItem.equals(newItem);
      }
    }, true).dispatchUpdatesTo(this);
  }

  class MatchViewHolder extends RecyclerView.ViewHolder {
    private final MatchesRowBinding binding;

    public MatchViewHolder(MatchesRowBinding binding) {
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
