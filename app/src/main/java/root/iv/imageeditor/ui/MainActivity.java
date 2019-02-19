package root.iv.imageeditor.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import root.iv.imageeditor.R;
import root.iv.imageeditor.app.App;
import root.iv.imageeditor.ui.fragments.EditFragment;
import root.iv.imageeditor.ui.fragments.SelectFragment;

public class MainActivity extends AppCompatActivity implements SelectFragment.Listener {
    private static final String ADD_BACK_STACK_EDIT     = "backstack:edit";
    private static final String ADD_BACK_STACK_SELECT   = "backstack:select";

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
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openEditFragment(String bitmapPath) {
        this.bitmapPath = bitmapPath;
        bottomNavigationView.setSelectedItemId(R.id.menu_main_bottom_edit);
    }

    private boolean bottomItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        if (itemID != bottomNavigationView.getSelectedItemId()) {
            switch (itemID) {
                case R.id.menu_main_bottom_select:
                    if (getSupportFragmentManager().getBackStackEntryCount() > 1) getSupportFragmentManager().popBackStack();
                    break;

                case R.id.menu_main_bottom_edit:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainFrame, EditFragment.getInstance(bitmapPath), EditFragment.TAG)
                            .addToBackStack(ADD_BACK_STACK_EDIT)
                            .commit();
                    break;
                default:
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int count = getSupportFragmentManager().getBackStackEntryCount();
        App.logI("count: " + count);
        if (count == 0) {   // Значит ушла последняя транзация
            finish();
        } else {            // Ушла транзакция Edit
            bottomNavigationView.setSelectedItemId(R.id.menu_main_bottom_select);
        }
    }

    private void clearBackStack() {
        while (getSupportFragmentManager().popBackStackImmediate());
    }
}
