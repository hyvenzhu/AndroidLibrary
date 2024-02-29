package library.common.framework.ui.activity.view

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import library.common.R
import library.common.framework.ui.widget.AbstractLoadHelper
import library.common.util.APKUtils
import library.common.util.Callback
import library.common.util.NoDoubleClickListener
import library.common.util.StatusBarUtils

/**
 * 视图层代理的基类
 *
 * @author hyvenzhu
 * @version 2022/8/25
 */
abstract class AppDelegate: IDelegate {
    private val mViews = SparseArray<View?>()
    private lateinit var rootView: ViewGroup
    protected lateinit var titleGroup: ViewGroup
    private lateinit var context: Context
    private var loadViewHelper: AbstractLoadHelper? = null
    lateinit var content: ViewGroup

    private var fragment: Fragment? = null
    private var callback: Callback<Any?>? = null
    private var isVisible = false
    protected var isActivity = false
    private val viewControllers: MutableList<ViewController> = ArrayList()
    private var viewBinding: Any? = null

    override fun create(
        context: Context,
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        this.context = context
        isActivity = true
        rootView = (getRootView() as ViewGroup?) ?: inflater.inflate(
            R.layout.com_activity_common,
            container,
            false
        ) as ViewGroup
        titleGroup = rootView.findViewWithTag("title")
        content = rootView.findViewWithTag("content")
        val titleView = getTitleView(titleGroup)
        if (titleView != null) {
            titleGroup.visibility = View.VISIBLE
            if (fitStatusBarHeight()) {
                val statusBarHeight = APKUtils.getStatusBarHeight(context as Activity)
                titleGroup.layoutParams.apply {
                    height = statusBarHeight + getTitleHeight()
                    titleGroup.layoutParams = this
                }
            }
        } else {
            titleGroup.visibility = View.GONE
        }

        // 内容布局
        val layoutId = getContentLayoutId()
        if (layoutId != View.NO_ID) {
            inflater.inflate(layoutId, content, true)
        }
        // 初始化加载布局
        initLoadViewHelper(content)
    }

    override fun create(
        fragment: Fragment,
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        this.fragment = fragment
        create(fragment.requireContext(), inflater, container, savedInstanceState)
        isActivity = false
    }

    override fun onShow() {
        isVisible = true
        for (controller in viewControllers) {
            controller.onShow()
        }
    }

    override fun onHide() {
        isVisible = false
        for (controller in viewControllers) {
            controller.onHide()
        }
    }

    override fun onDestroy() {
        for (controller in viewControllers) {
            controller.onDestroy()
        }
        viewControllers.clear()
        viewBinding = null
    }

    /**
     * 是否可见
     *
     * @return
     */
    override fun isVisible(): Boolean = isVisible

    override fun getContentView(): ViewGroup = rootView

    fun hideTitle() {
        titleGroup.visibility = View.GONE
    }

    fun showTitle() {
        titleGroup.visibility = View.VISIBLE
    }

    /**
     * 自定义标题栏适应状态栏（标题栏高度需要为wrap_content或者match_parent）
     *
     * @param title
     */
    fun fitCustomTitle(title: View?) {
        StatusBarUtils.setTranslucent(getActivity<Activity>(), null)
        if (title != null) {
            val tag = title.getTag(R.id.com_paddingTop) ?:let {
                title.paddingTop.also {
                    title.setTag(R.id.com_paddingTop, it)
                }
            }
            val paddingTop = tag.toString().toInt()
            val statusBarHeight = APKUtils.getStatusBarHeight(getActivity())
            title.setPadding(
                title.paddingLeft, paddingTop + statusBarHeight,
                title.paddingRight, title.paddingBottom
            )
        }
    }

    fun cancelFitCustomTitle(title: View?) {
        StatusBarUtils.setTranslucent(getActivity<Activity>(), null)
        if (title != null) {
            val tag = title.getTag(R.id.com_paddingTop) ?:let {
                title.paddingTop.also {
                    title.setTag(R.id.com_paddingTop, it)
                }
            }
            val paddingTop = tag.toString().toInt()
            title.setPadding(
                title.paddingLeft, paddingTop,
                title.paddingRight, title.paddingBottom
            )
        }
    }

    fun fitCustomTitle(fragment: Fragment, title: View?) {
        StatusBarUtils.setTranslucent(fragment, null)
        if (title != null) {
            val tag = title.getTag(R.id.com_paddingTop) ?:let {
                title.paddingTop.also {
                    title.setTag(R.id.com_paddingTop, it)
                }
            }
            val paddingTop = tag.toString().toInt()
            val statusBarHeight = APKUtils.getStatusBarHeight(fragment.requireActivity())
            title.setPadding(
                title.paddingLeft, paddingTop + statusBarHeight,
                title.paddingRight, title.paddingBottom
            )
        }
    }

