package com.android.baseline.framework.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
/**
 * 保存Fragment状态
 * 参考：Android中保存和恢复Fragment状态的最好方法[http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0327/2648.html]
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2015-4-1]
 */
public class BaseFragment extends Fragment
{
    Bundle savedState;

    public BaseFragment()
    {
        super();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        // Restore State Here
        if (!restoreStateFromArguments())
        {
            // First Time, Initialize something here
            onFirstTimeLaunched();
        }
    }

    protected void onFirstTimeLaunched()
    {

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        // Save State Here
        saveStateToArguments();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        // Save State Here
        saveStateToArguments();
    }

    // //////////////////
    // Don't Touch !!
    // //////////////////

    private void saveStateToArguments()
    {
        if (getView() != null)
            savedState = saveState();
        if (savedState != null)
        {
            Bundle b = getArguments();
            b.putBundle("internalSavedViewState8954201239547",
                    savedState);
        }
    }

    // //////////////////
    // Don't Touch !!
    // //////////////////

    private boolean restoreStateFromArguments()
    {
        Bundle b = getArguments();
        savedState = b.getBundle("internalSavedViewState8954201239547");
        if (savedState != null)
        {
            restoreState();
            return true;
        }
        return false;
    }

    // ///////////////////////////////
    // Restore Instance State Here
    // ///////////////////////////////

    private void restoreState()
    {
        if (savedState != null)
        {
            // For Example
            // tv1.setText(savedState.getString("text"));
            onRestoreState(savedState);
        }
    }

    protected void onRestoreState(Bundle savedInstanceState)
    {

    }

    // ////////////////////////////
    // Save Instance State Here
    // ////////////////////////////

    private Bundle saveState()
    {
        Bundle state = new Bundle();
        // For Example
        // state.putString("text", tv1.getText().toString());
        onSaveState(state);
        return state;
    }

    protected void onSaveState(Bundle outState)
    {

    }
}
