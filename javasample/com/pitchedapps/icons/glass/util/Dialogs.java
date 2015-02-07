package com.pitchedapps.icons.glass.util;

import static com.pitchedapps.icons.glass.adapter.CreditsPeopleAdapter.TYPE_MAIN;
import static com.pitchedapps.icons.glass.adapter.CreditsPeopleAdapter.TYPE_TESTER;
import static com.pitchedapps.icons.glass.util.Utils.getApacheLicense;
import static com.pitchedapps.icons.glass.util.Utils.getMITLicense;
import static com.pitchedapps.icons.glass.util.Utils.isPackageInstalled;
import static com.pitchedapps.icons.glass.util.Utils.resToUri;
import static com.pkmmte.applylauncher.PkApplyLauncher.ACTION;
import static com.pkmmte.applylauncher.PkApplyLauncher.ADW;
import static com.pkmmte.applylauncher.PkApplyLauncher.APEX;
import static com.pkmmte.applylauncher.PkApplyLauncher.ATOM;
import static com.pkmmte.applylauncher.PkApplyLauncher.AVIATE;
import static com.pkmmte.applylauncher.PkApplyLauncher.GO;
import static com.pkmmte.applylauncher.PkApplyLauncher.HOLO;
import static com.pkmmte.applylauncher.PkApplyLauncher.INSPIRE;
import static com.pkmmte.applylauncher.PkApplyLauncher.NEXT;
import static com.pkmmte.applylauncher.PkApplyLauncher.NOVA;
import static com.pkmmte.applylauncher.PkApplyLauncher.SMART;

import android.graphics.drawable.ColorDrawable;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.pitchedapps.icons.glass.MainActivity;
import com.pitchedapps.icons.glass.R;
import com.pitchedapps.icons.glass.adapter.CreditsLibraryAdapter;
import com.pitchedapps.icons.glass.adapter.CreditsLibraryAdapter.onAvatarClickListener;
import com.pitchedapps.icons.glass.adapter.CreditsPeopleAdapter;
import com.pitchedapps.icons.glass.adapter.LauncherAdapter;
import com.pitchedapps.icons.glass.model.CreditsLibraryItem;
import com.pitchedapps.icons.glass.model.CreditsPeopleItem;
import com.pkmmte.applylauncher.Launcher;
import com.pkmmte.applylauncher.PkApplyLauncher;

public class Dialogs
{
	/**
	 * Returns a dialog containing a GridView 
	 * of launchers and a close button.
	 * 
	 * @param context
	 * @return
	 */
	public static Dialog getLaunchersDialog(final Context context)
	{
		// Create Dialog Base
		final Dialog mDialog = new Dialog(context);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.dialog_launchers);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.setCancelable(true);
		
		// Declare and initialize views
		GridView mGrid = (GridView) mDialog.findViewById(R.id.launcherGrid);
		Button btnCancel = (Button) mDialog.findViewById(R.id.btnCancel);
		
		// Load up the list of launchers
		final List<Launcher> mLaunchers = new ArrayList<Launcher>();
		mLaunchers.add(PkApplyLauncher.launcherAction);
		mLaunchers.add(PkApplyLauncher.launcherAdw);
		mLaunchers.add(PkApplyLauncher.launcherApex);
		mLaunchers.add(PkApplyLauncher.launcherAtom);
		mLaunchers.add(PkApplyLauncher.launcherAviate);
		mLaunchers.add(PkApplyLauncher.launcherGo);
		mLaunchers.add(PkApplyLauncher.launcherHolo);
		mLaunchers.add(PkApplyLauncher.launcherInspire);
		mLaunchers.add(PkApplyLauncher.launcherNext);
		mLaunchers.add(PkApplyLauncher.launcherNova);
		mLaunchers.add(PkApplyLauncher.launcherSmart);
		
