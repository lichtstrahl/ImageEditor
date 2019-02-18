package root.iv.imageeditor.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import rapid.decoder.BitmapDecoder;
import root.iv.imageeditor.R;
import root.iv.imageeditor.app.App;
import root.iv.imageeditor.ui.fragments.EditFragment;
import root.iv.imageeditor.ui.fragments.FragmentHolder;
import root.iv.imageeditor.ui.fragments.SelectFragment;

public class MainActivity extends AppCompatActivity implements SelectFragment.Listener {
    private static final int POSITION_SELECT_FRAGMENT = 0;
    private static final int POSITION_EDIT_FRAGMENT = 1;

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
                .replace(R.id.mainFrame, SelectFragment.getInstance())
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void openEditFragment(String bitmapPath) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFrame, EditFragment.getInstance(bitmapPath))
                .addToBackStack(null)
                .commit();
    }

    private boolean bottomItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_bottom_search:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainFrame, SelectFragment.getInstance())
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.menu_main_bottom_edit:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainFrame, EditFragment.getInstance(null))
                        .addToBackStack(null)
                        .commit();
                return true;
        }
        return true;
    }
}
