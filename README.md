###### ps:此图片来自网络请勿商用 
<img src="👻/ico.png" alt="Image" width="400" height="300" > 

# 莫愁前路无知己,天下谁人不识君.

# [集成开发环境](https://github.com/5tiaowu/MagicHands-IDE) 配合食用效果更佳！

### 声明：本库允许学习食用,允许食用者利用此库制作收费脚本,但不允许在此库基础上套壳变成收费框架!

# Android自动化引擎 · MagicHands

> The only people who have anything to fear from free software are those whose products are worth even less. 
>
> <p align="right">——David Emery</p>

MagicHands，是一款自动化测试引擎，可以在Android7.0以上免root运行自动化脚本，使用js编写，可以编写插件来扩展功能。

![](https://img.shields.io/badge/language-java-brightgreen.svg)


## 作者的碎碎念

中二男孩


## 找我
在线时间:(北京)12:00-00:00
- Q群:710985269
- QQ:3560000009
- Wx:php787 
- Tg:chenpingan_fjc
欢迎找我聊骚,以及建议和bug反馈【有求必应】

## 支持

目前已兼容 7.0 ～ 12.0并跟进后续新系统

## 如何使用

[点我看视频教程](https://www.bilibili.com/video/BV1Ao4y1E7NK/?share_source=copy_web&vd_source=2f8cb1fac706b3fcfd90ed86023a3c7c)

### Step 1.初始化：

参考demo的MainActivity的初始化工作流程

### Step 2.编写Js代码并运行：

在assets文件下新建js文件夹,在js文件夹下新建main.js为入口,在入口调用接口即可



### 相关API

```JavaScript
/**
获取给定节点的文本内容
@权限 无障碍
@函数名 getText
@参数 node - 节点对象
@返回值 成功:文本内容 失败:null
@功能 获取给定节点的文本内容
@介绍 本方法为免费方法由官方开发维护
@版本 1 正常
*/
function getText(node) {
    return js.getText(node);
 }

 
/**
尝试点击节点
@权限 无障碍
@函数名 tryClick
@参数 node - 节点对象
@返回值 成功:true 失败:false
@功能 尝试点击目标节点,当传入节点无法节点会依次向上层节点进行点击尝试
@介绍 本方法为免费方法由官方开发维护
@版本 1 正常
*/
function tryClick(node){
    return js.tryClick(node)
}


/**
获取节点的父亲节点
@权限 无障碍
@函数名 getParentNode
@参数 node - 节点
@返回值 成功:节点信息 失败:null
@功能 获取当前节点的父亲节点
@介绍 本方法为免费方法由官方开发维护
@版本 1 正常
*/
function getParentNode(node){
    return js.getParentNode(node)
}


/**
获取坐标点的Rgb值
@权限 常规权限
@函数名 getRgb
@参数 b - Bit对象 @参数 x - x坐标 @参数 y - y坐标
@返回值 成功:rgb字符串 失败:null
@功能 获取指定坐标点的rgb字符串
@介绍 本方法为免费方法由官方开发维护
@版本 1 正常
*/
function getRgb(b,x,y){
    return js.getRgb(b,x,y)
}

/**
捕获屏幕
@权限 常规权限
@函数名 setUpVirtualDisplay
@功能 捕获当前界面
@介绍 本方法为免费方法由官方开发维护
@版本 1 正常
*/
function setUpVirtualDisplay(){
     js.setUpVirtualDisplay()
}

/**
获取捕获的bit对象
@权限 常规权限
@函数名 getBitmap
@返回值 成功:bit对象 失败:null
@功能 获取捕获的bit对象
@介绍 本方法为免费方法由官方开发维护
@版本 1 正常
*/
function getBitmap(){
    return js.getBitmap()
}

```

更多其他操作看在线文档:[api.js](https://magichands.gitbook.io/api-docs-1/android/jie-shao)【在线文档暂不太完善,请先参考本地接口文档】



## 计划

 - opencv实现图色识别
 - 将要实现自动化的app加载到我们app中实现同一个进程的自动化
 - 利用蓝牙hid协议实现自动化
 - 利用android的adb提权原理实现免插数据线实现adb自动化
 - 脚手架工具:创建项目,打包项目,打包插件,调试代码,调试局部代码,远程调试,节点查看...

## 感谢

- [rhino](https://github.com/mozilla/rhino)
- [Android-Accessibility-Api](https://github.com/Vove7/Android-Accessibility-Api)

### License

> ```
> MIT License
> ```

>Copyright (c) 2023 pingan

>Permission is hereby granted, free of charge, to any person obtaining a copy
>of this software and associated documentation files (the "Software"), to deal
>in the Software without restriction, including without limitation the rights
>to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
>copies of the Software, and to permit persons to whom the Software is
>furnished to do so, subject to the following conditions:

>The above copyright notice and this permission notice shall be included in all
>copies or substantial portions of the Software.

>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
>IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
>FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
>AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
>LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
>OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
>SOFTWARE.
