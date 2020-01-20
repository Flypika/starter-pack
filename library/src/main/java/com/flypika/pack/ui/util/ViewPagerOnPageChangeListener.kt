package com.flypika.pack.ui.util

import androidx.viewpager.widget.ViewPager

interface ViewPagerOnPageChangeListener : ViewPager.OnPageChangeListener {

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {}
}
