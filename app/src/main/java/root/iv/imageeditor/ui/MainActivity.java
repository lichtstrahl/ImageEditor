package root.iv.imageeditor.ui;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import root.iv.imageeditor.R;
import root.iv.imageeditor.ui.fragments.SelectFragment;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.mainFrame)
    ViewGroup mainFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mainFrame, SelectFragment.getInstance())
                .commit();
    }
}
