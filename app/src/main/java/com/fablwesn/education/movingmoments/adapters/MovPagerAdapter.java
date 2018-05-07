/*
 * Copyright (C) 2018 Darijo Barucic, Seventoes
 *
 *  Licensed under the MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.fablwesn.education.movingmoments.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fablwesn.education.movingmoments.fragments.MovPagerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the viewpager using {@link FragmentPagerAdapter}
 * inside {@link com.fablwesn.education.movingmoments.HomeActivity} fragments.
 */
public class MovPagerAdapter extends FragmentPagerAdapter {

     /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       Fundamentals */

     private final static int PAGE_SIZE = 2;

    // Contains the page titles for the tab layout.
    private List<String> fragmentTitleList = new ArrayList<>();

    public MovPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    /**
     * Create and add the fragment with the passed title for the tab-layout.
     *
     * @param title of the fragment.
     */
    public void addFragment(String title) {
        fragmentTitleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return MovPagerFragment.newInstance(fragmentTitleList.get(position));
    }

    @Override
    public int getCount() {
        return PAGE_SIZE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Set the tab layout's title text.
        return fragmentTitleList.get(position);
    }

    /*____________________________________________________________________________________________*/
}