    /**
     * 设置通用标题栏颜色（状态栏会随之改变）
     *
     * @param color
     */
    fun setCommonTitleColor(@ColorInt color: Int) {
        StatusBarUtils.setTranslucent(getActivity<Activity>(), null)
        titleGroup.setBackgroundColor(color)
    }

    /**
     * 设置状态栏字体黑色（需要先调用[AppDelegate.fitCustomTitle]、[AppDelegate.fitCustomTitle]）
     *
     * @param activity
     */
    fun setLightMode(activity: Activity) {
        StatusBarUtils.setLightMode(activity)
    }

    /**
     * 设置状态栏字体白色（需要先调用[AppDelegate.fitCustomTitle]、[AppDelegate.fitCustomTitle]）
     *
     * @param activity
     */
    fun setDarkMode(activity: Activity) {
        StatusBarUtils.setDarkMode(activity)
    }

    /**
     * 标题栏隐藏和切换
     *
     * @param visible
     */
    fun setTitleVisible(visible: Boolean) {
        if (visible) {
            titleGroup.visibility = View.VISIBLE
        } else {
            titleGroup.visibility = View.GONE
        }
    }

    fun setCallback(callback: Callback<Any?>?) {
        this.callback = callback
    }

    fun doCall(data: Any?) {
        callback?.call(data)
    }

    fun doCall() {
        doCall(null)
    }

    /**
     * 返回标题栏，通过重写自定义标题栏
     *
     * @param root
     * @return
     */
    protected abstract fun getTitleView(root: ViewGroup?): View?

    /**
     * 返回内容布局
     *
     * @return
     */
    protected abstract fun getContentLayoutId(): Int

    /**
     * 返回标题栏高度（单位：px）
     *
     * @return
     */
    protected open fun getTitleHeight(): Int {
        return APKUtils.dip2px(context, 45f)
    }

    protected open fun fitStatusBarHeight() = true

    var viewBindingObj: Any? = null

    inline fun <reified T> getViewBinding(): T {
        return (viewBindingObj as T?) ?: let {
            val bindMethod = T::class.java.getMethod("bind", View::class.java)
            (bindMethod.invoke(null,  content.getChildAt(0)) as T).apply {
                viewBindingObj = this
            }
        }
    }

    /**
     * 默认的根布局是竖直方向分为两个View：
     * 0：标题
     * 1：内容
     * ----------------------------------------------------
     * 如果你想改变这种行为，可以重写此方法。返回的布局需要满足两个条件：
     * 1、标题 tag 为：title
     * 2、内容 tag 为：content
     *
     * @return
     */
    protected open fun getRootView(): View? = null

    override fun initWidget() {}

    override fun initWidget(intent: Intent) {
        initWidget()
    }

    override fun initWidget(args: Bundle?) {
        initWidget()
    }

    override fun initChildControllers() {
        for (viewController in viewControllers) {
            viewController.initWidget()
        }
    }

    operator fun <T : View> get(id: Int): T {
        var view = mViews[id] as T?
        if (view == null) {
            view = rootView.findViewById(id)
            mViews.put(id, view)
        }
        return view!!
    }

    fun setOnClickListener(listener: View.OnClickListener?, vararg ids: Int) {
        for (id in ids) {
            listener?.let {
                get<View>(id).setOnClickListener(object : NoDoubleClickListener() {
                    override fun onNoDoubleClick(v: View) {
                        it.onClick(v)
                    }
                })
            } ?: let {
                get<View>(id).setOnClickListener(null)
            }
        }
    }

    fun <T : Activity> getActivity(): T {
        return context as T
    }

    fun <T : Fragment?> getFragment(): T? {
        return fragment as T?
    }

    fun getResources(): Resources = getActivity<Activity>().resources

    fun getString(@StringRes strRes: Int): String {
        return getActivity<Activity>().getString(strRes)
    }

    fun getString(@StringRes strRes: Int, vararg formatArgs: Any?): String {
        return getActivity<Activity>().getString(strRes, *formatArgs)
    }

    /**
     * 初始化LoadViewHelper
     *
     * 默认情况，getContentLayoutId() 返回的布局会作为整个加载布局。如果你想改变，重写 getLoadView() 方法
     *
     * @param view
     */
    private fun initLoadViewHelper(view: View) {
        loadViewHelper = getLoadViewHelper(getLoadView() ?: view)
    }

    /**
     * 返回需要显示"加载"的布局
     *
     * @return
     */
    protected open fun getLoadView(): View? {
        return null
    }

    /**
     * 自定义加载、错误、无数据布局，子类重载实现定制化
     *
     * @param view
     * @return
     */
    protected abstract fun getLoadViewHelper(view: View): AbstractLoadHelper

    fun showLoadView() {
        showLoadView(null, -1, -1)
    }

    fun showLoadView(loadString: String?) {
        showLoadView(loadString, -1, -1)
    }

