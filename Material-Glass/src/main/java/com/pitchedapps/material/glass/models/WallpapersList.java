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

import java.util.ArrayList;

public class WallpapersList {

    public static ArrayList<WallpaperItem> wallsList = new ArrayList<>();

    public static void createWallpapersList(ArrayList<String> names, ArrayList<String> authors,
                                            ArrayList<String> urls, ArrayList<String> dimensions,
                                            ArrayList<String> copyrights) {
        try {
            for (int i = 0; i < names.size(); i++) {
                WallpaperItem wallItem =
                        new WallpaperItem(names.get(i), authors.get(i), urls.get(i),
                                dimensions.get(i), copyrights.get(i));
                wallsList.add(wallItem);
            }
        } catch (IndexOutOfBoundsException e) {
            //Do nothing
        }
    }

    public static ArrayList<WallpaperItem> getWallpapersList() {
        return wallsList;
    }

    public static void clearList() {
        wallsList.clear();
    }

}
