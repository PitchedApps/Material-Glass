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

package com.pitchedapps.material.glass.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.dialogs.ISDialogs;
import com.pitchedapps.material.glass.utilities.ThemeUtils;
import com.pitchedapps.material.glass.utilities.Utils;

public class CreditsFragment extends Fragment {

    private Context context;
    private ViewGroup layout;
    Drawable person, facebook, gplus, twitter, website, youtube, community, playstore, github,
            bugs, collaboratorsIcon, libs, uiCollaboratorsIcon, sherryIcon, email, translators;
    ImageView iconAuthor, iconDev, iconAuthorFacebook, iconAuthorGPlus, iconAuthorCommunity,
            youtubeIcon, twitterIcon, playStoreIcon, iconAuthorWebsite, uiCollaboratorsIV,
            iconDevGitHub, iconDevCommunity, bugIcon, collaboratorsIV, libsIcon,
            sherryIV, emailIV, translatorsIV;
    LinearLayout jahirL, authorFB, authorGPlus, authorTwitter, authorWebsite, authorYouTube,
            authorCommunity, authorPlayStore, devGitHub, libraries, uiCollaborators,
            thanksSherry, contributorsLayout, bugsL, communityL, emailL, translatorsL;
    boolean withLinkToFacebook = false,
            withLinkToTwitter = false,
            withLinkToGPlus = false,
            withLinkToYouTube = false,
            withLinkToCommunity = false,
            withLinkToPlayStore = false,
            withLinkToWebsite = false;
    String[] libsLinks, contributorsLinks, uiCollaboratorsLinks;

    private void setupBooleans(Context context) {
        Resources res = context.getResources();
        String[] sites = res.getStringArray(R.array.iconpack_author_sites);

        for (String site : sites) {
            if (site.equals(res.getString(R.string.facebook))) {
                withLinkToFacebook = true;
            } else if (site.equals(res.getString(R.string.google_plus))) {
                withLinkToGPlus = true;
            } else if (site.equals(res.getString(R.string.join_community))) {
                withLinkToCommunity = true;
            } else if (site.equals(res.getString(R.string.youtube))) {
                withLinkToYouTube = true;
            } else if (site.equals(res.getString(R.string.twitter))) {
                withLinkToTwitter = true;
            } else if (site.equals(res.getString(R.string.play_store))) {
                withLinkToPlayStore = true;
            }
        }

        withLinkToWebsite = res.getBoolean(R.bool.you_have_a_website);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        setupBooleans(context);

        libsLinks = context.getResources().getStringArray(R.array.libs_links);
        contributorsLinks = context.getResources().getStringArray(R.array.contributors_links);
        uiCollaboratorsLinks = context.getResources().getStringArray(R.array.ui_collaborators_links);

        if (layout != null) {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }

        layout = (ViewGroup) inflater.inflate(R.layout.credits_section, container, false);

        setupViewsIDs(layout);
        setupLayout(getActivity());
        setupExtraAuthorOptions();

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.collapseToolbar(getActivity());
    }

