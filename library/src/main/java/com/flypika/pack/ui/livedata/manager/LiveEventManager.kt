package com.flypika.pack.ui.livedata.manager

import androidx.lifecycle.Lifecycle
import com.flypika.pack.ui.livedata.LiveEvent

class LiveEventManager<E : Any> {

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
        sendEvent(event, strategy, LiveEvent<E>::setValue)
    }

    fun postEvent(strategy: Strategy = Strategy.ONE_EXECUTE, event: E) {
        sendEvent(event, strategy, LiveEvent<E>::postValue)
    }

    private inline fun sendEvent(
        event: E,
        strategy: Strategy,
        send: LiveEvent<E>.(E) -> Unit
    ) {
        val liveEvent: LiveEvent<E> = getLiveEvent(event, strategy)
        when (strategy) {
            Strategy.ONE_EXECUTE -> liveEvent.send(event)
            Strategy.SKIP -> if (liveEvent.hasActiveObservers()) liveEvent.send(event)
        }
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
