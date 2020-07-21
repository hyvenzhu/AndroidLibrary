package library.common.framework.logic;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author zhuhf
 * @version 2020/7/7
 */
public class ViewModelProviders {
    public static <T extends ViewModel> T of(final FragmentActivity activity, final Class<? extends ViewModel> viewModelClass) {
        return (T) new ViewModelProvider(activity, new ViewModelProvider.NewInstanceFactory()).get(viewModelClass);
    }

    public static <T extends ViewModel> T of(final Fragment fragment, final Class<? extends ViewModel> viewModelClass) {
        return (T) new ViewModelProvider(fragment, new ViewModelProvider.NewInstanceFactory()).get(viewModelClass);
    }
}
