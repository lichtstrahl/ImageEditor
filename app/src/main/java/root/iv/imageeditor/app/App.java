package root.iv.imageeditor.app;

import android.app.Application;
import android.util.Log;

import root.iv.imageeditor.BuildConfig;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void logI(String msg) {
        Log.i(BuildConfig.TAG_GLOBAL, msg);
    }

    public static void logW(String msg) {
        Log.w(BuildConfig.TAG_GLOBAL, msg);
    }

    public static void logE(String msg) {
        Log.e(BuildConfig.TAG_GLOBAL, msg);
    }
}
