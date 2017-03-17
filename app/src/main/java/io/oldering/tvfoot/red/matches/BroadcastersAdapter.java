package io.oldering.tvfoot.red.matches;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.databinding.BroadcasterRowBinding;
import java.util.ArrayList;
import java.util.List;

public class BroadcastersAdapter
    extends RecyclerView.Adapter<BroadcastersAdapter.BroadcasterViewHolder> {
  private List<BroadcasterRowDisplayable> broadcasters = new ArrayList<>();

  @Override public BroadcasterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

    BroadcasterRowBinding binding =
        DataBindingUtil.inflate(layoutInflater, R.layout.broadcaster_row, parent, false);
    return new BroadcasterViewHolder(binding);
  }

  @Override public void onBindViewHolder(BroadcasterViewHolder holder, int position) {
    holder.bind(broadcasters.get(position));
  }

  @Override public int getItemCount() {
    return broadcasters.size();
  }

  public void add(BroadcasterRowDisplayable broadcaster) {
    broadcasters.add(broadcaster);
  }

  static class BroadcasterViewHolder extends RecyclerView.ViewHolder {
    private final BroadcasterRowBinding binding;

    public BroadcasterViewHolder(BroadcasterRowBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(BroadcasterRowDisplayable broadcaster) {
      binding.setBroadcasterDrawableName(broadcaster.code());
    }
  }
}
