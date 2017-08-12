package com.benoitquenaudon.tvfoot.red.app.common

import android.support.v4.app.Fragment
import com.benoitquenaudon.tvfoot.red.injection.component.FragmentComponent
import com.benoitquenaudon.tvfoot.red.util.newFragmentComponent

abstract class BaseFragment : Fragment() {
  val activityComponent by lazy {
    (activity as BaseActivity).activityComponent
  }

  val fragmentComponent: FragmentComponent by lazy {
    this.newFragmentComponent(activityComponent)
  }
}