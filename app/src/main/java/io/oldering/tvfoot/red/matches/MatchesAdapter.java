package io.oldering.tvfoot.red.matches;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.databinding.MatchesRowBinding;
import java.util.Collections;
import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder> {
  private List<MatchRowDisplayable> matches = Collections.emptyList();

  @Override public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

    MatchesRowBinding binding =
        DataBindingUtil.inflate(layoutInflater, R.layout.matches_row, parent, false);
    return new MatchViewHolder(binding);
  }

  @Override public void onBindViewHolder(MatchViewHolder holder, int position) {
    holder.bind(matches.get(position));
  }

  @Override public int getItemCount() {
    return matches.size();
  }

  public void setMatches(List<MatchRowDisplayable> matches) {
    this.matches = matches;
  }

  static class MatchViewHolder extends RecyclerView.ViewHolder {
    private final MatchesRowBinding binding;

    public MatchViewHolder(MatchesRowBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(MatchRowDisplayable match) {
      binding.setMatch(match);

      RecyclerView recyclerView = binding.matchBroadcasters;

      BroadcastersAdapter broadcastersAdapter = new BroadcastersAdapter();
      match.getBroadcasters().forEach(broadcastersAdapter::add);
      if (broadcastersAdapter.getItemCount() == 0) {
        broadcastersAdapter.add(
            BroadcasterRowDisplayable.builder().setCode("ic_tv_black_18px").build());
      }
      recyclerView.setAdapter(broadcastersAdapter);
    }
  }
}
