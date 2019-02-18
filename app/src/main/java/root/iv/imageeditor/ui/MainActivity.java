package root.iv.imageeditor.ui;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import root.iv.imageeditor.R;
import root.iv.imageeditor.ui.fragments.EditFragment;
import root.iv.imageeditor.ui.fragments.SelectFragment;

public class MainActivity extends AppCompatActivity implements SelectFragment.Listener {
    private String bitmapPath = null;

    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(this::bottomItemSelected);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFrame, SelectFragment.getInstance(), SelectFragment.TAG)
                .commit();

    }

    @Override
    public void openEditFragment(String bitmapPath) {
        this.bitmapPath = bitmapPath;
        bottomNavigationView.setSelectedItemId(R.id.menu_main_bottom_edit);
    }

    private boolean bottomItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_bottom_select:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainFrame, SelectFragment.getInstance(), SelectFragment.TAG)
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.menu_main_bottom_edit:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainFrame, EditFragment.getInstance(bitmapPath), EditFragment.TAG)
                        .addToBackStack(null)
                        .commit();
                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 1) {
            super.onBackPressed();
        } else {
            EditFragment fragment = (EditFragment)getSupportFragmentManager().findFragmentByTag(EditFragment.TAG);
            if (fragment != null && fragment.isVisible()) {
                getSupportFragmentManager().popBackStack();
                bottomNavigationView.setSelectedItemId(R.id.menu_main_bottom_select);
            }
        }
    }
}
