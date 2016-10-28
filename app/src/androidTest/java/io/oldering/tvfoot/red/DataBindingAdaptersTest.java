package io.oldering.tvfoot.red;

import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
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

    // TODO(benoit) don't know how to test this properly
    // @Test
    // public void setImageResource() {
    //      Resources resources = mock(Resources.class);
    //      Context context = mock(Context.class);
    //      when(context.getResources()).thenReturn(resources);
    //      ImageView imageView = new ImageView(context);
    //
    //      DataBindingAdapters.setImageResource(imageView, "b1");
    //      verify(imageView).setImageResource(R.drawable.b1);
    // }
}
