package io.oldering.tvfoot.red.view.item;

import com.genius.groupie.Item;

import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.databinding.BroadcasterItemBinding;

public class BroadcasterItem extends Item<BroadcasterItemBinding> {
    private String broadcasterDrawableName;

    public BroadcasterItem(String broadcasterDrawableName) {
        this.broadcasterDrawableName = broadcasterDrawableName;
    }

    @Override
    public int getLayout() {
        return R.layout.broadcaster_item;
    }

    @Override
    public void bind(BroadcasterItemBinding viewBinding, int position) {
        viewBinding.setBroadcasterDrawableName(broadcasterDrawableName);
    }
}
