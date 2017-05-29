package com.barin.mdmappliation.application.util;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by barin on 4/18/2016.
 */
public class ToolbarColorizeHelper {

  public static void colorTheTextOnToolbar(Toolbar toolbarView, int toolbarIconsColor) {
    final PorterDuffColorFilter colorFilter =
        new PorterDuffColorFilter(toolbarIconsColor, PorterDuff.Mode.MULTIPLY);
    toolbarView.setTitleTextColor(toolbarIconsColor);
  }

  public static void changeVisibilityOfView(Toolbar toolbar, int childIndex, int visibility) {

    View view = toolbar.getChildAt(childIndex);
    view.setVisibility(visibility);
  }

  public static void changeDrawableView(Toolbar toolbar, int childIndex, int newDrawable) {
    View view = toolbar.getChildAt(childIndex);
    view.setBackgroundResource(newDrawable);
  }

  /**
   * Use this method to colorize toolbar icons to the desired target color
   *
   * @param toolbarView toolbar view being colored
   * @param toolbarIconsColor the target color of toolbar icons
   * @param activity reference to activity needed to register observers
   */

  public static void colorizeToolbar(Toolbar toolbarView, int toolbarIconsColor,
      Activity activity) {
    final PorterDuffColorFilter colorFilter =
        new PorterDuffColorFilter(toolbarIconsColor, PorterDuff.Mode.MULTIPLY);

    for (int i = 0; i < toolbarView.getChildCount(); i++) {
      final View v = toolbarView.getChildAt(i);

      //Step 1 : Changing the color of back button (or open drawer button).
      if (v instanceof ImageButton) {
        //Action Bar back button
        ((ImageButton) v).getDrawable().setColorFilter(colorFilter);
      }

/*      if (v instanceof ActionMenuView) {
        for (int j = 0; j < ((ActionMenuView) v).getChildCount(); j++) {

          //Step 2: Changing the color of any ActionMenuViews - icons that are not back button, nor text, nor overflow menu icon.
          //Colorize the ActionViews -> all icons that are NOT: back button | overflow menu
          final View innerView = ((ActionMenuView) v).getChildAt(j);
          if (innerView instanceof ActionMenuItemView) {
            for (int k = 0; k < ((ActionMenuItemView) innerView).getCompoundDrawables().length;
                k++) {
              if (((ActionMenuItemView) innerView).getCompoundDrawables()[k] != null) {
                final int finalK = k;

                //Important to set the color filter in seperate thread, by adding it to the message queue
                //Won't work otherwise.
                innerView.post(() -> ((ActionMenuItemView) innerView).getCompoundDrawables()[finalK].setColorFilter(
                    colorFilter));
              }
            }
          }
        }
      }*/

      //Step 3: Changing the color of title and subtitle.
      toolbarView.setTitleTextColor(toolbarIconsColor);
      toolbarView.setSubtitleTextColor(toolbarIconsColor);
    }
  }
}