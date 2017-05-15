package com.benoitquenaudon.tvfoot.red.app.common;

import android.databinding.Observable;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import javax.annotation.Nullable;

/**
 * {@link android.databinding.ObservableField<String>} に使うコールバックで
 * 文字列が更新されたら snackBar を表示する仕組み
 */
public class SnackBarChangedCallback extends Observable.OnPropertyChangedCallback {
  private final WeakReference<View> view;
  private final SnackBarViewModel viewModel;

  public SnackBarChangedCallback(View descendantOfCoordinatorLayout, SnackBarViewModel viewModel) {
    view = new WeakReference<>(descendantOfCoordinatorLayout);
    this.viewModel = viewModel;
  }

  @Override public void onPropertyChanged(Observable observable, int ignored) {
    if (view.get() == null || viewModel.getSnackBarText() == null) {
      return;
    }
    Snackbar snackBar =
        Snackbar.make(view.get(), viewModel.getSnackBarText(), Snackbar.LENGTH_LONG);
    View snackBarView = snackBar.getView();

    int snackBarTextId = android.support.design.R.id.snackbar_text;
    TextView textView = (TextView) snackBarView.findViewById(snackBarTextId);
    textView.setTextColor(Color.WHITE);

    snackBar.show();
  }

  public interface SnackBarViewModel {
    @Nullable String getSnackBarText();
  }
}