    /**
     * 加载中页面
     */
    fun showLoadView(loadString: String?, @DrawableRes loadRes: Int, @DrawableRes bgRes: Int) {
        loadViewHelper?.show(loadViewHelper?.loadingView(loadString, loadRes, bgRes))
    }

    fun showLoadError(listener: View.OnClickListener?) {
        showLoadError(null, -1, true, null, -1, listener)
    }

    fun showLoadError(errMsg: String?, listener: View.OnClickListener?) {
        showLoadError(errMsg, -1, true, null, -1, listener)
    }

    fun showLoadError(
        errMsg: String?,
        @DrawableRes bgResId: Int,
        listener: View.OnClickListener?
    ) {
        showLoadError(errMsg, -1, true, null, bgResId, listener)
    }

    fun showLoadErrorWithoutButton(errMsg: String?) {
        showLoadError(errMsg, -1, false, null, -1, null)
    }

    /**
     * 显示加载失败
     *
     * @param errorText
     * @param errorRes
     * @param showButton
     * @param buttonText
     * @param bgRes
     * @param onClickListener
     */
    fun showLoadError(
        errorText: String?,
        @DrawableRes errorRes: Int,
        showButton: Boolean,
        buttonText: String?,
        @DrawableRes bgRes: Int,
        onClickListener: View.OnClickListener?
    ) {
        loadViewHelper?.show(
            loadViewHelper?.errorView(
                errorText,
                errorRes,
                showButton,
                buttonText,
                bgRes,
                onClickListener
            )
        )
    }

    fun showLoadEmpty() {
        showLoadEmpty(null, -1, -1, null)
    }

    fun showLoadEmpty(emptyMsg: String?) {
        showLoadEmpty(emptyMsg, -1, -1, null)
    }

    fun showLoadEmpty(emptyMsg: String?, onClickListener: View.OnClickListener?) {
        showLoadEmpty(emptyMsg, -1, -1, onClickListener)
    }

    fun showLoadEmpty(@DrawableRes bgRes: Int) {
        showLoadEmpty(null, -1, bgRes, null)
    }

    fun showLoadEmpty(onClickListener: View.OnClickListener?) {
        showLoadEmpty(null, -1, -1, onClickListener)
    }

    fun showLoadEmpty(emptyMsg: String?, @DrawableRes emptyRes: Int) {
        showLoadEmpty(emptyMsg, emptyRes, -1, null)
    }

    fun showLoadEmpty(emptyMsg: String?, @DrawableRes emptyRes: Int, @DrawableRes bgRes: Int) {
        showLoadEmpty(emptyMsg, emptyRes, bgRes, null)
    }

    fun showLoadEmpty(
        emptyMsg: String?,
        @DrawableRes emptyRes: Int,
        onClickListener: View.OnClickListener?
    ) {
        showLoadEmpty(emptyMsg, emptyRes, -1, onClickListener)
    }

    /**
     * 显示空数据页面
     *
     * @param emptyMsg
     * @param emptyRes
     * @param onClickListener
     */
    fun showLoadEmpty(
        emptyMsg: String?,
        @DrawableRes emptyRes: Int,
        @DrawableRes bgRes: Int,
        onClickListener: View.OnClickListener?
    ) {
        loadViewHelper?.show(
            loadViewHelper?.emptyView(
                emptyMsg,
                emptyRes,
                null,
                bgRes,
                onClickListener
            )
        )
    }

    fun showLoadEmpty(
        emptyMsg: String?,
        buttonText: String?,
        onClickListener: View.OnClickListener?
    ) {
        loadViewHelper?.show(
            loadViewHelper?.emptyView(
                emptyMsg,
                -1,
                buttonText,
                -1,
                onClickListener
            )
        )
    }

    /**
     * 隐藏加载页面
     */
    fun hideLoadView() {
        loadViewHelper?.hide()
    }

    /**
     * Toast
     *
     * @param message
     */
    fun showToast(message: CharSequence?) {
        loadViewHelper?.showToast(message)
    }

    /**
     * 显示加载框
     *
     * @param message
     */
    fun showProgress(message: String?) {
        loadViewHelper?.showProgress(message, false)
    }

    /**
     * 显示加载框
     *
     * @param message
     * @param cancelable
     */
    fun showProgress(message: String?, cancelable: Boolean) {
        loadViewHelper?.showProgress(message, cancelable)
    }

    /**
     * 显示加载框
     *
     * @param message
     * @param cancelable
     * @param cancelListener
     */
    fun showProgress(
        message: String?,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) {
        loadViewHelper?.showProgress(message, cancelable, cancelListener)
    }

    /**
     * 隐藏加载框
     */
    fun hideProgress() {
        loadViewHelper?.hideProgress()
    }

    fun addViewController(viewController: ViewController) {
        viewControllers!!.add(viewController)
    }
}