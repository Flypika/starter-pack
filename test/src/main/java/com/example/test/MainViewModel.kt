package com.example.test

import android.os.Handler
import com.flypika.pack.ui.base.viewmodel.BaseViewAction
import com.flypika.pack.ui.base.viewmodel.BaseViewModel
import com.flypika.pack.ui.livedata.manager.Strategy

class MainViewModel : BaseViewModel<MainViewModel.MainViewAction>() {

    interface MainViewAction : BaseViewAction

    init {
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                viewActionManager.postEvent(Strategy.ONE_EXECUTE) {
                    showMessage("ONE_EXECUTE")
                }
                handler.postDelayed(this, 5000L)
            }
        }, 5000L)

        handler.postDelayed(object : Runnable {
            override fun run() {
                viewActionManager.postEvent(Strategy.SKIP) {
                    showMessage("SKIP")
                }
                handler.postDelayed(this, 2500L)
            }
        }, 2500L)
    }
}
