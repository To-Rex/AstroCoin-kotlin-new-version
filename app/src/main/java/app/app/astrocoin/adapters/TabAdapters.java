package app.app.astrocoin.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import app.app.astrocoin.FragmentOrder;
import app.app.astrocoin.FragmentTransfers;

public class TabAdapters extends FragmentPagerAdapter {
    private static final List<Fragment> fragments = new ArrayList<>(2);
    public TabAdapters(@NonNull @NotNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentTransfers();
            case 1:
                return new FragmentOrder();
        }
        return new FragmentTransfers();
    }

    @Override
    public int getCount() {
        return 2;
    }
    public void addFragment(Fragment fragment){
        fragments.add(fragment);
    }
}