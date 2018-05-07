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
import com.fablwesn.education.movingmoments.models.details.DetailsProdCountry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Methods used to convert display-data to another format.
 */
public class ConverterUtils {
    private ConverterUtils() {
    }

    private final static String TEXT_COMMA_SEPARATOR = ", ";

    private final static String API_DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public static final String RELEASE_DATE_FULL = "dd.MMMM.yyyy";
    public static final String RELEASE_DATE_SHORT = "MMMM, yyyy";

    public final static int ID_COUNTRY_CONVERTER_ISO = 0;
    public final static int ID_COUNTRY_CONVERTER_FULL = 1;

    /**
     * Converts the genre ids to readable strings and connects them correctly
     * to be displayed inside one TextView.
     *
     * @param context used
     * @param genreIds a list of integer ids representing a genre on TMDb
     * @return a String with readable genres, separated correctly if there's more than one genre
     */
    public static String formatGenres(Context context, List<Integer> genreIds) {
        int genreSize = genreIds.size();
        StringBuilder movieGenre = new StringBuilder();
        if (genreIds.isEmpty()) {
            return movieGenre.toString();
        } else if (genreSize == 1) {
            return getDisplayGenreString(context, genreIds.get(0));
        } else {
            for (int i = 0; i < genreSize; i++) {
                movieGenre.append(ConverterUtils.getDisplayGenreString(context, genreIds.get(i)));
                if (i < genreSize - 1) {
                    movieGenre.append(ConverterUtils.TEXT_COMMA_SEPARATOR);
                }
            }
            return movieGenre.toString();
        }
    }

    /**
     * Converts the List of countries to a single string, either fully written or
     * in their ISO representation.
     *
     * @param prodCountries list of countries from TMDb
     * @param typeId decides if to convert into iso or fully written countries
     * @return a single String containing all countries from the List in a readable format
     */
    public static String formatProductionCountries(List<DetailsProdCountry> prodCountries, int typeId) {
        int countriesCount = prodCountries.size();
        StringBuilder formattedCountries = new StringBuilder();
        if (prodCountries.isEmpty()) {
            return formattedCountries.toString();
        } else if (countriesCount == 1) {
            switch (typeId) {
                case ID_COUNTRY_CONVERTER_ISO:
                    return prodCountries.get(0).getCountryIso();
                default:
                    return prodCountries.get(0).getCountryName();
            }
        } else {
            for (int i = 0; i < countriesCount; i++) {
                switch (typeId) {
                    case ID_COUNTRY_CONVERTER_ISO:
                        formattedCountries.append(prodCountries.get(i).getCountryIso());
                        break;
                    default:
                        formattedCountries.append(prodCountries.get(i).getCountryName());
                        break;
                }

                if (i < countriesCount - 1) {
                    formattedCountries.append(ConverterUtils.TEXT_COMMA_SEPARATOR);
                }
            }
            return formattedCountries.toString();
        }
    }

    /**
     * Change a passed date from the existing format to the newly specified one.
     *
     * @param requiredFormat the format we want to have
     * @param dateString     the date we want to change
     * @return our date in the desired format
     * @throws ParseException when changing the format
     */
    public static String changeDateFormat(String requiredFormat, String dateString) throws ParseException {
        String result = "";

        SimpleDateFormat formatterOld = new SimpleDateFormat(API_DEFAULT_DATE_FORMAT, Locale.getDefault());
        SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.getDefault());

        Date date;

        date = formatterOld.parse(dateString);

        if (date != null) {
            result = formatterNew.format(date);
        }

        return result;
    }

    /**
     * Convert the genre id to the corresponding readable string
     * to display to the user.
     *
     * @param context used
     * @param genreId to convert
     * @return genre as readable string of the passed id
     */
    private static String getDisplayGenreString(Context context, int genreId) {
        String genreString;
        if (genreId == context.getResources().getInteger(R.integer.genre_id_tmdb_adventure)) {
            genreString = context.getString(R.string.title_category_adven);
        } else if (genreId == context.getResources().getInteger(R.integer.genre_id_tmdb_animation)) {
            genreString = context.getString(R.string.title_category_anim);
        } else if (genreId == context.getResources().getInteger(R.integer.genre_id_tmdb_comedy)) {
            genreString = context.getString(R.string.title_category_comedy);
        } else if (genreId == context.getResources().getInteger(R.integer.genre_id_tmdb_crime)) {
            genreString = context.getString(R.string.title_category_crime);
        } else if (genreId == context.getResources().getInteger(R.integer.genre_id_tmdb_docu)) {
            genreString = context.getString(R.string.title_category_docu);
        } else if (genreId == context.getResources().getInteger(R.integer.genre_id_tmdb_drama)) {
            genreString = context.getString(R.string.title_category_drama);
        } else if (genreId == context.getResources().getInteger(R.integer.genre_id_tmdb_family)) {
            genreString = context.getString(R.string.title_category_family);
        } else if (genreId == context.getResources().getInteger(R.integer.genre_id_tmdb_fantasy)) {
            genreString = context.getString(R.string.title_category_fantasy);
        } else if (genreId == context.getResources().getInteger(R.integer.genre_id_tmdb_history)) {
            genreString = context.getString(R.string.title_category_hist);
        } else if (genreId == context.getResources().getInteger(R.integer.genre_id_tmdb_horror)) {
            genreString = context.getString(R.string.title_category_horror);
        } else if (genreId == context.getResources().getInteger(R.integer.genre_id_tmdb_music)) {
            genreString = context.getString(R.string.title_category_music);
        } else if (genreId == context.getResources().getInteger(R.integer.genre_id_tmdb_myst)) {
            genreString = context.getString(R.string.title_category_myst);
        } else if (genreId == context.getResources().getInteger(R.integer.genre_id_tmdb_romance)) {
            genreString = context.getString(R.string.title_category_romance);
        } else if (genreId == context.getResources().getInteger(R.integer.genre_id_tmdb_sf)) {
            genreString = context.getString(R.string.title_category_sf);
        } else if (genreId == context.getResources().getInteger(R.integer.genre_id_tmdb_tv)) {
            genreString = context.getString(R.string.title_category_tv);
        } else if (genreId == context.getResources().getInteger(R.integer.genre_id_tmdb_thriller)) {
            genreString = context.getString(R.string.title_category_thriller);
        } else if (genreId == context.getResources().getInteger(R.integer.genre_id_tmdb_war)) {
            genreString = context.getString(R.string.title_category_war);
        } else if (genreId == context.getResources().getInteger(R.integer.genre_id_tmdb_western)) {
            genreString = context.getString(R.string.title_category_western);
        } else {
            genreString = context.getString(R.string.title_category_action);
        }

        return genreString;
    }
}
