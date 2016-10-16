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

    @Override
    public int getLayout() {
        return R.layout.day_header_item;
    }

    @Override
    public void bind(DayHeaderItemBinding viewBinding, int position) {
        viewBinding.setDayHeader(this.dayHeaderVM);
    }
}
