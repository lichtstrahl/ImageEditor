package root.iv.imageeditor.ui;

import android.os.Bundle;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import root.iv.imageeditor.R;
import root.iv.imageeditor.ui.fragments.SelectFragment;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.mainFrame)
    ViewGroup mainFrame;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    SmartTabLayout tabLayout;
    private FragmentPagerItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FragmentPagerItems pages = new FragmentPagerItems(this);
        pages.add(FragmentPagerItem.of("Поиск", SelectFragment.class));
        adapter = new FragmentPagerItemAdapter(getFragmentManager(), pages);

        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
    }
}
