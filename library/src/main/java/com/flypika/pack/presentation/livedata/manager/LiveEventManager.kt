package com.flypika.pack.presentation.livedata.manager

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import com.flypika.pack.presentation.livedata.LiveEvent

class LiveEventManager<E : Any> {

    private val handler = Handler(Looper.getMainLooper())

    private val map: MutableMap<String, LiveEvent<E>> = HashMap()

    private val observers: MutableList<Pair<Lifecycle, FunObserver<E>>> = ArrayList()

    fun addObserver(lifecycle: Lifecycle, observer: FunObserver<E>) {
        map.values.forEach { liveData ->
            liveData.observe(
                { lifecycle },
                observer
            )
        }
        observers += lifecycle to observer
    }

    fun removeObserver(observer: FunObserver<E>) {
        map.values.forEach { liveData ->
            liveData.removeObserver(observer)
        }
        observers.removeAll { it.second == observer }
    }

    fun setEvent(strategy: Strategy = Strategy.ONE_EXECUTE, event: E) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw IllegalStateException("setEvent not on main thread")
        }
        val liveEvent: LiveEvent<E> = getLiveEvent(event, strategy)
        when (strategy) {
            Strategy.ONE_EXECUTE -> liveEvent.value = event
            Strategy.SKIP -> if (liveEvent.hasActiveObservers()) liveEvent.value = event
        }
    }

    fun postEvent(strategy: Strategy = Strategy.ONE_EXECUTE, event: E) {
        handler.post { setEvent(strategy, event) }
    }

    private fun getLiveEvent(event: E, strategy: Strategy): LiveEvent<E> {
        val key: String = getKey(event, strategy)
        return map.getOrPut(key, this::createLiveEvent)
    }

    private fun createLiveEvent() = LiveEvent<E>().also { liveEvent ->
        observers.forEach {
            liveEvent.observe(
                { it.first },
                it.second
            )
        }
    }

    private fun getKey(event: E, strategy: Strategy) =
        "${event::class.java.name}${strategy.name}"
}
