/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pitchedapps.material.glass.utilities.color;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;

import com.pitchedapps.material.glass.utilities.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Utility methods for working with colors.
 */
public class ColorUtils {

    private ColorUtils() {
    }

    public static final int IS_LIGHT = 0;
    public static final int IS_DARK = 1;
    public static final int LIGHTNESS_UNKNOWN = 2;

    /**
     * Blend {@code color1} and {@code color2} using the given ratio.
     *
     * @param ratio of which to blend. 0.0 will return {@code color1}, 0.5 will give an even blend,
     *              1.0 will return {@code color2}.
     */
    public static
    @CheckResult
    @ColorInt
    int blendColors(@ColorInt int color1,
                    @ColorInt int color2,
                    @FloatRange(from = 0f, to = 1f) float ratio) {
        final float inverseRatio = 1f - ratio;
        float a = (Color.alpha(color1) * inverseRatio) + (Color.alpha(color2) * ratio);
        float r = (Color.red(color1) * inverseRatio) + (Color.red(color2) * ratio);
        float g = (Color.green(color1) * inverseRatio) + (Color.green(color2) * ratio);
        float b = (Color.blue(color1) * inverseRatio) + (Color.blue(color2) * ratio);
        return Color.argb((int) a, (int) r, (int) g, (int) b);
    }

    /**
     * Checks if the most populous color in the given palette is dark
     * <p/>
     * Annoyingly we have to return this Lightness 'enum' rather than a boolean as palette isn't
     * guaranteed to find the most populous color.
     */
    public static
    @Lightness
    int isDark(Palette palette, boolean forViewer) {
        Palette.Swatch mostPopulous = getMostPopulousSwatch(palette);
        if (mostPopulous == null) return LIGHTNESS_UNKNOWN;
        return isDark(mostPopulous.getHsl(), forViewer) ? IS_DARK : IS_LIGHT;
    }

    public static
    @Nullable
    Palette.Swatch getMostPopulousSwatch(Palette palette) {
        Palette.Swatch mostPopulous = null;
        if (palette != null) {
            for (Palette.Swatch swatch : palette.getSwatches()) {
                if (mostPopulous == null || swatch.getPopulation() > mostPopulous.getPopulation()) {
                    mostPopulous = swatch;
                }
            }
        }
        return mostPopulous;
    }

    public static
    @Nullable
    Palette.Swatch getLessPopulousSwatch(Palette palette) {
        Palette.Swatch lessPopulous = null;
        if (palette != null) {
            for (Palette.Swatch swatch : palette.getSwatches()) {
                if (lessPopulous == null || swatch.getPopulation() < lessPopulous.getPopulation()) {
                    lessPopulous = swatch;
                }
            }
        }
        return lessPopulous;
    }

    /**
     * Determines if a given bitmap is dark. This extracts a palette inline so should not be called
     * with a large image!!
     * <p/>
     * Note: If palette fails then check the color of the central pixel
     */
    /*
    public static boolean isDark(@NonNull Bitmap bitmap) {
        return isDark(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
    }
    */
    public static boolean isDark(@NonNull Bitmap bitmap, boolean useCustomDividers,
                                 int widthDivider, int heightDivider, boolean forViewer) {
        if (useCustomDividers) {
            return isDark(bitmap, bitmap.getWidth() / widthDivider, bitmap.getHeight() / heightDivider, forViewer);
        } else {
            return isDark(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, forViewer);
        }
    }

    /**
     * Determines if a given bitmap is dark. This extracts a palette inline so should not be called
     * with a large image!! If palette fails then check the color of the specified pixel
     */
    public static boolean isDark(@NonNull Bitmap bitmap, int backupPixelX, int backupPixelY, boolean forViewer) {
        // first try palette with a small color quant size
        Palette palette = Palette.from(bitmap).maximumColorCount(3).generate();
        if (palette != null && palette.getSwatches().size() > 0) {
            return isDark(palette, forViewer) == IS_DARK;
        } else {
            // if palette failed, then check the color of the specified pixel
            return isDark(bitmap.getPixel(backupPixelX, backupPixelY), forViewer);
        }
    }

    /**
     * Check that the lightness value (0–1)
     */
    public static boolean isDark(float[] hsl, boolean forViewer) { // @Size(3)
        float min = 0;
        if (forViewer) {
            min = 0.3f;
            Utils.showLog("HSL: " + hsl[2] + " Min: " + min + " for Viewer." + " Dark: " + String.valueOf(hsl[2] < min));
        } else {
            min = 0.4f;
            Utils.showLog("HSL: " + hsl[2] + " Min: " + min + " for toolbar." + " Dark: " + String.valueOf(hsl[2] < min));
        }
        return hsl[2] < min;
    }

    /**
     * Convert to HSL & check that the lightness value
     */
    public static boolean isDark(@ColorInt int color, boolean forViewer) {
        float[] hsl = new float[3];
        android.support.v4.graphics.ColorUtils.colorToHSL(color, hsl);
        return isDark(hsl, forViewer);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({IS_LIGHT, IS_DARK, LIGHTNESS_UNKNOWN})
    public @interface Lightness {

    }
}