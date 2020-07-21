package library.common.framework.notification;

import android.net.Uri;
import androidx.annotation.NonNull;

/**
 * 通知渠道
 *
 * @author zhuhf
 * @version [AndroidLibrary, 2018/10/26]
 */
public class ChannelEntity {
    /**
     * 渠道Id
     */
    private String channelId;
    /**
     * 渠道名称
     */
    private String channelName;
    /**
     * 重要等级
     */
    private int importance;
    /**
     * 描述
     */
    private String description;
    /**
     * 是否显示icon角标
     */
    private boolean showBadge;
    /**
     * 铃声
     */
    private Uri sound;
    /**
     * 震动
     */
    private long[] vibratePattern;

    public ChannelEntity(@NonNull String channelId, @NonNull String channelName, @ImportanceType int importance) {
        this.channelId = channelId;
        this.channelName = channelName;
        this.importance = importance;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public int getImportance() {
        return importance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isShowBadge() {
        return showBadge;
    }

    public void setShowBadge(boolean showBadge) {
        this.showBadge = showBadge;
    }

    public void setSound(Uri sound) {
        this.sound = sound;
    }

    public Uri getSound() {
        return sound;
    }

    public long[] getVibratePattern() {
        return vibratePattern;
    }

    public void setVibratePattern(long[] vibratePattern) {
        this.vibratePattern = vibratePattern;
    }
}
