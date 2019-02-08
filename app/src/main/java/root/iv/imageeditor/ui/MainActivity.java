package root.iv.imageeditor.ui;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import root.iv.imageeditor.R;
import root.iv.imageeditor.ui.fragments.EditFragment;
import root.iv.imageeditor.ui.fragments.SelectFragment;

public class MainActivity extends AppCompatActivity implements SelectFragment.Listener {
    private static final int POSITION_SELECT_FRAGMENT = 0;
    private static final int POSITION_EDIT_FRAGMENT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
}
