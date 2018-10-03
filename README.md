# Libero-master
##特色

1.  使得进程间通信像调用本地函数一样方便简单。
2.  轻而易举在本地进程创建其他进程类的对象，轻而易举在本进程获取其他进程的单例，轻而易举在本进程使用其他进程的工具类
3.  跨进程调用对外不依赖aidl，仅提供接口

##基本原理

IPC的主要目的是调用其他进程的函数，Libero让你方便地调用其他进程函数，调用语句和本地进程函数调用一模一样。

##场景
一个app有一个主进程。给这个主进程命名为进程A。假设有一个进程B，想要调用进程A的函数。

##使用方法

###主进程注册

进程A中，被进程B调用的类需要事先注册。如果类上面没有加上注解，那么注册就不是必须的，Libero会通过类名进行反射查找相应的类。

###子进程AndroidManifest.xml

在AndroidManifest.xml中加入如下声明，你可以加上其他属性。

```
<service
   android:name="com.jaapin.library.libero.LiberoService$LiberoService0"
  android:exported="true" />

```

###子进程连接Libero



你可以在进程B的Application.OnCreate()或者Activity.OnCreate()中对Libero初始化。相应的API是Libero.connect(Context)。

```
Libero.connect(this);

```
###创建实例
Libero.getInstance(Class)

这个函数在进程A中通过指定类的getInstance方法创建实例，并将引用返回给进程B。第二个参数将传给对应的getInstance方法。

这个函数特别适合获取单例，这样进程A和进程B就使用同一个单例。

```
@ClassId("a")
public interface IUserManager {
    public void setUserName(String name);   
	public String getUserName(); 
}

```

进程B中，调用IUserManager iUserManager = Libero.getInstance(IUserManager.class);将得到UserManager的实例。


```
Toast.makeText(this, iUserManager.getUserName(), Toast.LENGTH_SHORT).show();
```
##工程说明
server 模块：模拟接口提供程序
client 模块：客户端模拟调用
Libero-core模块：Libero代码





