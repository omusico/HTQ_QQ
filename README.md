# HTQ_QQ
本项目为本人参加校园APP制作大赛项目，整体框架为目前大多数优质应用主流框架（侧滑菜单+底部导航）,UI框架可供绝大数应用直接引用，主要模仿腾讯QQ，实现了发送文本与图片功能，绝大部分代码为本人所写，小部分代码参考了相关书上 或网上优秀代码，可供安卓程序员参考学习，如若要引用，请注明出处。

涉及到的安卓技术：

(1)自定义控件：
<br>自绘控件，组合控件，继承控件这三种方式都用到了
比如：仿QQ用来显示用户头像的CircleImageView采用的是自绘的方式（后改为github开源项目CircleImageView）
整个应用的自定义标题栏TitleBarView采用的是组合控件的方式，在该TitleBarView中提供了一些setter(),getter()方法来操作这些组合的控件
仿QQ滑动删除功能的MyListView采用的是继承控件的方式

(2)第三方接口调用：
<br>如在用户登录界面中提供的腾讯QQ授权登陆功能采用了腾讯开放平台提供的openAPI接口。

(3)github开源库的应用：
   <br>如侧滑菜单控件SlidingMenu和显示用户圆形图像的CircleImageView
   
(4)安卓中的消息传递：
 <br> 最基本的运用Handler在子线程与主线程之间传递信息<br>在MyApplication这个全局单例类中定义公共的对象及方法来供不同组件之间访问信息
<br>在ClientInputThread客户端读线程中采用了接口回调对外传递信息
<br>在GetMsgService服务中采用了广播与BaseActivity传递信息。

(5)网络通信及多线程：
    <br>最基本网络请求采用HttpUrlConnection和Volley(主要用来处理请求频繁但数据量不大的情况，比如Json和图片的请求)，解决Volley数据请求中文乱码问题。
    <br>最核心的聊天功能采用Socket通信，为了在网络上传送用户的信息，采用了ObjectInputStream/ObjectOutputStream来读写信息，定义的User类实现来Serializale接口实现序列化以达到在网络上传输的目的
将客户端读写功能放到单独的线程中，通过Client类来管理ClientInputThread与ClientOutputThread，在ClientInputThread中接收服务器端消息的代码在public void run(){}
方法中，因为该方法返回值为void无法返回线程中读取的信息，所以采用了接口回调
技术对外传递信息

(6)xml与json数据解析：
     <br>这个在腾讯第三方接口调用中用到，用来解析从腾讯服务器端获取的用户登录的一些基本信息，如昵称，用户头像等，这个主要参考腾讯开放平台提供的openAPI文档，在QQ天气功能模块也用到，主要采用Volley获取从中国天气网指定接口中获取的json数据。

(7)数据存储，数据库操作
   <br>比如保存用户头像等一些资料到本地文件，MessageDB保存用户的聊天记录到数据库
UserDB保存用户的好友信息到数据库。

调试错误:
<br>这个主要是通过在模拟器上运行出错时通过查看LogCat上的错误信息来判断代码逻辑(一般运行时出错都是代码逻辑上的错误)，某些很难判断的错误通过百度，贴吧，论坛，
stackoverflow（个人觉得这个网站是调bug求帮助最好的网站，可惜是英文）寻求错误信息查找解决方案。


以下为该APP程序结构图（vsd文件请参看目录中的HQ_QQ程序结构图.vsd文件）

![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/HQ_QQ程序结构图.jpg)
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/HQ_QQ程序结构图.vsd)

以下为APP部分运行截图，具体界面请参看运行截图文件夹
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/运行截图/启动界面.JPG)
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/运行截图/注册界面.jpg)
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/运行截图/登陆界面.jpg)
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/运行截图/腾讯QQ登陆授权界面.jpg)
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/运行截图/主界面_消息界面.jpg)
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/运行截图/主界面_消息_通话界面.jpg)
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/运行截图/主界面_联系人.jpg)
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/运行截图/主界面_动态.jpg)
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/运行截图/主界面消息_popUpWindow.jpg)
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/运行截图/滑动菜单界面_1.jpg)
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/运行截图/滑动菜单界面_2.jpg)
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/运行截图/滑动菜单界面_3.jpg)
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/运行截图/聊天界面_1.JPG)
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/运行截图/聊天界面_2.JPG)
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/运行截图/聊天界面_3.JPG)
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/运行截图/腾讯QQ空间登陆界面.jpg)
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/运行截图/修改个性签名界面.jpg)
![image](https://github.com/HuTianQi/HTQ_QQ/raw/master/运行截图/关于作者界面.jpg)






            *******************************************
            *  CopyRight:胡琪(htq) 三峡大学计算机系   *
            *              2015.3                     *
            *             verion:1.0                  *
            *******************************************