    private void setupViewsIDs(final ViewGroup layout) {
        iconAuthor = (ImageView) layout.findViewById(R.id.icon_author);
        iconDev = (ImageView) layout.findViewById(R.id.icon_dev);
        iconAuthorFacebook = (ImageView) layout.findViewById(R.id.icon_facebook_author);
        iconAuthorGPlus = (ImageView) layout.findViewById(R.id.icon_google_plus_author);
        iconAuthorCommunity = (ImageView) layout.findViewById(R.id.icon_community_author);
        youtubeIcon = (ImageView) layout.findViewById(R.id.icon_youtube);
        twitterIcon = (ImageView) layout.findViewById(R.id.icon_twitter);
        playStoreIcon = (ImageView) layout.findViewById(R.id.icon_play_store);
        iconAuthorWebsite = (ImageView) layout.findViewById(R.id.icon_website_author);
        iconDevGitHub = (ImageView) layout.findViewById(R.id.icon_github);
        bugIcon = (ImageView) layout.findViewById(R.id.icon_bug_report);
        libsIcon = (ImageView) layout.findViewById(R.id.icon_libs);
        collaboratorsIV = (ImageView) layout.findViewById(R.id.icon_collaborators);
        sherryIV = (ImageView) layout.findViewById(R.id.icon_sherry);
        uiCollaboratorsIV = (ImageView) layout.findViewById(R.id.icon_ui_design);
        translatorsIV = (ImageView) layout.findViewById(R.id.icon_translators);
        iconDevCommunity = (ImageView) layout.findViewById(R.id.icon_google_plus_community);
        emailIV = (ImageView) layout.findViewById(R.id.icon_email);

        emailL = (LinearLayout) layout.findViewById(R.id.send_email);
        emailL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendEmailWithDeviceInfo(context);
            }
        });

        jahirL = (LinearLayout) layout.findViewById(R.id.devName);
        jahirL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLinkInChromeCustomTab(context,
                        Utils.getStringFromResources(context, R.string.dashboard_author_website));
            }
        });

        authorFB = (LinearLayout) layout.findViewById(R.id.author_facebook);
        authorFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isAppInstalled(context, "com.facebook.katana")) {
                    Utils.openLink(context,
                            getResources().getString(R.string.iconpack_author_fb));
                } else {
                    Utils.openLinkInChromeCustomTab(context,
                            getResources().getString(R.string.iconpack_author_fb_alt));
                }
            }
        });

        authorGPlus = (LinearLayout) layout.findViewById(R.id.add_to_google_plus_circles);
        authorGPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLinkInChromeCustomTab(context,
                        getResources().getString(R.string.iconpack_author_gplus));
            }
        });

        authorTwitter = (LinearLayout) layout.findViewById(R.id.twitter);
        authorTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Utils.openLink(context,
                            getResources().getString(R.string.iconpack_author_twitter));
                } catch (Exception e) {
                    Utils.openLink(context,
                            getResources().getString(R.string.iconpack_author_twitter_alt));
                }
            }
        });

        authorWebsite = (LinearLayout) layout.findViewById(R.id.visit_website);
        authorWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLinkInChromeCustomTab(context,
                        getResources().getString(R.string.iconpack_author_website));
            }
        });

        authorYouTube = (LinearLayout) layout.findViewById(R.id.youtube);
        authorYouTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLinkInChromeCustomTab(context,
                        getResources().getString(R.string.iconpack_author_youtube));
            }
        });

        authorCommunity = (LinearLayout) layout.findViewById(R.id.community);
        authorCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLinkInChromeCustomTab(context,
                        getResources().getString(R.string.iconpack_author_gplus_community));
            }
        });

        authorPlayStore = (LinearLayout) layout.findViewById(R.id.play_store);
        authorPlayStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLinkInChromeCustomTab(context,
                        getResources().getString(R.string.iconpack_author_playstore));
            }
        });

        devGitHub = (LinearLayout) layout.findViewById(R.id.dev_github);
        devGitHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLinkInChromeCustomTab(context,
                        getResources().getString(R.string.dashboard_author_github));
            }
        });

        thanksSherry = (LinearLayout) layout.findViewById(R.id.collaboratorsSherry);
        thanksSherry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISDialogs.showSherryDialog(context);
            }
        });

        uiCollaborators = (LinearLayout) layout.findViewById(R.id.uiDesign);
        uiCollaborators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISDialogs.showUICollaboratorsDialog(context, uiCollaboratorsLinks);
            }
        });

        libraries = (LinearLayout) layout.findViewById(R.id.libraries);
        libraries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISDialogs.showLibrariesDialog(context, libsLinks);
            }
        });

        contributorsLayout = (LinearLayout) layout.findViewById(R.id.collaborators);
        contributorsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISDialogs.showContributorsDialog(context, contributorsLinks);
            }
        });

        translatorsL = (LinearLayout) layout.findViewById(R.id.translators);
        translatorsL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISDialogs.showTranslatorsDialogs(context);
            }
        });

        bugsL = (LinearLayout) layout.findViewById(R.id.report_bugs);
        bugsL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLinkInChromeCustomTab(context,
                        getResources().getString(R.string.dashboard_bugs_report));
            }
        });

        communityL = (LinearLayout) layout.findViewById(R.id.join_google_plus_community);
        communityL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLinkInChromeCustomTab(context,
                        getResources().getString(R.string.dashboard_author_gplus_community));
            }
        });

    }

    private void setupLayout(Context context) {
        int light = ContextCompat.getColor(context, R.color.drawable_tint_dark);
        int dark = ContextCompat.getColor(context, R.color.drawable_tint_light);

        person = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_account)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        facebook = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_facebook)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        gplus = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_google_plus)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        community = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_group_work)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        twitter = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_twitter)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        github = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_github)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        youtube = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_youtube_play)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        playstore = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_case_play)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        website = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_globe_alt)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        bugs = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_bug)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        collaboratorsIcon = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_code)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        libs = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_file_text)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        sherryIcon = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_star)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        uiCollaboratorsIcon = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_palette)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        translators = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_translate)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        email = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_email)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        iconAuthor.setImageDrawable(person);
        iconDev.setImageDrawable(person);
        iconAuthorGPlus.setImageDrawable(gplus);
        iconAuthorCommunity.setImageDrawable(community);
        youtubeIcon.setImageDrawable(youtube);
        twitterIcon.setImageDrawable(twitter);
        playStoreIcon.setImageDrawable(playstore);
        iconAuthorWebsite.setImageDrawable(website);
        iconDevGitHub.setImageDrawable(github);
        bugIcon.setImageDrawable(bugs);
        iconAuthorFacebook.setImageDrawable(facebook);
        libsIcon.setImageDrawable(libs);
        collaboratorsIV.setImageDrawable(collaboratorsIcon);
        sherryIV.setImageDrawable(sherryIcon);
        uiCollaboratorsIV.setImageDrawable(uiCollaboratorsIcon);
        translatorsIV.setImageDrawable(translators);
        iconDevCommunity.setImageDrawable(community);
        emailIV.setImageDrawable(email);

    }

    private void setupExtraAuthorOptions() {
        authorFB.setVisibility(withLinkToFacebook ? View.VISIBLE : View.GONE);
        authorTwitter.setVisibility(withLinkToTwitter ? View.VISIBLE : View.GONE);
        authorGPlus.setVisibility(withLinkToGPlus ? View.VISIBLE : View.GONE);
        authorYouTube.setVisibility(withLinkToYouTube ? View.VISIBLE : View.GONE);
        authorCommunity.setVisibility(withLinkToCommunity ? View.VISIBLE : View.GONE);
        authorPlayStore.setVisibility(withLinkToPlayStore ? View.VISIBLE : View.GONE);
        authorWebsite.setVisibility(withLinkToWebsite ? View.VISIBLE : View.GONE);
    }

}