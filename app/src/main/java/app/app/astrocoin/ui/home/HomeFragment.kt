package app.app.astrocoin.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import app.app.astrocoin.FragmentOrder
import app.app.astrocoin.FragmentTransfers
import app.app.astrocoin.R
import app.app.astrocoin.adapters.TabAdapters
import com.google.android.material.tabs.TabLayout
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private var tabAdapters: TabAdapters? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)

        tabAdapters = TabAdapters(childFragmentManager)
        tabAdapters?.addFragment(FragmentTransfers())
        tabAdapters?.addFragment(FragmentOrder())

        viewPager!!.adapter = tabAdapters
        tabLayout!!.setupWithViewPager(viewPager)
        tabLayout!!.setTabTextColors(Color.parseColor("#C9C9C9"), Color.parseColor("#5733D1"))
        tabLayout!!.setSelectedTabIndicatorColor(Color.parseColor("#5733D1"))

        Objects.requireNonNull(tabLayout!!.getTabAt(0))!!.text = "Transfer"
        Objects.requireNonNull(tabLayout!!.getTabAt(1))!!.text = "Orders"

    }
}