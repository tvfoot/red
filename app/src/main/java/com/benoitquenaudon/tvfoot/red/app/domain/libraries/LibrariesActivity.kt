package com.benoitquenaudon.tvfoot.red.app.domain.libraries

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.app.common.BaseActivity
import com.benoitquenaudon.tvfoot.red.app.common.flowcontroller.FlowController
import com.benoitquenaudon.tvfoot.red.databinding.ActivityLibrariesBinding
import com.benoitquenaudon.tvfoot.red.util.errorHandlingSubscribe
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class LibrariesActivity : BaseActivity() {
  @Inject
  lateinit var flowController: FlowController
  @Inject
  lateinit var adapter: LibrariesAdapter
  @Inject
  lateinit var disposables: CompositeDisposable

  private val binding: ActivityLibrariesBinding by lazy(NONE) {
    DataBindingUtil.setContentView<ActivityLibrariesBinding>(this, R.layout.activity_libraries)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setSupportActionBar(binding.librariesToolbar)
    supportActionBar?.run {
      setDisplayShowTitleEnabled(false)
      setDisplayHomeAsUpEnabled(true)
    }

    binding.librariesRecyclerView.adapter = adapter

    disposables
        .add(adapter.libraryClickObservable
            .errorHandlingSubscribe { library -> flowController.toLibrary(library.link) })
  }

  override fun onDestroy() {
    disposables.dispose()
    super.onDestroy()
  }

}
