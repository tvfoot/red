package com.benoitquenaudon.tvfoot.red.app.domain.libraries

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.app.common.BaseActivity
import com.benoitquenaudon.tvfoot.red.app.common.flowcontroller.FlowController
import com.benoitquenaudon.tvfoot.red.databinding.ActivityLibrariesBinding
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class LibrariesActivity : BaseActivity() {
  @Inject lateinit var flowController: FlowController
  @Inject lateinit var adapter: LibrariesAdapter
  @Inject lateinit var disposables: CompositeDisposable

  private val binding: ActivityLibrariesBinding by lazy(NONE) {
    DataBindingUtil.setContentView<ActivityLibrariesBinding>(this, R.layout.activity_libraries)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activityComponent.inject(this)

    setSupportActionBar(binding.librariesToolbar)
    supportActionBar?.let {
      it.setDisplayShowTitleEnabled(false)
      it.setDisplayHomeAsUpEnabled(true)
    }

    binding.librariesRecyclerView.adapter = adapter

    disposables.add(adapter.libraryClickObservable
        .subscribe { library -> flowController.toLibrary(library.link) })
  }

  override fun onDestroy() {
    disposables.dispose()
    super.onDestroy()
  }

}
