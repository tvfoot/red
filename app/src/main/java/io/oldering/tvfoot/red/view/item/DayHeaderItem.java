package io.oldering.tvfoot.red.view.item;

import com.genius.groupie.Item;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.databinding.DayHeaderItemBinding;
import io.oldering.tvfoot.red.viewmodel.DayHeaderViewModel;

public class DayHeaderItem extends Item<DayHeaderItemBinding> {
  public DayHeaderViewModel dayHeaderVM;

  public DayHeaderItem(DayHeaderViewModel dayHeaderViewModel) {
    this.dayHeaderVM = dayHeaderViewModel;
  }

  @Override public int getLayout() {
    return R.layout.day_header_item;
  }

  @Override public void bind(DayHeaderItemBinding viewBinding, int position) {
    viewBinding.setDayHeader(this.dayHeaderVM);
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DayHeaderItem)) return false;

    DayHeaderItem that = (DayHeaderItem) o;

    return dayHeaderVM.equals(that.dayHeaderVM);
  }

  @Override public int hashCode() {
    return dayHeaderVM.hashCode();
  }

  public String getKey() {
    return dayHeaderVM.getDisplayedDate();
  }
}
