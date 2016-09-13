package edu.umn.paull011.evolveworkoutlogger.fragments;

import android.support.v4.app.Fragment;

import com.squareup.leakcanary.RefWatcher;

import edu.umn.paull011.evolveworkoutlogger.EvolveApplication;

/**
 * Base class for all fragments,
 * Uses LeakCanary to check for fragment leaks
 * Created by mitchell on 9/12/16.
 */
public abstract class BaseFragment extends Fragment {

    @Override public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = EvolveApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
