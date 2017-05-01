package io.oldering.tvfoot.red.app.domain.matches.displayable;

import android.support.v7.util.DiffUtil;
import java.util.List;

public class MatchesItemDisplayableDiffUtilCallback extends DiffUtil.Callback {
  private List<MatchesItemDisplayable> oldItems;
  private List<MatchesItemDisplayable> newItems;

  public MatchesItemDisplayableDiffUtilCallback() {
  }

  @Override public int getOldListSize() {
    return oldItems.size();
  }

  @Override public int getNewListSize() {
    return newItems.size();
  }

  @Override public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
    MatchesItemDisplayable oldItem = oldItems.get(oldItemPosition);
    MatchesItemDisplayable newItem = newItems.get(newItemPosition);

    return oldItem.isSameAs(newItem);
  }

  @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
    MatchesItemDisplayable oldItem = oldItems.get(oldItemPosition);
    MatchesItemDisplayable newItem = newItems.get(newItemPosition);

    return oldItem.equals(newItem);
  }

  public void bindItems(List<MatchesItemDisplayable> oldItems,
      List<MatchesItemDisplayable> newItems) {
    this.oldItems = oldItems;
    this.newItems = newItems;
  }
}
