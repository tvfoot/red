package com.benoitquenaudon.tvfoot.red.app.domain.libraries

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.app.common.BaseActivity
import com.benoitquenaudon.tvfoot.red.databinding.ActivityLibrariesBinding
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class LibrariesActivity : BaseActivity() {
  @Inject lateinit var librariesAdapter: LibrariesAdapter

  private val binding by lazy(NONE) {
    DataBindingUtil.setContentView<ActivityLibrariesBinding>(this, R.layout.activity_libraries)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activityComponent.inject(this)

    binding.librariesRecyclerView.adapter = librariesAdapter
  }
}
