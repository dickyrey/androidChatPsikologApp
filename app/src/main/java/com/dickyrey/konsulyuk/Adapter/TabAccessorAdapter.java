package com.dickyrey.konsulyuk.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dickyrey.konsulyuk.Fragment.ChatsFragment;
import com.dickyrey.konsulyuk.Fragment.ContactsFragment;
import com.dickyrey.konsulyuk.Fragment.GroubFragment;
import com.dickyrey.konsulyuk.Fragment.MenuFragment;
import com.dickyrey.konsulyuk.Fragment.RequestsFragment;


public class TabAccessorAdapter extends FragmentPagerAdapter {
    public TabAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0 :
                MenuFragment menuFragment = new MenuFragment();
                return menuFragment;
            case 1 :
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case 2 :
                GroubFragment groubFragment= new GroubFragment();
                return groubFragment;
//            case 3 :
//                ContactsFragment contactsFragment = new ContactsFragment();
//                return contactsFragment;
            case 3 :
                RequestsFragment requestsFragment = new RequestsFragment();
                return requestsFragment;

             default:
                 return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0 :
                return "Menu";
            case 1 :
                return "Chats";
            case 2 :
                return "Grup";
//            case 3 :
//                return "Klien";
            case 3 :
                return "Permintaan";

            default:
                return null;
        }
    }
}
