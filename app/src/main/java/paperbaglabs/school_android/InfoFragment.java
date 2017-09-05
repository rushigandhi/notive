package paperbaglabs.school_android;


import android.content.res.Configuration;
import android.icu.util.RangeValueIterator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {



    // This page will explain more about notify and our contact information


    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        Element adsElement = new Element();
        View aboutPage = new AboutPage(getActivity())
                .isRTL(false)
              //  .setImage(R.drawable.logo) TODO: Add Logo
                .addItem(new Element().setTitle("Version 0.13"))
                .setDescription("Notive is an app that helps you stay informed of important announcements from all the organizations in your life. ")
                .addGroup("Connect with us")
                .addEmail("hello@paperbaglabs.com")
                .addWebsite("https://paperbaglabs.com/")
                .addFacebook("paperbaglabs")
                .addTwitter("paperbaglabs")
                .addPlayStore("com.darshilpatel")
                .addGitHub("paperbaglabs")
                .addItem(getCopyRightsElement())
                .create();

        return aboutPage;




    }
    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.ic_mode_comment_black_24dp);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "V.0.53 --> <3", Toast.LENGTH_LONG).show();
            }
        });
        return copyRightsElement;
    }




}
