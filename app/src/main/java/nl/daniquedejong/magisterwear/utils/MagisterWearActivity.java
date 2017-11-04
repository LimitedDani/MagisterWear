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

import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;


import net.ilexiconn.magister.ParcelableMagister;
import net.ilexiconn.magister.adapter.PresenceAdapter;
import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.container.Grade;
import net.ilexiconn.magister.container.Presence;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.container.User;

import java.util.Date;

import nl.daniquedejong.magisterwear.task.LoginTask;

/**
 * Created by bas on 7-6-16.
 */
public class MagisterWearActivity extends FragmentActivity {
    public ParcelableMagister mMagister;
    public School mSchool;
    public User mUser;
    public Appointment[] mAppointments;
    public Grade[] grades;
    public Presence[] mPresences;
    //public GradesAdapter mGradesAdapter;
    //public HomeworkAdapter mHomeworkAdapter;
    public PresenceAdapter mPresenceAdapter;

    public SwipeRefreshLayout mSwipeRefreshLayout;
    //public CoordinatorLayout coordinatorLayout;
    //public AppointmentsAdapter mAppointmentAdapter;
    public ListView listView;
    //public ErrorView errorView;
    public ProgressBar mProgressBar;

    public Date date;


    Integer type = 0;

    public void getMagister(final MagisterWearActivity activity, final School mSchool, final User mUser) {
        if (LoginUtils.reLogin(this)) {
            new LoginTask(activity, mSchool, mUser).execute();
        } else if (LoginUtils.loginError(this)) {
            Log.d("Error", "Something is wrong");
        }

    }

    public void retrieveData(final MagisterWearActivity activity) {
        if (mMagister != null) {
            switch (type) {
                case 0:
                    if (mMagister != null || mMagister.isExpired()) {
                        getMagister(activity, mSchool, mUser);
                    } else {
                        getMagister(activity, mSchool, mUser);
                    }
            }
        } else {
            getMagister(activity, mSchool, mUser);
        }

    }
}
