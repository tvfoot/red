package io.oldering.tvfoot.red.matches;

import android.support.v7.util.DiffUtil;
import java.util.List;

class MatchRowDisplayableDiffUtilCallback extends DiffUtil.Callback {
  private List<MatchRowDisplayable> oldItems;
  private List<MatchRowDisplayable> newItems;

  MatchRowDisplayableDiffUtilCallback() {
  }

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

  void bindItems(List<MatchRowDisplayable> oldItems, List<MatchRowDisplayable> newItems) {
    this.oldItems = oldItems;
    this.newItems = newItems;
  }
}
