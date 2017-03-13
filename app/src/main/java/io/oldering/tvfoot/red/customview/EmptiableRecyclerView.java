package io.oldering.tvfoot.red.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * EmptyView を預かれる RecyclerView
 * see https://github.com/googlesamples/android-XYZTouristAttractions/blob/master/Application/src/main/java/com/example/android/xyztouristattractions/ui/AttractionsRecyclerView.java
 */
public class EmptiableRecyclerView extends RecyclerView {
  private View emptyView;

  private AdapterDataObserver dataObserver = new AdapterDataObserver() {
    @Override public void onChanged() {
      super.onChanged();
      updateEmptyView();
    }
  };

  public EmptiableRecyclerView(Context context) {
    super(context);
  }

  public EmptiableRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public EmptiableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setEmptyView(View emptyView) {
    this.emptyView = emptyView;
  }

  @Override public void setAdapter(@Nullable RecyclerView.Adapter adapter) {
    if (getAdapter() != null) {
      getAdapter().unregisterAdapterDataObserver(dataObserver);
    }
    if (adapter != null) {
      adapter.registerAdapterDataObserver(dataObserver);
    }
    super.setAdapter(adapter);
    updateEmptyView();
  }

  void updateEmptyView() {
    if (emptyView != null && getAdapter() != null) {
      boolean showEmptyView = getAdapter().getItemCount() == 0;
      emptyView.setVisibility(showEmptyView ? VISIBLE : GONE);
      setVisibility(showEmptyView ? GONE : VISIBLE);
    }
  }
}