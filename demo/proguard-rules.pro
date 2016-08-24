-ignorewarnings

# 建议使用fastjson转换的bean都实现Serializable接口
-keepclassmembers class * implements java.ios.Serializable  {
   *;
}

# Android-BaseLine 混淆----------------start
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
# Android-BaseLine 混淆----------------end