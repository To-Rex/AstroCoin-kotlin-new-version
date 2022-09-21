package app.app.astrocoin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import app.app.astrocoin.FragmentTransfers
import app.app.astrocoin.FragmentOrder
import app.app.astrocoin.adapters.TabAdapters
import java.util.ArrayList

class TabAdapters(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return FragmentTransfers()
            1 -> return FragmentOrder()
        }
        return FragmentTransfers()
    }

    override fun getCount(): Int {
        return 2
    }

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
    }

    companion object {
        private val fragments: MutableList<Fragment> = ArrayList(2)
    }
}