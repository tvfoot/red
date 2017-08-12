package com.benoitquenaudon.tvfoot.red.util

import android.support.v4.app.Fragment
import com.benoitquenaudon.tvfoot.red.injection.component.ActivityComponent
import com.benoitquenaudon.tvfoot.red.injection.component.FragmentComponent
import com.benoitquenaudon.tvfoot.red.injection.module.FragmentModule

fun Fragment.newFragmentComponent(activityComponent: ActivityComponent): FragmentComponent {
  return activityComponent.plus(FragmentModule(this))
}