		// Add your own custom properties
		mLaunchers.get(ACTION)
			.putExtra(LauncherAdapter.ICON_KEY, R.mipmap.banner_al)
			.putExtra(LauncherAdapter.INSTALLED_KEY, isPackageInstalled(mLaunchers.get(ACTION).getPackage(), context));
		mLaunchers.get(ADW)
			.putExtra(LauncherAdapter.ICON_KEY, R.mipmap.banner_adw)
			.putExtra(LauncherAdapter.INSTALLED_KEY, isPackageInstalled(mLaunchers.get(ADW).getPackage(), context));
		mLaunchers.get(APEX)
			.putExtra(LauncherAdapter.ICON_KEY, R.mipmap.banner_apex)
			.putExtra(LauncherAdapter.INSTALLED_KEY, isPackageInstalled(mLaunchers.get(APEX).getPackage(), context));
		mLaunchers.get(ATOM)
			.putExtra(LauncherAdapter.ICON_KEY, R.mipmap.banner_atom)
	        .putExtra(LauncherAdapter.INSTALLED_KEY, isPackageInstalled(mLaunchers.get(ATOM).getPackage(), context));
		mLaunchers.get(AVIATE)
			.putExtra(LauncherAdapter.ICON_KEY, R.mipmap.banner_aviate)
			.putExtra(LauncherAdapter.INSTALLED_KEY, isPackageInstalled(mLaunchers.get(AVIATE).getPackage(), context));
		mLaunchers.get(GO)
			.putExtra(LauncherAdapter.ICON_KEY, R.mipmap.banner_go)
	        .putExtra(LauncherAdapter.INSTALLED_KEY, isPackageInstalled(mLaunchers.get(GO).getPackage(), context));
		mLaunchers.get(HOLO)
			.putExtra(LauncherAdapter.ICON_KEY, R.mipmap.banner_holo)
			.putExtra(LauncherAdapter.INSTALLED_KEY, isPackageInstalled(mLaunchers.get(HOLO).getPackage(), context));
		mLaunchers.get(INSPIRE)
			.putExtra(LauncherAdapter.ICON_KEY, R.mipmap.banner_inspire)
			.putExtra(LauncherAdapter.INSTALLED_KEY, isPackageInstalled(mLaunchers.get(INSPIRE).getPackage(), context));
		mLaunchers.get(NEXT)
			.putExtra(LauncherAdapter.ICON_KEY, R.mipmap.banner_next)
		    .putExtra(LauncherAdapter.INSTALLED_KEY, isPackageInstalled(mLaunchers.get(NEXT).getPackage(), context));
		mLaunchers.get(NOVA)
			.putExtra(LauncherAdapter.ICON_KEY, R.mipmap.banner_nova)
			.putExtra(LauncherAdapter.INSTALLED_KEY, isPackageInstalled(mLaunchers.get(NOVA).getPackage(), context));
		mLaunchers.get(SMART)
			.putExtra(LauncherAdapter.ICON_KEY, R.mipmap.banner_smart)
			.putExtra(LauncherAdapter.INSTALLED_KEY, isPackageInstalled(mLaunchers.get(SMART).getPackage(), context));
		
		// Create adapter and set it to the list
		LauncherAdapter mAdapter = new LauncherAdapter(context, mLaunchers);
		mGrid.setAdapter(mAdapter);
		
		// onClick listeners
		mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				// Get the selected launcher...
				Launcher selectedLauncher = mLaunchers.get(position);
				
				// Apply the launcher...
				boolean result = PkApplyLauncher.applyLauncher(selectedLauncher, (MainActivity) context, context.getString(R.string.package_name));
				
