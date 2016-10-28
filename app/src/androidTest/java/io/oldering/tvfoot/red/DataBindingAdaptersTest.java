package io.oldering.tvfoot.red;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DataBindingAdaptersTest {
    @Test
    public void setVisibilityGone() {
        View view = mock(View.class);
        DataBindingAdapters.setVisibility(view, false);
        verify(view).setVisibility(View.GONE);
    }

    @Test
    public void setVisibilityVisible() {
        View view = mock(View.class);
        DataBindingAdapters.setVisibility(view, true);
        verify(view).setVisibility(View.VISIBLE);
    }

    @Test
    public void setImageResource() {
        String resourceName = "b1";
        String packageName = "packageName";

        Resources resources = mock(Resources.class);
        when(resources.getIdentifier(resourceName, "drawable", packageName)).thenReturn(R.drawable.b1);

        Context context = mock(Context.class);
        when(context.getResources()).thenReturn(resources);
        when(context.getPackageName()).thenReturn(packageName);

        ImageView imageView = mock(ImageView.class);
        when(imageView.getContext()).thenReturn(context);

        DataBindingAdapters.setImageResource(imageView, resourceName);
        verify(imageView).setImageResource(R.drawable.b1);
    }
}
