package app.app.astrocoin.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import app.app.astrocoin.FragmentOrder;
import app.app.astrocoin.FragmentTransfers;

public class TabAdapters extends FragmentPagerAdapter {
    private final int tabCount;

    public TabAdapters(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentTransfers();
            case 1:
                return new FragmentOrder();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
