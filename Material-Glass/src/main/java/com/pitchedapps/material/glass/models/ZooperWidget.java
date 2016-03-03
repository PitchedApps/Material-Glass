/*
 * Copyright (c) 2016.  Jahir Fiquitiva
 *
 * Licensed under the CreativeCommons Attribution-ShareAlike
 * 4.0 International License. You may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *    http://creativecommons.org/licenses/by-sa/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Big thanks to the project contributors. Check them in the repository.
 *
 */

/*
 *
 */

package com.pitchedapps.material.glass.models;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.pitchedapps.material.glass.utilities.Utils;

public class ZooperWidget {

    public final String name;
    public final Bitmap preview;

    public ZooperWidget(String name, Bitmap preview) {
        this.name = name;
        this.preview = preview;
    }

    public String getName() {
        return this.name;
    }

    public Bitmap getPreview() {
        return this.preview;
    }

    public Bitmap getTransparentBackgroundPreview() {
        return Utils.getBitmapWithReplacedColor(this.preview, Color.parseColor("#555555"), Color.TRANSPARENT);
    }
}
