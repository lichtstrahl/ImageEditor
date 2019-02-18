package root.iv.imageeditor.ui.fragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import root.iv.imageeditor.R;

public class FragmentHolder {
    int[] items = new int [] {R.id.menu_main_bottom_search, R.id.menu_main_bottom_edit};

    public static Fragment openEditFragment(FragmentManager manager, String bitmapPath) {
        Fragment fragment = EditFragment.getInstance(bitmapPath);
        return null;
    }
}
