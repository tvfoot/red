package io.oldering.tvfoot.red.matches;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.data.model.Match;
import io.oldering.tvfoot.red.databinding.MatchesRowBinding;
import java.util.Collections;
import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchesViewHolder> {
  private List<Match> matches = Collections.emptyList();

  @Override public MatchesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

    MatchesRowBinding binding =
        DataBindingUtil.inflate(layoutInflater, R.layout.matches_row, parent, false);
    return new MatchesViewHolder(binding);
  }

  @Override public void onBindViewHolder(MatchesViewHolder holder, int position) {
    holder.bind(matches.get(position));
  }

  @Override public int getItemCount() {
    return matches.size();
  }

  public void setMatches(List<Match> matches) {
    this.matches = matches;
  }

  static class MatchesViewHolder extends RecyclerView.ViewHolder {
    private final MatchesRowBinding binding;

    public MatchesViewHolder(MatchesRowBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(Match match) {
      binding.setMatch(match);
    }
  }
}
