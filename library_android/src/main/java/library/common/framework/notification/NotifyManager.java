package library.common.framework.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Random;

import library.common.util.IntentUtils;

/**
 * 通知管理类
 *
 * @author zhuhf
 * @version [AndroidLibrary, 2018/10/26]
 */
public class NotifyManager {
    private Context context;
    private NotificationManager notificationManager;
    private Random random;

    public NotifyManager(@NonNull Context context) {
        this.context = context;
        init();
    }

    private void init() {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        random = new Random();
    }

    /**
     * 创建渠道
     *
     * @param channelEntity 渠道消息
     */
    public void createNotificationChannel(ChannelEntity channelEntity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(channelEntity, null);
        }
    }

    /**
     * 创建渠道组和一个渠道
     *
     * @param groupId
     * @param groupName
     * @param channel
     */
    public void createNotificationGroupWithChannel(@NonNull String groupId, @Nullable String groupName, @NonNull ChannelEntity channel) {
        ArrayList<ChannelEntity> channelList = new ArrayList<>();
        channelList.add(channel);
        createNotificationGroupWithChannel(groupId, groupName, channelList);
    }

    /**
     * 创建渠道组和一组渠道
     *
     * @param groupId
     * @param groupName
     * @param channelList
     */
    public void createNotificationGroupWithChannel(@NonNull String groupId, @Nullable String groupName, @NonNull ArrayList<ChannelEntity> channelList) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (!TextUtils.isEmpty(groupId)) {
                createNotificationGroup(groupId, groupName);
            }

            for (ChannelEntity channel : channelList) {
                createNotificationChannel(channel, groupId);
            }
        }
    }

    /**
     * 创建渠道，并创建组
     *
     * @param channelEntity
     * @param groupId
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(@NonNull ChannelEntity channelEntity, @Nullable String groupId) {
        NotificationChannel channel = new NotificationChannel(channelEntity.getChannelId(), channelEntity.getChannelName(), channelEntity.getImportance());
        channel.setShowBadge(channelEntity.isShowBadge());
        if (!TextUtils.isEmpty(channelEntity.getDescription())) {
            channel.setDescription(channelEntity.getDescription());
        }
        if (!TextUtils.isEmpty(groupId)) {
            channel.setGroup(groupId);
        }
        if (channelEntity.getSound() != null) {
            channel.setSound(channelEntity.getSound(), null);
        }
        if (channelEntity.getVibratePattern() != null) {
            channel.setVibrationPattern(channelEntity.getVibratePattern());
        }
        notificationManager.createNotificationChannel(channel);
    }

    /**
     * 创建渠道组
     *
     * @param groupId
     * @param groupName
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationGroup(@NonNull String groupId, @Nullable String groupName) {
        NotificationChannelGroup group = new NotificationChannelGroup(groupId, groupName);
        notificationManager.createNotificationChannelGroup(group);
    }

    /**
     * 删除渠道
     *
     * @param channelId
     */
    public void deleteNotificationChannel(@NonNull String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannel(channelId);
        }
    }

    /**
     * 删除组
     *
     * @param groupId
     */
    public void deleteNotificationChannelGroup(@NonNull String groupId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannelGroup(groupId);
        }
    }

    /**
     * 发送通知
     *
     * @param notification 通知具体内容
     * @return 通知Id
     */
    public int notifyNotify(@NonNull Notification notification) {
        int notifyId = getRandomId();
        return notifyNotify(notifyId, notification);
    }

    /**
     * 发送通知
     *
     * @param notifyId     通知Id
     * @param notification 通知具体内容
     * @return
     */
    public int notifyNotify(int notifyId, @NonNull Notification notification) {
        notificationManager.notify(notifyId, notification);
        return notifyId;
    }

    public int notifyNotify(String tag, int notifyId, @NonNull Notification notification) {
        notificationManager.notify(tag, notifyId, notification);
        return notifyId;
    }

    /**
     * 关闭状态栏通知
     *
     * @param notifyId 通知Id
     */
    public void cancelNotify(int notifyId) {
        notificationManager.cancel(notifyId);
    }

    /**
     * 关闭状态栏通知
     *
     * @param tag
     * @param notifyId
     */
    public void cancelNotify(String tag, int notifyId) {
        notificationManager.cancel(tag, notifyId);
    }

    /**
     * 关闭状态栏所有通知
     */
    public void cancelAll() {
        notificationManager.cancelAll();
    }

    /**
     * 默认设置，调用方可以添加和修改
     *
     * @param channelId
     * @param smallIcon
     * @param color
     * @return
     */
    public NotificationCompat.Builder getDefaultBuilder(@NonNull String channelId, @DrawableRes int smallIcon, @ColorInt int color) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setSmallIcon(smallIcon)
                .setColor(color);
        return builder;
    }

    /**
     * 检查当前渠道的通知是否可用，Android O及以上版本调用
     * <p>
     * 注：areNotificationsEnabled()返回false时，即当前App通知被关时，此方法仍可能返回true，
     *
     * @param channelId 渠道Id
     * @return false：不可用
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean areChannelsEnabled(@NonNull String channelId) {
        NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
        if (notificationChannel != null && notificationChannel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
            return false;
        }
        return true;
    }

    /**
     * 检查通知是否可用
     *
     * @return false：不可用
     */
    public boolean areNotificationsEnabled() {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        return notificationManagerCompat.areNotificationsEnabled();
    }

    /**
     * 调转到渠道设置页
     *
     * @param activity
     * @param channelId
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void gotoChannelSetting(Activity activity, @NonNull String channelId) {
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.getPackageName());
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
        if (IntentUtils.isIntentAvailable(activity, intent)) {
            activity.startActivity(intent);
        }
    }

    /**
     * 打开通知设置
     */
    public static void gotoNotificationSetting(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.getPackageName());
            if (IntentUtils.isIntentAvailable(activity, intent)) {
                activity.startActivity(intent);
            } else {
                openAppSetting(activity);
            }
        } else {
            openAppSetting(activity);
        }
    }

    /**
     * 打开App的设置页
     */
    static void openAppSetting(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        if (IntentUtils.isIntentAvailable(activity, intent)) {
            activity.startActivity(intent);
        }
    }

    /**
     * Generate a random integer
     *
     * @return int, [0, 50000)
     */
    public int getRandomId() {
        return random.nextInt(50000);
    }

    public static long[] getVibrate() {
        // 静止、震动
        return new long[]{0, 200, 200, 200};
    }
}



