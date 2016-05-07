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

package jahirfiquitiva.iconshowcase.models;

import android.os.Parcel;
import android.os.Parcelable;

public class WallpaperItem implements Parcelable {

    public static final Creator<WallpaperItem> CREATOR = new Creator<WallpaperItem>() {
        @Override
        public WallpaperItem createFromParcel(Parcel in) {
            return new WallpaperItem(in);
        }

        @Override
        public WallpaperItem[] newArray(int size) {
            return new WallpaperItem[size];
        }
    };

    private String wallName, wallAuthor, wallUrl, wallDimensions, wallCopyright;

    public WallpaperItem(String wallName, String wallAuthor, String wallUrl, String wallDimensions, String wallCopyright) {
        this.wallName = wallName;
        this.wallAuthor = wallAuthor;
        this.wallUrl = wallUrl;
        this.wallDimensions = wallDimensions;
        this.wallCopyright = wallCopyright;
    }

    public String getWallName() {
        return wallName;
    }

    public String getWallAuthor() {
        return wallAuthor;
    }

    public String getWallURL() {
        return wallUrl;
    }

    public String getWallDimensions() {
        return wallDimensions;
    }

    public String getWallCopyright() {
        return wallCopyright;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected WallpaperItem(Parcel in) {
        wallName = in.readString();
        wallAuthor = in.readString();
        wallUrl = in.readString();
        wallDimensions = in.readString();
        wallCopyright = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(wallName);
        dest.writeString(wallAuthor);
        dest.writeString(wallUrl);
        dest.writeString(wallDimensions);
        dest.writeString(wallCopyright);
    }
}
