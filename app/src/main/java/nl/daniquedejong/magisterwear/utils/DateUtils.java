/*
 * Copyright 2016 Bas van den Boom 'Z3r0byte'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.daniquedejong.magisterwear.utils;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by bas on 24-5-16.
 */
public class DateUtils {

    private static final String TAG = "DateUtils";

    public static String formatDate(Date date, String format) {
        format = format.replace("YYYY", "yyyy");
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static Date parseDate(String date, String format) {
        format = format.replace("YYYY", "yyyy");
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
        Date error = new Date(2000, 12, 31);
        return error;
    }

    public static Date getToday() {
        Calendar calendar = Calendar.getInstance();
        return new Date(calendar.getTimeInMillis());
    }

    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return new Date(calendar.getTimeInMillis());
    }

    public static Date addHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hours);
        return new Date(calendar.getTimeInMillis());
    }

    public static Date addMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return new Date(calendar.getTimeInMillis());
    }

    public static Date fixTimeDifference(Date date, Boolean reverse, Context context) {
        ConfigUtil configUtil = new ConfigUtil(context);
        int difference = configUtil.getInteger("timezoneFix");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (reverse) {
            calendar.add(Calendar.SECOND, -difference);
        } else {
            calendar.add(Calendar.SECOND, difference);
        }
        return new Date(calendar.getTimeInMillis());
    }

    public static void lastLogin(Context context, String date) {
        context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().putString("LastLogin", date).apply();
    }
}
