Android-BaseLine
================

Android基线框架, 提供以下基础功能


1、升级Volley框架, 增加对多附件上传及进度更新回调的支持


2、提供统一的网络请求业务逻辑框架, 使用EventBus和Volley协作完成


3、提供统一的异步耗时任务执行框架, 使用EventBus作为通知中心来协作完成


4、提供日志管理框架, 对日志存储的大小、数量、格式做了规范处理, 支持打印级别的控制等


5、提供全局异常处理框架, 用户崩溃信息可以通过Email及时发送到开发者手中


6、提供统一的数据库访问接口, 支持多线程并发访问


7、SharedPreferences配置文件通过数据库来保存, 避免部分手机无法保存问题, 使用方法与原生API保持一致


8、基类Activity和Fragment提供View的注解和事件绑定框架, 以及统一的Toast、ProgressDialog等UI组件


9、基类Adapter, 提供控件复用的管理机制


10、支持App ANR时, 可以通过Email及时发送ANR的trace到开发者手中


11、升级Volley框架，ImageLoader支持内存和磁盘两级缓存（L1&L2 LRU Cache）


附：Volley升级版的Github地址
https://github.com/hiphonezhu/Android-Volley


欢迎star、watch、fork，共同交流进步，有好的建议或疑问请联系我hiphonezhu@gmail.com