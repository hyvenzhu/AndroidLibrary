package library.common.framework.ui.activity.view

import android.app.Activity
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 * 子 View 视图控制器，用来更加细颗粒的拆分 [AppDelegate]
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2019-1-23]
 */
abstract class ViewController(private val appDelegate: AppDelegate, parentId: Int) {

    val view: View

    init {
        val parent = appDelegate.contentView.findViewById<ViewGroup>(parentId)
        view = LayoutInflater.from(parent.context).inflate(getView(), null, false)
        parent.removeAllViews()
        parent.addView(view)
        appDelegate.addViewController(this)
    }

    var viewBindingObj: Any? = null

    inline fun <reified T> getViewBinding(): T {
        return (viewBindingObj as T?) ?: let {
            val bindMethod = T::class.java.getMethod("bind", View::class.java)
            (bindMethod.invoke(null, view) as T).apply {
                viewBindingObj = this
            }
        }
    }

    open fun initWidget() {}

    fun onShow() {}

    fun onHide() {}

    fun onDestroy() {}

    /**
     * 返回View布局
     *
     * @return
     */
    @LayoutRes
    abstract fun getView(): Int

    protected fun setOnClickListener(listener: View.OnClickListener?, vararg ids: Int) {
        appDelegate.setOnClickListener(listener, *ids)
    }

    protected fun <T : Activity?> getActivity(): T {
        return appDelegate.getActivity()
    }

    protected fun <T : Fragment?> getFragment(): T? {
        return appDelegate.getFragment()
    }

    protected fun getResource(): Resources = appDelegate.getResources()

    protected fun getString(@StringRes strRes: Int): String {
        return appDelegate.getString(strRes)
    }

    protected fun getString(@StringRes strRes: Int, vararg formatArgs: Any?): String {
        return appDelegate.getString(strRes, *formatArgs)
    }

    protected fun showToast(message: String) {
        appDelegate.showToast(message)
    }
}