				// Launch play store and show toast if not found
				if(!result) {
					PkApplyLauncher.launchPlayStore(selectedLauncher, (MainActivity) context);
					Toast.makeText(context, selectedLauncher.getName() + context.getString(R.string.launcher_not_installed), Toast.LENGTH_SHORT).show();
				}
			}
		});
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		
		// Return the dialog object
		return mDialog;
	}
	
	public static Dialog getAboutDialog(Context context)
	{
		// Create dialog base
		final Dialog mDialog = new Dialog(context);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.dialog_about);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.setCancelable(true);
		
		// Return the dialog object
		return mDialog;
	}
	
	public static Dialog getCreditsPeopleDialog(Context context)
	{
		// Create & configure ListView
		ListView mList = new ListView(context);
		mList.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mList.setSelector(new ColorDrawable(context.getResources().getColor(R.color.transparent)));
		mList.setClickable(true);
		mList.setDivider(null);
		mList.setDividerHeight(0);
		mList.setHorizontalScrollBarEnabled(false);
		mList.setVerticalScrollBarEnabled(false);
		mList.setPadding(0, (int) Utils.convertDpToPixel(24, context), 0, (int) Utils.convertDpToPixel(24, context));
		mList.setClipToPadding(false);
		
		// Create dialog base
		final Dialog mDialog = new Dialog(context, R.style.the1template_TransparentDialog);
		mDialog.setContentView(mList);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.setCancelable(true);
		
		final CreditsPeopleAdapter mAdapter = new CreditsPeopleAdapter(context);
		mAdapter.addItem(new CreditsPeopleItem.Builder()
			.banner(resToUri(context, R.drawable.credits_the1dynasty_banner))
			.name("the1dynasty")
			.tagline("Main Developer")
			.description("I started this ish and.......")
			.type(TYPE_MAIN)
			.build());
		mAdapter.addItem(new CreditsPeopleItem.Builder()
			.avatar(resToUri(context, R.drawable.credits_pkmmte_xeleon))
			//.banner(resToUri(context, R.drawable.credits_phlash_banner))
			.name("Pkmmte Xeleon")
			.tagline("Main Developer")
			.description(getApacheLicense("Copyright 2013 readyState Software Limited\n\n"))
			.type(TYPE_MAIN)
			.build());
        //TODO Add info
        mAdapter.addItem(new CreditsPeopleItem.Builder()
			.avatar(resToUri(context, R.drawable.credits_ivonliu))
			.name("Ivon Liu")
			.tagline("Side Developer")
			.description("Technology enthusiast who enjoys programming as a hobby. Writes many Android apps when bored. Check them out on the Play Store at http://goo.gl/0NE9OG")
			.type(TYPE_MAIN)
			.build());
		mAdapter.addItem(new CreditsPeopleItem.Builder()
			.avatar(resToUri(context, R.drawable.credits_phlash))
			.banner(resToUri(context, R.drawable.credits_phlash_banner))
			.name("Tha PHLASH")
			.tagline("Icon Legend")
			.description("Icon master that creates awesomeness")
			.type(TYPE_TESTER)
			.build());
		mAdapter.addItem(new CreditsPeopleItem.Builder()
			.avatar(resToUri(context, R.drawable.credits_bobby))
			//.banner(resToUri(context, R.drawable.credits_bobby_banner))
			.name("Bobby McKenzie")
			.tagline("Designer & Tester")
			.description(getApacheLicense())
			.type(TYPE_TESTER)
			.build());
		mAdapter.addItem(new CreditsPeopleItem.Builder()
			.avatar(resToUri(context, R.drawable.credits_inder))
			.banner(resToUri(context, R.drawable.credits_inder_banner))
			.name("Inder Deep")
			.tagline("Designer & Tester")
			.description("Inder came up with the design for the alternate home fragment, provided the 2 default wallpapers for the template, and helped throughly test it all. BladeXDesigns create some of the most unique and beautiful icon sets.")
			.type(TYPE_TESTER)
			.build());
		mAdapter.addItem(new CreditsPeopleItem.Builder()
			.avatar(resToUri(context, R.drawable.credits_alim))
			.banner(resToUri(context, R.drawable.credits_alim_banner))
			.name("Alim Haque")
			.tagline("Ghetto Tester")
			.description("Dis dawg makes sure dat dis template functions like the bawses Pk and the1d wanted it to, designs more icons dan he would ever need in his lifetime (hey, photoshop is fun), 'n' he raps like a 21st century monk #Haquer #RealTalk")
			.type(TYPE_TESTER)
			.build());
		mAdapter.addItem(new CreditsPeopleItem.Builder()
			.avatar(resToUri(context, R.drawable.credits_andrew))
			//.banner(resToUri(context, R.drawable.credits_andrew_banner))
			.name("Andrew Ruffolo")
			.tagline("The Dude")
			.description("This guy contributed early and hasn't been seen for months. Every now and then he emerges from the shadows to wish someone a happy birthday and then crawls back into his cave where it's been said he makes YouTube videos.")
			.type(TYPE_TESTER)
			.build());
		mAdapter.addItem(new CreditsPeopleItem.Builder()
			.avatar(resToUri(context, R.drawable.credits_nitish))
			//.banner(resToUri(context, R.drawable.credits_nitish_banner))
			.name("Nitish Saxena")
			.tagline("Tester")
			.description(getApacheLicense())
			.type(TYPE_TESTER)
			.build());
		
		mList.setAdapter(mAdapter);
		
		// Return the dialog object
		return mDialog;
	}
	
	public static Dialog getCreditsLibraryDialog(final Context context)
	{
		// Create & configure ListView
		ListView mList = new ListView(context);
		mList.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mList.setSelector(R.color.transparent);
		mList.setClickable(true);
		mList.setDivider(null);
		mList.setDividerHeight(0);
		mList.setHorizontalScrollBarEnabled(false);
		mList.setVerticalScrollBarEnabled(false);
		mList.setPadding(0, (int) Utils.convertDpToPixel(24, context), 0, (int) Utils.convertDpToPixel(24, context));
		mList.setClipToPadding(false);
		
		// Create dialog base
		final Dialog mDialog = new Dialog(context, R.style.the1template_TransparentDialog);
		mDialog.setContentView(mList);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.setCancelable(true);
		
		// Add items
		final CreditsLibraryAdapter mAdapter = new CreditsLibraryAdapter(context);
		mAdapter.addItem(new CreditsLibraryItem.Builder()
			.avatar(resToUri(context, R.drawable.credits_jgilfelt))
			.link(Uri.parse("https://github.com/jgilfelt/SystemBarTint"))
			.title("SystemBarTint")
			.author("Jeff Gilfelt")
			.license(getApacheLicense("Copyright 2013 readyState Software Limited\n\n"))
			.build());
		mAdapter.addItem(new CreditsLibraryItem.Builder()
			.avatar(resToUri(context, R.drawable.credits_romannurik))
			.link(Uri.parse("https://github.com/jgilfelt/SystemBarTint"))
			.title("Muzei API")
			.author("Roman Nurik")
			.license(getApacheLicense("Copyright 2014 Google Inc.\n\n"))
			.build());
		mAdapter.addItem(new CreditsLibraryItem.Builder()
			.avatar(resToUri(context, R.drawable.credits_square))
			.link(Uri.parse("https://github.com/square/okhttp"))
			.title("OkHttp")
			.author("Square")
			.license(getApacheLicense())
			.build());
		mAdapter.addItem(new CreditsLibraryItem.Builder()
			.avatar(resToUri(context, R.drawable.credits_square))
			.link(Uri.parse("https://github.com/square/okio"))
			.title("Okio")
			.author("Square")
			.license(getApacheLicense())
			.build());
		mAdapter.addItem(new CreditsLibraryItem.Builder()
			.avatar(resToUri(context, R.drawable.credits_chrisbanes))
			.link(Uri.parse("https://github.com/chrisbanes/PhotoView"))
			.title("PhotoView")
			.author("Chris Banes")
			.license(getApacheLicense("Copyright 2011, 2012 Chris Banes\n\n"))
			.build());
		mAdapter.addItem(new CreditsLibraryItem.Builder()
			.avatar(resToUri(context, R.drawable.credits_square))
			.link(Uri.parse("https://github.com/square/picasso"))
			.title("Picasso")
			.author("Square")
			.license(getApacheLicense("Copyright 2013 Square, Inc.\n\n"))
			.build());
		mAdapter.addItem(new CreditsLibraryItem.Builder()
			.avatar(resToUri(context, R.drawable.credits_pkmmte))
			.link(Uri.parse("https://github.com/Pkmmte/PkApplyLauncher"))
			.title("PkApplyLauncher")
			.author("Pkmmte")
			.license(getMITLicense("Copyright (c) 2014 Pkmmte Xeleon\n\n"))
			.build());
		mAdapter.addItem(new CreditsLibraryItem.Builder()
			.avatar(resToUri(context, R.drawable.credits_pkmmte))
			.link(Uri.parse("https://github.com/Pkmmte/PkRequestManager"))
			.title("PkRequestManager")
			.author("Pkmmte")
			.license(getMITLicense("Copyright (c) 2014 Pkmmte Xeleon\n\n"))
			.build());
		mAdapter.addItem(new CreditsLibraryItem.Builder()
			.avatar(resToUri(context, R.drawable.credits_pkmmte))
			.link(Uri.parse("https://github.com/Pkmmte/PkWallpaperManager"))
			.title("PkWallpaperManager")
			.author("Pkmmte")
			.license(getApacheLicense("Copyright 2014 Pkmmte Xeleon.\n\n"))
			.build());
        mAdapter.addItem(new CreditsLibraryItem.Builder()
			.avatar(resToUri(context, R.drawable.credits_dschuermann))
			.link(Uri.parse("https://github.com/dschuermann/android-donations-lib"))
			.title("Donations Lib")
			.author("Dschuermann")
			.license(getApacheLicense("Copyright 2004 dschuermann.\n\n"))
			.build());
		
		
		mList.setAdapter(mAdapter);
		
		mAdapter.setOnAvatarClickListener(new onAvatarClickListener() {
			@Override
			public void onClick(Uri link) {
				context.startActivity(new Intent(Intent.ACTION_VIEW).setData(link));
			}
		});
		
		// Return the dialog object
		return mDialog;
	}
	
	/**
	 * Returns the old changelog dialog.
	 * 
	 * @param context
	 * @return
	 */
	public static Dialog getOldChangelog(Context context)
	{
		// Create dialog base
		final Dialog CDialog = new Dialog(context);
	 	CDialog.setTitle(context.getResources().getString(R.string.changelog_title));
	 	CDialog.setContentView(R.layout.changelog);
	 	CDialog.setCanceledOnTouchOutside(true);
	 	CDialog.setCancelable(true);
	 	 
	 	// Close button
	 	Button Close = (Button) CDialog.findViewById(R.id.btnClose);
	 	Close.setOnClickListener(new View.OnClickListener() {
	 		@Override
	 		public void onClick(View v) {
	 			CDialog.dismiss();
	 		}
	 	});
	 	 
	 	return CDialog;
	}
	
	/**
	 * Returns a changelog dialog.
	 * 
	 * @param context
	 * @return
	 */
	public static Dialog getChangelog(Context context)
	{
		// Create dialog base
		final Dialog mDialog = new Dialog(context);
	 	mDialog.setTitle(context.getResources().getString(R.string.changelog_title));
	 	mDialog.setContentView(R.layout.dialog_list);
	 	mDialog.setCanceledOnTouchOutside(true);
	 	mDialog.setCancelable(true);
	 	 
	 	// Close button
	 	Button Close = (Button) mDialog.findViewById(R.id.btnClose);
	 	Close.setOnClickListener(new View.OnClickListener() {
	 		@Override
	 		public void onClick(View v) {
	 			mDialog.dismiss();
	 		}
	 	});
	 	
	 	ListView list = (ListView) mDialog.findViewById(R.id.mList);
	 	 
	 	return mDialog;
	}
}