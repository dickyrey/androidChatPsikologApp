package com.dickyrey.konsulyuk.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dickyrey.konsulyuk.Fragment.ChatsFragment;
import com.dickyrey.konsulyuk.Fragment.ContactsFragment;
import com.dickyrey.konsulyuk.Fragment.GroubFragment;
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
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case 1 :
                GroubFragment groubFragment= new GroubFragment();
                return groubFragment;
            case 2 :
                ContactsFragment contactsFragment = new ContactsFragment();
                return contactsFragment;
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
                return "Chats";
            case 1 :
                return "Grup";
            case 2 :
                return "Klien";
            case 3 :
                return "Permintaan";

            default:
                return null;
        }
    }
}
