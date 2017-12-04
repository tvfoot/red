package com.benoitquenaudon.tvfoot.red.injection.module

import android.support.v4.app.Fragment
import com.benoitquenaudon.tvfoot.red.injection.scope.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(val fragment: Fragment) {
  @Provides
  @FragmentScope
  fun provideFragment(): Fragment {
    return fragment
  }
}