package root.iv.imageeditor.util.anim;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;

public class AnimationManager {

    private AnimationManager() {
        throw new UnsupportedOperationException("Не поддерживает создание объектов");
    }

    public static void moveToScreen(View target) {
        ViewGroup parent = (ViewGroup)target.getParent();
        int width = parent.getWidth();
        SpringAnimation anim = new SpringAnimation(target, DynamicAnimation.TRANSLATION_X);
        anim.animateToFinalPosition(-width);
    }

    public static void changeAlpha(View target, float alpha) {
        SpringAnimation anim = new SpringAnimation(target, DynamicAnimation.ALPHA);
        anim.animateToFinalPosition(alpha);
    }

    public static void translate(View target, float dx, float dy) {
        SpringAnimation anim;
        anim = new SpringAnimation(target, DynamicAnimation.TRANSLATION_Y);
        anim.animateToFinalPosition(dy);

        anim = new SpringAnimation(target, DynamicAnimation.TRANSLATION_X);
        anim.animateToFinalPosition(dx);
    }
}
