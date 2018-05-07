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

package com.fablwesn.education.movingmoments.utility;

import android.content.Context;

import com.fablwesn.education.movingmoments.R;

public class PrefUtils {
    private PrefUtils() {
    }

    // Time intervals for the backdrop pager timer inside the details activity.
    public final static int BACKDROP_PAGER_INIT_TIME = 4000;
    public final static int BACKDROP_PAGER_PERIOD_TIME = 6000;

    /**
     * Returns the id of the nav drawer to be checked, corresponding to the default fragment ot load
     *
     * @param context        of the activity
     * @param sectionChecked default fragment nav item
     * @return nav drawer id to check on startup
     */
    public static int setNavDefaultSection(Context context, String sectionChecked) {
        if (sectionChecked.equals(context.getString(R.string.act_label_home_fav)))
            return R.id.nav_favorites;
        else return R.id.nav_movies;
    }
}
