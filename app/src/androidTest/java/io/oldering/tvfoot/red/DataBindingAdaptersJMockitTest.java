package io.oldering.tvfoot.red;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DataBindingAdaptersJMockitTest {

    @Test
    public void nothing() {
    }

//    // Multie dex files define...
//    @Ignore
//    @Test
//    public void setImageResourceJMockit(@Mocked final Resources resources, @Mocked final Context context, @Mocked final ImageView imageView) {
//        String resourceName = "b1";
//        String packageName = "packageName";
//
//        new NonStrictExpectations(resources) {{
//            resources.getIdentifier(resourceName, "drawable", packageName);
//            result = R.drawable.b1;
//        }};
//        new NonStrictExpectations(context) {{
//            context.getResources();
//            result = resources;
//            context.getPackageName();
//            result = packageName;
//        }};
//        new NonStrictExpectations(imageView) {{
//            imageView.getContext();
//            result = context;
//        }};
//
//        new Verifications() {{
//            imageView.setImageResource(R.drawable.b1);
//        }};
//    }
}
