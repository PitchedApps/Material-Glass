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

public class IconsLists {

    private String name;
    private ArrayList<IconItem> iconsArray = new ArrayList<>();
    private static ArrayList<IconsCategory> categoriesList = new ArrayList<>();

    public IconsLists(String name) {
        this.name = name;
    }

    public IconsLists(String name, ArrayList<IconItem> iconsArray) {
        this.name = name;
        this.iconsArray = iconsArray;
    }

    public IconsLists(ArrayList<IconsCategory> categoriesList) {
        IconsLists.categoriesList = categoriesList;
    }

    public String getCategoryName() {
        return this.name;
    }

    public void setCategoryName(String name) {
        this.name = name;
    }

    public ArrayList<IconItem> getIconsArray() {
        return iconsArray.size() > 0 ? this.iconsArray : null;
    }

    public static ArrayList<IconsCategory> getCategoriesList() {
        return categoriesList.size() > 0 ? categoriesList : null;
    }

}