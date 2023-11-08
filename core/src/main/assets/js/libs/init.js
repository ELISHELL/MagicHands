//const { java,rhino } = require('java');
//
//var js=java.import('magichands.core.ExposingApi')

// var global_cls= magichands.core.OpenApi



function getApc() {
    return js.getApc()
}

function getAcc() {
    return js.getAcc()
}

function getAct() {
    return js.getAct()
}

function getCon() {
    return js.getCon()
}

function Bo(object1, object2) {
    return String(object1) === String(object2);
}

function checkSubstring(str, sub) {
  return str.includes(sub);
}

function object2JsonString(o) {
    if (o == null) {
        return "{}";
    }
    if ((typeof o) === 'string') {
        return o;
    }
    return JSON.stringify(o);
}
/**
pairArray
@权限 普通权限
@参数  arguments[...] - 坐标集合
@返回值 成功:Array 失败:null
*/
function pairArray() {
    switch (arguments.length) {
        case 4:
                return js.pairArray(arguments[0], arguments[1],arguments[2], arguments[3]);

        case 6:
            return js.pairArray(arguments[0], arguments[1],arguments[2], arguments[3],arguments[4], arguments[5]);

        case 8:
            return js.pairArray(arguments[0], arguments[1],arguments[2], arguments[3],arguments[4], arguments[5],arguments[6], arguments[7]);


        default:
           logd("","pairArray","意料之外");
    }
}
/**
listpairArray
@权限 普通权限
@参数  arguments[...] - 列表坐标集合
@返回值 成功:Array 失败:null
*/
function listpairArray() {
    switch (arguments.length) {
        case 2:
            return js.listpairArray(arguments[0], arguments[1]);

        case 3:
            return  js.listpairArray(arguments[0], arguments[1],arguments[2]);

        case 4:
            return js.listpairArray(arguments[0], arguments[1],arguments[2], arguments[3]);


        default:
            logd("","listpairArray","意料之外");
    }
}

/**
float4DArray
@权限 普通权限
@参数 s - 4维字符串
@返回值 成功:浮点4维数组 失败:null
*/
function float4DArray (s) {
    return js.Float4DArray(s)
}

/**
启动nodejs
@权限 普通权限
*/
function nodeStart () {
    return js.nodejsStart()
}

/**
调用nodejs
@权限 普通权限
@参数 str - nodejs代码
@返回值 成功:对应node返回值 失败:null
*/
function nodeEval (str) {
    return js.nodejsEval(str)
}


/**
休眠
@权限 普通
@参数 time - ms
*/
 function  sleep(time) {

    js.sleep(time)
}


/**
启动js脚本
@权限 普通权限
*/
function start () {
    js.start()
}

/**
停止
@权限 普通权限
*/
function exit () {
    js.exit()
}



/**
日志
@权限 普通权限
@参数 hh - 行号 @参数 tag - 标签 @参数 msg - 消息
*/
function logd (hh, tag, msg) {
    if (typeof msg === 'undefined') {
        msg = tag; // 如果tag参数未提供，设置默认值
        tag=""
    }
    js.logd(hh, tag, msg)
}


/**
吐司
@权限 普通权限
@参数 hh - 行号 @参数 tag - 标签 @参数 msg - 消息
*/
function toast (hh, tag, msg) {
    if (typeof msg === 'undefined') {
        msg = tag; // 如果tag参数未提供，设置默认值
        tag=""
    }


    js.toast(hh, tag, msg)
}



/**
获取内部缓存路径
@权限 普通权限
@返回值 成功:路径地址 失败:null
*/
function getExternalFilesDir () {
    return js.getExternalFilesDir()
}

/**
返回运行目录
@权限 普通权限
@返回值 成功:运行目录地址 失败:null
*/
function homedir () {

    return js.getBase()
}





function RectEx(rectEx) {
    this.index = 0;
    this.top = 0;
    this.bottom = 0;
    this.left = 0;
    this.right = 0;
    this.similarity = 0;
    this.info = "";
    if (rectEx != null) {
        this.top = rectEx["top"];
        this.bottom = rectEx["bottom"];
        this.left = rectEx["left"];
        this.right = rectEx["right"];
        this.similarity = rectEx["similarity"];
        this.index = rectEx["index"];
        this.info += rectEx["info"];
    }
}



/**
 * 取得中间的坐标点
 * @return Point 对象
 */
RectEx.prototype.center = function () {
    let p = new android.graphics.Point(null);
    p.x = this.left + (this.right - this.left) / 2;
    p.y = this.top + (this.bottom - this.top) / 2;
    return p;
};
RectEx.jsonToObject = function (res) {
    if (res == null || res == "") {
        return null;
    }
    res = JSON.parse(res);
    if (res == null) {
        return null;
    }
    return new android.graphics.Rect(res);
};


RectEx.get = function () {
    return new android.graphics.Rect(null);
};
RectEx.prototype.setLeft = function (left) {
    this.left = left;
    return this;
};
RectEx.prototype.setTop = function (top) {
    this.top = top;
    return this;
};
RectEx.prototype.setRight = function (right) {
    this.right = right;
    return this;
};
RectEx.prototype.setBottom = function (bottom) {
    this.bottom = bottom;
    return this;
};
RectEx.prototype.toJSONString = function () {
    return JSON.stringify(this);
};





/** @type {节点相关} */
const node = {}
var node_cls= magichands.core.OpenApi.node
node.inf = {}
var node_inf_cls= magichands.core.OpenApi.node.inf
node.obj = {}
var node_obj_cls= magichands.core.OpenApi.node.obj


/**
获取给定节点的文本内容。
@权限 无障碍权限
@参数 node - 节点对象
@返回值 成功:文本内容 失败:null
*/
node.obj.getText = function (node) {
    return node_obj_cls.getText(node);
}


/**
尝试点击节点
@权限 无障碍权限
@参数 node - 节点对象
@返回值 成功:true 失败:false
*/
node.obj.tryClick = function (node) {

    return Bo(node_obj_cls.tryClick(node), true)
}


/**
判断有无包含节点信息,类似正则匹配
@权限 无障碍权限
@参数 type - 节点类型 @参数 node - 节点描述
@返回值 成功:节点对象 失败:null
*/
node.obj.findContainsNode = function (type, node) {
    return node_obj_cls.containsSwich(type, node)
}


/**
判断有无节点信息类似,完全匹配
@权限 无障碍权限
@参数 type - 节点类型 @参数 node - 节点描述
@返回值 成功:节点对象 失败:null
*/
node.obj.findAllNode = function (type, node) {
    return node_obj_cls.allSwich(type, node)
}


/**
获取节点范围
@权限 无障碍权限
@参数 node - 节点对象
@返回值 成功:节点范围 失败:null
*/
node.obj.getBounds = function (node) {
    return node_obj_cls.getBounds(node)
}



/**
点击节点
@权限 无障碍权限
@参数 node - 节点对象
@返回值 成功:true 失败:false
*/
node.obj.click = function (node) {

    return Bo(node_obj_cls.click(node), true)
}


/**
全局长按节点
@特殊说明 常用于点击网页节点
@权限 无障碍权限
@参数 node - 节点对象
@返回值 成功:true 失败:false
*/
node.obj.globalLongClick = function (node) {

    return Bo(node_obj_cls.globalLongClick(node), true)
}


/**
全局点击节点
@特殊说明 常用于点击网页节点
@权限 无障碍权限
@参数 node - 节点对象
@返回值 成功:true 失败:false
*/
node.obj.globalClick = function (node) {

    return Bo(node_obj_cls.globalClick(node), true)
}



/**
长点击节点
@权限 无障碍权限
@参数 node - 节点对象
@返回值 成功:true 失败:false
*/
node.obj.longClick = function (node) {

    return Bo(node_obj_cls.longClick(node), true)
}


/**
尝试长点击节点
@权限 无障碍权限
@参数 node - 节点对象
@返回值 成功:true 失败:false
*/
node.obj.tryLongClick = function (node) {

    return Bo(node_obj_cls.tryLongClick(node), true)
}

/**
双击点击节点
@权限 无障碍权限
@参数 node - 节点对象
@返回值 成功:true 失败:false
*/
node.obj.doubleClick = function (node) {

    return Bo(node_obj_cls.doubleClick(node), true)
}



/**
节点调用链
@权限 无障碍权限
@返回值 成功:SmartFinder对象 失败:null
*/
node.obj.nodeCallChain = function () {

    var SmartFinderConditionsKt =  cn.vove7.auto.core.viewfinder.SmartFinderConditionsKt
    return {
        text: function (node) {
            let text = SmartFinderConditionsKt.text(js.SF(), node)
            return {
                id: function (node) {
                    let id = SmartFinderConditionsKt.id(text, node)
                    return {
                        desc: function (node) {
                            return {
                                get: function () {

                                    return SmartFinderConditionsKt.desc(id, node)

                                },
                            }
                        },
                        get: function () {

                            return id

                        },
                    }

                },
                desc: function (node) {
                    let desc = SmartFinderConditionsKt.desc(text, node)
                    return {
                        id: function (node) {

                               return {
                                get: function () {



                                    return SmartFinderConditionsKt.id(desc, node)

                                },
                            }

                        },

                        get: function () {
                            return desc

                        },

                    }

                },

                get: function () {

                    return text

                },


            }


        },
        id: function (node) {
            let id = SmartFinderConditionsKt.id(js.SF(), node)
            return {
                text: function (node) {
                    let text = SmartFinderConditionsKt.text(id, node)
                    return {
                        desc: function (node) {

                            return {
                                get: function () {

                                    return  SmartFinderConditionsKt.desc(text, node)

                                },
                            }

                        },
                        get: function () {

                            return text

                        },
                    }

                },
                desc: function (node) {
                    let desc = SmartFinderConditionsKt.desc(id, node)
                    return {
                        text: function (node) {


                            return {
                                get: function () {

                                    return  SmartFinderConditionsKt.text(desc, node)

                                },
                            }

                        },
                        get: function () {

                            return desc

                        },
                    }

                },

                get: function () {

                    return id

                },


            }


        },
        desc: function (node) {

            let desc = SmartFinderConditionsKt.desc(js.SF(), node)
            return {
                text: function (node) {
                    let text = SmartFinderConditionsKt.text(desc, node)
                    return {
                        id: function (node) {

                            return {
                                get: function () {

                                    return SmartFinderConditionsKt.id(text, node)

                                },
                            }


                        },
                        get: function () {

                            return text

                        },
                    }

                },
                id: function (node) {
                    let id = SmartFinderConditionsKt.text(desc, node)
                    return {
                        text: function (node) {
                            return {
                                get: function () {

                                    return SmartFinderConditionsKt.text(id, node)

                                },
                            }


                        },
                        get: function () {

                            return id

                        },
                    }

                },
                get: function () {

                    return desc

                },



            }

        },
    }
}



/**
正则匹配Text节点
@权限 无障碍权限
@参数 node - 正则表达式
@返回值 成功:节点对象集合 失败:null
*/
node.obj.textNodeRegular = function (regular) {
    return node_obj_cls.textNodeRegular(regular)
}



/**
判断有无节点
@权限 无障碍权限
@参数 node - 节点对象
@返回值 成功:true 失败:false
*/
node.obj.nodeExist = function (node) {
if (node !== null && String(node).trim() !== ""){
if(checkSubstring(String(node),",")){
return true
}
return Bo(node_obj_cls.nodeExist(node), true)
}else{
return false
}

}



/**
筛选节点
@权限 无障碍权限
@参数  callback - 回调
@返回值 成功:节点对象集合集合 失败:null
*/
node.obj.nodeFilter =function(callback){
    node_obj_cls.nodeFilter(callback)
 return node_obj_cls.filter()
}
/**
查找节点id
@权限 无障碍权限
@参数 node - 节点描述
@返回值 成功:节点信息 失败:null
*/
node.inf.findNodeById = function (node) {
    return node_inf_cls.findNodeById(node)
}
/**
查找节点text
@权限 无障碍权限
@参数 node - 节点描述
@返回值 成功:节点信息 失败:null
*/
node.inf.findNodeByText = function (node) {
    return node_inf_cls.findNodeByText(node)
}
/**
查找节点desc
@权限 无障碍权限
@参数 node - 节点描述
@返回值 成功:节点信息 失败:null
*/
node.inf.findNodeByDesc = function (node) {
    return node_inf_cls.findNodeByDesc(node)
}
/**
通过节点输入文本
@权限 无障碍权限
@参数 node - 节点信息 @参数 t - 文本
@返回值 成功:true 失败:false
*/
node.inf.inputText = function (node, t) {

    return Bo(node_inf_cls.inputText(node, t), true)
}
/**
查找传入节点的前一个兄弟节点
@权限 无障碍权限
@参数 node - 节点信息
@返回值 成功:节点信息 失败:null
*/
node.inf.findPreviousSibling = function (node) {
    return node_inf_cls.findPreviousSibling(node)
}
/**
查找传入节点的后面兄弟节点
@权限 无障碍权限
@参数 node - 节点信息
@返回值 成功:节点信息 失败:null
*/
node.inf.findNextSibling = function (node) {
    return node_inf_cls.findNextSibling(node)
}

/**
获取节点的父亲节点
@权限 无障碍权限
@参数 node - 节点信息
@返回值 成功:节点信息 失败:null
*/
node.inf.getParentNode = function (node) {
    return node_inf_cls.getParentNode(node)
}
/**
获取节点的儿子节点
@权限 无障碍权限
@参数 node - 节点信息
@返回值 成功:节点信息 失败:null
*/
node.inf.getChildNodes = function (node) {
    return node_inf_cls.getChildNodes(node)
}



/**
强制更新当前页面的节点信息
@权限 无障碍权限
*/
node.refreshCurrentPageNodes = function () {
    node_cls.refreshCurrentPageNodes()
}


/**
清楚节点缓存
@权限 无障碍权限
@返回值 成功:true 失败:false
*/
node.clearNodeCache = function () {
    return Bo(node_cls.clearAccessibilityCache(), true)
}






/** @type {坐标相关} */
const point = {}
var point_cls= magichands.core.OpenApi.point


/**
触摸长按
@权限 无障碍权限
@参数 x - 横坐标 @参数 y - 竖坐标
@返回值 成功:true 失败:false
*/
point.touchDown = function (x, y) {

    return Bo(point_cls.touchDown(x, y), true)
}
/**
触摸滑动
@权限 无障碍权限
@参数 x - 横坐标 @参数 y - 竖坐标
@返回值 成功:true 失败:false
*/
point.touchMove = function (x, y) {

    return Bo(point_cls.touchMove(x, y), true)
}
/**
触摸抬起
@权限 无障碍权限
@参数 x - 横坐标 @参数 y - 竖坐标
@返回值 成功:true 失败:false
*/
point.touchUp = function (x, y) {

    return Bo(point_cls.touchUp(x, y), true)
}



/**
点击坐标
@权限 无障碍权限
@参数 x - 横坐标 @参数 y - 竖坐标
@返回值 成功:true 失败:false
*/
point.clickPoint = function (x, y) {

    return Bo(point_cls.clickPoint(x, y), true)
}


/**
长点击坐标
@权限 无障碍权限
@参数 x - 横坐标 @参数 y - 竖坐标
@返回值 成功:true 失败:false
*/
point.longClickPoint = function (x, y) {

    return Bo(point_cls.longClickPoint(x, y), true)
}

/**
模拟滑动
@权限 无障碍权限
@参数 x - 开始横坐标 @参数 y - 开始竖坐标 @参数 x1 - 结束横坐标 @参数 y1 - 结束竖坐标 @参数 dur - 滑动时长
@返回值 成功:true 失败:false
*/
point.swipeToPoint = function (x, y, x1, y1, dur) {

    return Bo(point_cls.swipeToPoint(x, y, x1, y1, dur), true)
}



/**
根据坐标生成路径执行手势
@权限 无障碍权限
@参数 s - 运行时长 p - 坐标组
@返回值 成功:true 失败:false
*/
point.gesture = function (s,p) {
    return Bo(point_cls.gesture(s,p), true)
}

/**
多路径手势
@权限 无障碍权限
@参数 s - 运行时长 Array - 坐标组组
@返回值 成功:true 失败:false
*/
point.gestures = function (s,p) {
    return Bo(point_cls.gestures(s,p), true)
}

// /**
// 向上滑动根据坐标
// @权限 无障碍权限
// @返回值 成功:true 失败:false
// */
// point.scrollUp = function () {
//     return Bo(point_cls.scrollUp(), true)
// }

// /**
// 向下滑动根据坐标
// @权限 无障碍权限
// @返回值 成功:true 失败:false
// */
// point.scrollDown = function () {
//     return Bo(point_cls.scrollDown(), true)
// }


/** @type {按键相关} */
const button = {}
var button_cls= magichands.core.OpenApi.button

/**
返回键
@权限 无障碍权限
@返回值 成功:true 失败:false
*/
button.back = function () {

    return Bo(button_cls.back(), true)
}


/**
主页键
@权限 无障碍权限
@返回值 成功:true 失败:false
*/
button.home = function () {

    return Bo(button_cls.home(), true)
}

/**
快捷设置面板键
@权限 无障碍权限
@返回值 成功:true 失败:false
*/

button.quickSettings = function () {

    return Bo(button_cls.quickSettings(), true)
}
/**
设备电源菜单键
@权限 无障碍权限
@返回值 成功:true 失败:false
*/
button.powerDialog = function () {
    return Bo(button_cls.powerDialog(), true)
}



/**
下拉通知栏键
@权限 无障碍权限
@返回值 成功:true 失败:false
*/
button.pullNotificationBar = function () {
    return Bo(button_cls.pullNotificationBar(), true)
}


/**
多任务切换器键
@权限 无障碍权限
@返回值 成功:true 失败:false
*/
button.recents = function () {
    return Bo(button_cls.recents(), true)

}


/**
锁屏键
@权限 无障碍权限
@返回值 成功:true 失败:false

*/
button.lockScreen = function () {
    return Bo(button_cls.lockScreen(), true)

}


/**
截屏
@权限 无障碍权限
@返回值 成功:true 失败:false
*/
button.screenShot = function () {
    return Bo(button_cls.screenShot(), true)

}


/**
分屏
@权限 无障碍权限
@返回值 成功:true 失败:false
*/
button.splitScreen = function () {
    return Bo(button_cls.splitScreen(), true)
}






/** @type {app相关} */
const app = {}
var app_cls= magichands.core.OpenApi.app

/**
打开App
@权限 无障碍权限
@参数 pkg - 包名 mode - 模式(1:普通权限尝试打开,2:无障碍权限尝试打开)
@返回值 成功:true 失败:false
*/
app.openApp = function (mode,pkg) {

    return Bo(app_cls.openApp(mode,pkg), true)
}

/**
打开Scheme
@权限 普通权限
@参数 url - 协议字符串
*/
app.openScheme = function (url) {
    app_cls.openScheme(url)
}




/** @type {录屏相关} */
const recordscreen = {}
var recordscreen_cls= magichands.core.OpenApi.recordscreen


/**
申请录屏权限
@权限 普通权限
@参数 ay - AppCompatActivity对象,不传入使用默认对象
*/
recordscreen.startScreenCapture = function (ay) {
    ay = ay || getApc()
    recordscreen_cls.startScreenCapture(ay)
}

/**
判断录屏权限状态
@权限 普通权限
@返回值 成功:true 失败:false
*/
recordscreen.isScreenShot = function () {

    return Bo(recordscreen_cls.isScreenShot(), true)
}
/**
捕获屏幕图像
@权限 截图权限
@参数 left 左@参数 top 上 @参数 right 右 @参数 bottom 下
*/
recordscreen.setUpVirtualDisplay = function (left,top,right,bottom) {
    recordscreen_cls.setUpVirtualDisplay(left,top,right,bottom)
}

/**
释放录屏权限
@权限 普通权限
@参数 ay - AppCompatActivity对象,不传入使用默认对象
@返回值 成功:true 失败:false
*/
recordscreen.stopRecordscreen = function (ay) {
    ay = ay || getApc()

    return Bo(recordscreen_cls.stopScreenCapture(ay), true)
}



/**
申请录屏权限
@权限 普通权限
@参数 number - 等待事件/秒
@返回值 成功:true 失败:false
*/
recordscreen.checkRecordscreen = function (number) {
    recordscreen.startScreenCapture()

    for (let i = 0; i < number*10; i++) {
        sleep(100)
        if (recordscreen.isScreenShot()) {
            return true;
        }


    }
    return false;
}
/**
截图
@权限 截图权限
@参数 number - 等待事件/秒 @参数 left 左@参数 top 上 @参数 right 右 @参数 bottom 下
@返回值 成功:bit对象 失败:null
*/
recordscreen.setUpVirtualDisplays = function (number,left,top,right,bottom) {
    recordscreen.setUpVirtualDisplay(left,top,right,bottom)
    for (let i = 0; i < number*10; i++) {
        sleep(100)
        if (recordscreen.getBitmap() != null) {
            bit = recordscreen.getBitmap()
            return bit
        }
    }
    return null
}



/**
获取捕获的bit对象
@权限 普通权限
@返回值 成功:bit对象 失败:null
*/
recordscreen.getBitmap = function () {
    return recordscreen_cls.getBitmap()
}



/** @type {图片相关} */
const image = {}
var image_cls= magichands.core.OpenApi.image



/**
base64图像数据转换为bitmap对象
@权限 普通权限
@参数 base64String - base64数据
@返回值 成功:bit对象 失败:null
*/
image.base64ToBitmap = function (base64String) {
    return image_cls.base64ToBitmap(base64String)
}


/**
bitmap对象转换为base64图像数据
@权限 普通权限
@参数 bit - bit对象
@返回值 成功:base64字符串 失败:null
*/
image.bitmapToBase64 = function (bit) {
    return image_cls.bitmapToBase64(bit)
}



/**
全分辨率-找图Surf
@权限 普通权限
@参数 img - 大图 @参数 template - 小图 @参数  imgResize - 大图缩放倍数  例如 1表示不缩放 0.5表示缩小一倍 默认1.0 @c参数 tempResize - 小图缩放倍数  例如 1表示不缩放 0.5表示缩小一倍 默认1.0  @参数 sx - 起点x 默认0 @参数 sy - 起点y 默认0 @参数 ex - 终点x 默认0 @参数 ey - 终点y 默认0 @参数 threshold - 匹配度  默认是0.6 @参数 upright - true 只计算正常方向 ，false 计算全部方向（耗时） 默认true
@返回值 成功:Point[] 失败:null
*/

image.findImgBySurfEx = function (img, imgResize, template, tempResize, threshold, sx, sy, ex, ey, upright) {
    imgResize = imgResize || 1.0;
    tempResize = tempResize || 1.0;
    sx = sx || 0;
    sy = sy || 0;
    ex = ex || 0;
    ey = ey || 0;
    threshold = threshold || 0.6;
    if (typeof (upright) == 'object' || typeof (upright) == 'undefined') { upright = true; }
    let res = image_cls.findImgBySurfEx(img, imgResize, template, tempResize, threshold, sx, sy, ex, ey, upright)
    if (res == null) {
        return null;
    }
    let x1 = [];
    for (let i = 0; i < res.length; i++) {
        x1.push(new android.graphics.Point(res[i]));
    }
    return x1;

}

/**
全分辨率-找图Sift
@权限 普通权限
@参数 img - 大图 @参数 template - 小图 @参数  imgResize - 大图缩放倍数  例如 1表示不缩放 0.5表示缩小一倍 默认1.0 @c参数 tempResize - 小图缩放倍数  例如 1表示不缩放 0.5表示缩小一倍 默认1.0  @参数 sx - 起点x 默认0 @参数 sy - 起点y 默认0 @参数 ex - 终点x 默认0 @参数 ey - 终点y 默认0 @参数 threshold - 匹配度  默认是0.6
@返回值 成功:Point[] 失败:null
*/

image.findImgBySift = function (img, imgResize, template, tempResize, threshold, sx, sy, ex, ey) {
    imgResize = imgResize || 1.0;
    tempResize = tempResize || 1.0;
    sx = sx || 0;
    sy = sy || 0;
    ex = ex || 0;
    ey = ey || 0;
    threshold = threshold || 0.6;

    let res = image_cls.findImgBySift(img, imgResize, template, tempResize, threshold, sx, sy, ex, ey);
    if (res == null) {
        return null;
    }
    let x1 = [];
    for (let i = 0; i < res.length; i++) {
        x1.push(new android.graphics.Point(res[i]));
    }
    return x1;


}





/**
全分辨率模板匹配
@权限 普通权限
@参数 img - 大图 @参数 temp - 小图 @参数  oWidth - 制作小图时的屏幕宽度 @参数 oHeight - 制作小图时的屏幕高度 @参数 threshold - 匹配度 默认0.9 @参数 sx - 起点x 默认0 @参数 sy - 起点y 默认0 @参数 ex - 终点x 默认0 @参数 ey - 终点y 默认0 @参数 count - 寻找个数 默认 1 @参数 scalePro -  每次缩放比例, 默认0.05,即百分之五,程序根据分辨率自动放大或缩小,如果为负数将执行相反的过程
@返回值 成功:RectEx[] 失败:null
*/
image.findImgByPyramid = function (img, temp, oWidth, oHeight, threshold, sx, sy, ex, ey, count, scalePro) {
    sx = sx || 0;
    sy = sy || 0;
    ex = ex || 0;
    ey = ey || 0;
    threshold = threshold || 0.9;
    scalePro = scalePro || 0.05;
    count = count || 1;
    let num1 = Math.min(device.getScreenWidth(), device.getScreenHeight());
    let num2 = Math.min(oWidth, oHeight);
    if (num1 < num2) {
        scalePro *= -1;
        let temp = num1;
        num1 = num2;
        num2 = temp;
    }
    let scaleTimes = Math.abs(Math.ceil((num1 - num2) / (num1 * scalePro))) + 1;
    let rects = image_cls.FindImgByPyramid(img, temp, sx, sy, ex, ey, 3, threshold - 0.1, threshold, count, scaleTimes, scalePro);
    if (rects == null) { return null; }
    let d = [];
    for (let i = 0; i < rects.length; i++) {
        d.push(new RectEx(rects[i]));
    }
    return d;
}




/**
同分辨率-轮廓找图
@权限 普通权限
@参数 img - 大图 @参数 temp - 小图 @参数  resize - 大图缩放倍数(适当缩放有利于速度提升 例如 1表示不缩放 0.5表示缩小一倍 默认0.5) @参数 threshold - 匹配度 0~1 默认0.95 @参数 sx - 起点x 默认0 @参数 sy - 起点y 默认0 @参数 ex - 终点x 默认0 @参数 ey - 终点y 默认0 @参数 count - 寻找个数 默认 1 @参数 weakThreshold -  检测轮廓(canny)时的弱阈值 大于弱阈值且依附在边缘的点会被保留  默认值 50 @参数 strongThreshold -  检测轮廓(canny)时的强阈值 即相邻的像素点差值大于强阈值则被认为是边缘 默认值 100
@返回值 成功:RectEx[] 失败:null
*/
image.findImgByContour = function (img, temp, resize, threshold, sx, sy, ex, ey, count, weakThreshold, strongThreshold) {
    resize = resize || 0.5;
    threshold = threshold || 0.9;
    sx = sx || 0;
    sy = sy || 0;
    ex = ex || 0;
    ey = ey || 0;
    count = count || 1;
    weakThreshold = weakThreshold || 50;
    strongThreshold = strongThreshold || 100;
    let rects = image_cls.findImgByContour(img, temp, resize, threshold, sx, sy, ex, ey, count, weakThreshold, strongThreshold)
    if (rects == null) { return null; }
    let d = [];
    for (let i = 0; i < rects.length; i++) {
        d.push(new RectEx(rects[i]));
    }
    return d;
}



/**
同分辨率-半透明找图
@特殊说明 背景最好为纯色,如果是杂色,请涂成纯色
@权限 普通权限
@参数 mat - 大图 @参数 temp - 小图 @参数  resize - 大图缩放倍数(适当缩放有利于速度提升 例如 1表示不缩放 0.5表示缩小一倍 默认0.5) @参数 threshold - 匹配度 0~1 默认0.95 @参数 sx - 起点x 默认0 @参数 sy - 起点y 默认0 @参数 ex - 终点x 默认0 @参数 ey - 终点y 默认0 @参数 count - 寻找个数 默认 1 @参数 colorOffset -  背景颜色偏差 默认 20 个点
@返回值 成功:RectEx[] 失败:null
*/
image.findTranslucentImg = function (mat, temp, resize, threshold, sx, sy, ex, ey, count, colorOffset) {
    resize = resize || 0.5;
    threshold = threshold || 0.95;
    sx = sx || 0;
    sy = sy || 0;
    ex = ex || 0;
    ey = ey || 0;
    count = count || 1;
    colorOffset = colorOffset || 10;
    let rects = image_cls.findTranslucentImg(mat, temp, resize, threshold, sx, sy, ex, ey, count, colorOffset)
    if (rects == null) { return null; }
    let d = [];
    for (let i = 0; i < rects.length; i++) {
        d.push(new RectEx(rects[i]));
    }
    return d;
}


/**
图片二值化
@权限 普通权限
@参数 mat - 图片 @参数 threshold_value - 二值化阈值，0 ~ 255 @maxValue  大于阈值的点变成的数值 默认 255 白色 @参数 type - 一般写0即可 默认0<br/>
 0 灰度值大于阈值为最大值，其他值为0<br/>
 1 灰度值大于阈值为0，其他值为最大值<br/>
 2 灰度值大于阈值的为阈值，其他值不变<br/>
 3 灰度值大于阈值的不变，其他值为0<br/>
 4 灰度值大于阈值的为零，其他值不变<br/>
 8 大津法自动寻求全局阈值<br/>
 16 三角形法自动寻求全局阈值<br/>
@返回值 成功:Bitmap对象 失败:null
*/
image.BinaryzationMat = function (mat, threshold_value, maxValue, type) {
    if (typeof (maxValue) != "number") { maxValue = 255; }
    type = type || 0;
    return image_cls.BinaryzationMat(mat, threshold_value, maxValue, type);
}



/**
将inputStream类型的文图像转换为Bitmap
@权限 普通权限
@参数 inputStream - inputStream对象
@返回值 成功:Bitmap对象 失败:null
*/
image.getBitmapFromInputStream = function (inputStream) {

    return image_cls.getBitmapFromInputStream(inputStream)

}



/** @type {释放相关} */
const release = {}
var release_cls= magichands.core.OpenApi.release



/**
关闭InputStream对象
@权限 普通权限
@参数 b - InputStream对象
@返回值 成功:true 失败:false
*/
release.closeInputStream = function (b) {
    release_cls.closeInputStream(b)
}


/**
释放bit对象
@权限 普通权限
@参数 b - Bit对象
@返回值 成功:true 失败:false
*/
release.bitRecycle = function (b) {

    release_cls.bitRecycle(b)
}



/** @type {颜色相关} */
const color = {}
var color_cls= magichands.core.OpenApi.color

/**
获取坐标点的Rgb值
@权限 普通权限
@参数 b - Bit对象 @参数 x - x坐标 @参数 y - y坐标
@返回值 成功:rgb字符串 失败:null
*/
color.getRgb = function (b, x, y) {
    return color_cls.getRgb(b, x, y)
}

/**
获取坐标点的R通道-红色
@权限 普通权限
@参数 b - Bit对象 @参数 x - x坐标 @参数 y - y坐标
@返回值 成功:R通道的颜色数 失败:null
*/
color.getR = function (b, x, y) {
    return color_cls.getR(b, x, y)
}

/**
获取坐标点的G通道-绿色
@权限 普通权限
@参数 b - Bit对象 @参数 x - x坐标 @参数 y - y坐标
@返回值 成功:G通道的颜色数 失败:null
*/
color.getG = function (b, x, y) {
    return color_cls.getG(b, x, y)
}

/**
获取坐标点的B通道-蓝色
@权限 普通权限
@参数 b - Bit对象 @参数 x - x坐标 @参数 y - y坐标
@返回值 成功:B通道的颜色数 失败:null
*/
color.getB = function (b, x, y) {
    return color_cls.getB(b, x, y)
}





/**
同分辨-单点比色
@权限 普通权限
@参数 r - 第一个点的r @参数 g - 第一个点的g @参数 b - 第一个点的b @参数 r2 - 第二个点的r @参数 g2 - 第二个点的g @参数 b3 - 第二个点的b @参数 threshold - 相似度
@返回值 成功:B通道的颜色数 失败:null
*/
color.cmpColor = function (r, g, b, r2, g2, b2, threshold) {
    threshold = threshold || 0.72;
    r3 = (r - r2) / 256
    g3 = (g - g2) / 256
    b3 = (b - b2) / 256
    let diff = 1 - Math.sqrt(r3 * r3 + g3 * g3 + b3 * b3)
    // logd(diff);
    if (parseFloat(diff) > parseFloat(threshold)) {
        return true
    }
    return false
}




/**
同分辨-多点找色
@权限 普通权限
@参数 img - 图片 @参数 firstColor - 第一个点的颜色 例如 "0xCDD7E9-0x101010" "#00FF00" @参数 colorMulti - 字符串类似这样 "6|1|0x969696-0x000010,1|12|0x969696,-4|0|0x969696" @参数 threshold - 找色时颜色相似度取值为 0.0 ~ 1.0 @参数 sx - 起点x 默认0 @参数 sy - 起点y 默认0 @参数 ex - 终点x 默认0 @参数 ey - 终点y 默认0  @参数 count - 寻找的个数 默认1 @参数 direction - 寻找的方向 1~8 默认1  <br/>
 1: → 从左上横着找到右下 (默认方式)  <br/>
 2: ← 从右上横着找到左下  <br/>
 3: ↓ 从右上竖直找到左下  <br/>
 4: ↑ 从右下竖直找到左上  <br/>
 5: ← 从右下横着找到左上  <br/>
 6: → 从左下横着找到右上  <br/>
 7: ↑ 从左下竖直找到右上  <br/>
 8: ↓ 从左上竖直找到右下  <br/>
@返回值 成功:Point[] 失败:null
*/

color.findMultiColor = function (img, firstColor, colorMulti, threshold, sx, sy, ex, ey, count, direction) {
    threshold = threshold || 0.9;
    sx = sx || 0;
    sy = sy || 0;
    ex = ex || 0;
    ey = ey || 0;
    count = count || 1;
    direction = direction || 1;
    let res = color_cls.findMultiColor(img, firstColor, colorMulti, threshold, sx, sy, ex, ey, count, direction)
    if (res == null) {
        return null;
    }
    let x1 = [];
    for (let i = 0; i < res.length; i++) {
        x1.push(new android.graphics.Point(res[i]));
    }
    return x1;
}






/** @type {文件相关} */
const file = {}
var file_cls= magichands.core.OpenApi.file


/**
判断指定路径的文件是否存在
@权限 储存权限
@参数 filePath - 文件路径
@返回值 成功:true 失败:false
*/
file.isFileExists = function (filePath) {

    return Bo(file_cls.isFileExists(filePath), true)
}

/**
判断指定路径的文件夹是否存在
@权限 储存权限
@参数 dirPath - 文件夹路径
@返回值 成功:true 失败:false
*/
file.isDirectoryExists = function (dirPath) {

    return Bo(file_cls.isDirectoryExists(dirPath), true)
}

/**
读取指定文件中所有行的内容
@权限 储存权限
@参数 filePath - 文件路径
@返回值 成功:字符串数组 失败:null
*/
file.readLinesFromFile = function (filePath) {
    return file_cls.readLinesFromFile(filePath)
}

/**
读取指定文件中指定行的内容
@权限 储存权限
@参数 dirPath - 文件夹路径 @参数 lineNumber - 指定行号
@返回值 成功:字符串 失败:null
*/
file.readLineFromFile = function (filePath, lineNumber) {
    return file_cls.readLineFromFile(filePath, lineNumber)
}

/**
删除指定文件夹
@权限 储存权限
@参数 folderPath - 文件夹路径
@返回值 成功:true 失败:false
*/
file.deleteFolder = function (folderPath) {

    return Bo(file_cls.deleteFolder(folderPath), true)
}

/**
创建文件夹
@权限 储存权限
@参数 folderPath - 文件夹路径
@返回值 成功:true 失败:false
*/
file.createFolder = function (folderPath) {

    return Bo(file_cls.createFolder(folderPath), true)
}
/**
获取指定文件夹下所有文件名的列表
@权限 储存权限
@参数 folderPath - 文件夹路径
@返回值 成功:文件名列表字符串 失败:null
*/
file.listFolderFiles = function (folderPath) {
    return file_cls.listFolderFiles(folderPath)
}

/**
追加写入内容
@权限 储存权限
@参数 filePath - 文件夹路径 @参数 content - 要写入的内容
@返回值 成功:true 失败:false
*/
file.appendTextToFile = function (filePath, content) {

    return Bo(file_cls.appendTextToFile(filePath, content), true)
}


/**
覆盖写入内容
@权限 储存权限
@参数 filePath - 文件夹路径 @参数 content - 要写入的内容
@返回值 成功:true 失败:false
*/
file.writeTextToFile = function (filePath, content) {

    return Bo(file_cls.writeTextToFile(filePath, content), true)
}

/**
删除文件
@权限 储存权限
@参数 filePath - 文件路径
@返回值 成功:true 失败:false
*/
file.deleteFile = function (filePath) {

    return Bo(file_cls.deleteFile(filePath), true)
}


/**
创建文件
@权限 储存权限
@参数 filePath - 文件夹路径
@返回值 成功:true 失败:false
*/
file.createNewFile = function (filePath) {

    return Bo(file_cls.createNewFile(filePath), true)
}




/**
删除指定文件中指定行的内容
@权限 储存权限
@参数 filePath - 文件路径 @参数 lineNumber - 行号
@返回值 成功:true 失败:false
*/
file.deleteLine = function (filePath, lineNumber) {

    return Bo(file_cls.deleteLine(filePath, lineNumber), true)
}


/**
替换文件中的指定字符串为另一个字符串
@权限 储存权限
@参数 filePath - 文件路径 @参数 oldString - 原字符 @参数 newString - 新字符
@返回值 成功:true 失败:false
*/
file.replaceString = function (filePath, oldString, newString) {

    return Bo(file_cls.replaceString(filePath, oldString, newString), true)
}


/**
在给定文件中查找指定的文本，并返回第一次出现的行号。如果没有找到文本，则返回-1
@权限 储存权限
@参数 filePath - 文件路径 @参数 searchText - 要查找的文本
@返回值 成功:对应行号 失败:-1
*/
file.findLineNumber = function (filePath, searchText) {
    return file_cls.findLineNumber(filePath, searchText)
}

/**
获取assets的文件对象
@权限 普通权限
@参数 con - 上下文 @参数 filePath - 路径
@返回值 成功:InputStream对象 失败:null
*/
file.getAssetFile = function (filePath, con) {
    con = con || getCon()
    return file_cls.getAssetFile(con, filePath)
}


/**
保存inputStream类型的文件到本地
@权限 普通权限
@参数 filePath - 本地路径 @参数 inputStream - inputStream对象
@返回值 成功:true 失败:false
*/
file.saveInputStreamToFile = function (filePath, inputStream) {

    return Bo(file_cls.saveInputStreamToFile(filePath, inputStream), true)
}


/**
将本地文件转换成InputStream对象
@权限 普通权限
@参数 file - 文件路径
@返回值 成功:InputStream对象 失败:null
*/
file.readFileInputStream = function (file) {
    return file_cls.readFileInputStream(file)
}

/**
保存bit对象到指定路径
@权限 储存权限
@参数 b - Bit对象 @参数 q - 保存质量(0-100) @参数 f - 保存的路径
@返回值 成功:true 失败:false
*/
file.saveBitmapToSDCard = function (b, q, f) {

    return Bo(file_cls.saveBitmapToSDCard(b, q, f), true)
}

/** @type {socket相关} */
const socket = {}
var socket_cls= magichands.core.OpenApi.socket
/**
webSocket-客户端
@权限 网络权限
@参数 ip - 服务端地址 @参数  callback 事件回调
@返回值 成功:webSocketClient对象 失败:null
*/
socket.webSocketClient=function(ip,callback){
return socket_cls.webSocketClient(ip,callback)
}


/**
webSocket-服务端
@权限 网络权限
@参数 port - 服务端端口 @参数  callback 事件回调
@返回值 成功:webSocketServer对象 失败:null
*/
socket.webSocketServer=function(port,callback){
return socket_cls.webSocketServer(port,callback)
}



/** @type {http相关} */
const http = {}
var http_cls= magichands.core.OpenApi.http


/**
get请求-不带文件
@权限 网络权限
@参数 url - 请求的网址
@返回值 成功:请求响应 失败:null
*/

http.sendGetRequest = function (url) {
    return http_cls.sendGetRequest(url)
}


/**
get请求-带文件
@权限 网络权限
@参数 url - 请求的网址  @参数 key - 文件参数 @参数 f - 文件
@返回值 成功:请求响应 失败:null
*/

http.sendGetRequestWithFile = function (url, key, f) {
    return http_cls.sendGetRequestWithFile(url, key, f)
}

/**
post请求-不带文件
@权限 网络权限
@参数 url - 请求的网址 @参数 j - 参数(json对象)
@返回值 成功:请求响应 失败:null
*/

http.sendPostRequest = function (url, j) {
    return http_cls.sendPostRequest(url, object2JsonString(j))
}

/**
post请求-带文件
@权限 网络权限
@参数 url - 请求的网址 @参数 j - 参数(json对象) @参数 key - 文件参数  @参数 f - 文件路径
@返回值 成功:请求响应 失败:null
*/

http.sendPostRequestWithFile = function (url, j, key, f) {
    return http_cls.sendPostRequestWithFile(url, object2JsonString(j), key, f)
}

/** @type {设备相关} */
const device = {}
var device_cls= magichands.core.OpenApi.device









/**
打开无障碍设置页面
*/
device.openAccessibilitySettings = function () {
     device_cls.accessibilitySettings()
}


/**
判断无障碍权限
@返回值 成功:true 失败:false
*/
device.checkAccessibilityPermissions = function () {
    return device_cls.accessibilitySettings()
}



/**
获取本机IMEI
@权限 通话权限(<android10)
@返回值 成功:imei字符串 失败:null
*/
device.getImei = function () {
    return device_cls.getImei()
}
/**
获取本机ID
@返回值 成功:androidId字符串 失败:null
*/
device.getAndroidID = function () {
    return device_cls.getAndroidID()
}

/**
获取本机数字版权管理ID
@返回值 成功:数字版权管理ID字符串 失败:null
*/
device.getWidevineID = function () {
    return device_cls.getWidevineID()
}

/**
获取本机伪造ID(根据硬件信息生成)
@返回值 成功:伪造ID字符串 失败:null
*/
device.getPseudoID = function () {
    return device_cls.getPseudoID()
}

/**
获取GUID(随机生成)
@返回值 成功:GUID字符串 失败:null
*/
device.getGUID = function () {
    return device_cls.getGUID()
}

/**
获取OAID/AAID
国内手机厂商的开放匿名标识（OAID）、海外手机平台的安卓广告标识（AAID）
@返回值 成功:OAID/AAID字符串 失败:null
*/
device.getOAID = function () {
    return device_cls.getOAID()
}


/**
获取本机wifi-IP地址
@权限 普通权限
@参数 con - Context对象,不传入使用默认对象
@返回值 成功:ip字符串 失败:null
*/
device.getLocalIPAddress = function (con) {
    con = con || getCon()
    return device_cls.getLocalIPAddress(con)
}

/**
获取外网IP地址
@权限 普通权限
@返回值 成功:ip字符串 失败:null
*/
device.getPublicIPAddress = function () {
    return device_cls.getPublicIPAddress()
}


/**
获取当前位置信息
@权限 普通权限
@参数 con - Context对象,不传入使用默认对象
@返回值 成功:经纬度信息字符串 失败:null
*/
device.getCurrentLocation = function (con) {
    con = con || getCon()
    return device_cls.getCurrentLocation(con)
}

/**
获取当前android版本
@权限 普通权限
@返回值 成功:版本信息字符串 失败:null
*/
device.getAndroidVersion = function () {
    return device_cls.getAndroidVersion()
}
/**
获取状态栏高度
@权限 普通权限
@参数 con - Context对象,不传入使用默认对象
@返回值 成功:状态栏高度信息 失败:null
*/
device.getStatusBarHeight = function (con) {
    con = con || getCon()
    return device_cls.getStatusBarHeight(con)
}
/**
获取导航栏高度
@权限 普通权限
@参数 con - Context对象,不传入使用默认对象
@返回值 成功:导航栏高度信息 失败:null
*/
device.getNavigationBarHeight = function (con) {
    con = con || getCon()
    return device_cls.getNavigationBarHeight(con)
}

/**
获取当前屏幕高度
@权限 普通权限
@参数 con - Context对象,不传入使用默认对象
@返回值 成功:屏幕高度信息 失败:null
*/
device.getScreenHeight = function (con) {
    con = con || getCon()
    return device_cls.getScreenHeight(con)
}

/**
获取当前屏幕宽度
@权限 普通权限
@参数 con - Context对象,不传入使用默认对象
@返回值 成功:屏幕宽度信息 失败:null
*/
device.getScreenWidth = function (con) {
    con = con || getCon()
    return device_cls.getScreenWidth(con)
}

/**
获取当前屏幕dpi
@权限 普通权限
@参数 con - Context对象,不传入使用默认对象
@返回值 成功:屏幕dpi信息 失败:null
*/
device.getScreenDpi = function (con) {
    con = con || getCon()
    return device_cls.getScreenDpi(con)
}

/**
获取当前设备时间的时间戳
@权限 普通权限
@返回值 成功:当前设备时间戳 失败:null
*/
device.getCurrentTimestamp = function () {
    return device_cls.getCurrentTimestamp()
}


/**
获取剪切板内容
@权限 普通权限
@返回值 剪切板的内容
*/
device.getClipboardContent = function () {
    return device_cls.getClipboardContent()
}



/**
设置剪切板内容
@权限 普通权限
@参数 str - 要设置的文本
@返回值 成功:true 失败:false
*/
device.setClipboardContent = function (str) {
    return Bo(device_cls.setClipboardContent(str), true)
}


/** @type {时间相关} */
const time = {}
var time_cls= magichands.core.OpenApi.time
/**
时间戳转换标准时间
@权限 普通权限
@参数 t - 时间戳 @参数 p - 转换格式 (格式为"yyyy-MM-dd HH:mm:ss")
@返回值 成功:标准时间信息 失败:null
*/
time.timestampToStandardTime = function (t, p) {
    return time_cls.timestampToStandardTime(t, p)
}
/** @type {解压缩相关} */
const zip = {}
var zip_cls= magichands.core.OpenApi.zip

/**
压缩一个文件或者文件夹
@权限 普通权限
@参数 srcFilePath - 压缩路径 @参数 destFilePath - 压缩后路径   @参数 password - 密码
@返回值 成功:true 失败:false
*/
zip.compress = function (srcFilePath, destFilePath, password) {

    return Bo(zip_cls.compress(srcFilePath, destFilePath, password), true)
}


/**
解压一个压缩包
@权限 普通权限
@参数 zipFilePath - 压缩包路径 @参数 destFilePath - 解压后路径 @参数 password - 解压密码
@返回值 成功:true 失败:false
*/
zip.decompress = function (zipFilePath, destFilePath, password) {

    return Bo(zip_cls.decompress(zipFilePath, destFilePath, password), true)
}
/** @type {ui相关} */
const ui = {}
var ui_cls= magichands.core.OpenApi.ui
ui.xml = {}
var ui_xml_cls= magichands.core.OpenApi.ui.xml
ui.vue = {}
var ui_vue_cls= magichands.core.OpenApi.ui.vue
/**
assets/sd 解析指定的布局文件
@权限 普通权限
@参数 con - Context对象,不传入使用默认对象 @参数 layoutName - assets里的xml文件路径
@返回值 成功:view对象 失败:null
*/
ui.xml.parseLayout = function (layoutName, con) {
    con = con || getCon()
    return ui_xml_cls.parseLayout(con, layoutName)
}



/**
展示view
@权限 普通权限
@参数 view - view对象 @参数 act - Activity对象,不传入使用默认对象
*/
ui.xml.showViewOnScreen = function (view, act) {
    act = act || getAct()
    ui_xml_cls.showViewOnScreen(view, act)
}

/**
设置 Spinner
@权限 普通权限
@参数 spinner - spinner对象
*/
ui.xml.setOnItemSelectedListener = function (spinner) {
    ui_xml_cls.setOnItemSelectedListener(spinner)
}

/**
获取对应id的spinner对象
@权限 普通权限
@参数 id - spinner的id
@返回值 成功:spinner对象信息 失败:null
*/
ui.xml.getSpinner = function (id) {
    return ui_xml_cls.getSpinner(id)
}

/**
获取spinner控件中选中项的名称
@权限 普通权限
@返回值 成功:选中的选项名称信息 失败:null
*/
ui.xml.getSpinnerName = function () {
    return ui_xml_cls.getSpinnerName()
}

/**
获取editText输入的信息
@权限 普通权限
@参数 editText - editText对象
@返回值 成功:输入的信息 失败:null
*/
ui.xml.getText = function (editText) {
    return ui_xml_cls.editText(editText)
}

/**
设置editText文本信息
@权限 普通权限
@参数 editText - editText对象 @参数 text - 设置内容
@返回值 成功:输入的信息 失败:null
*/
ui.xml.setText = function (editText,text) {
     ui_xml_cls.setText(editText,text)
}

/**
获取对应id的editText对象
@权限 普通权限
@参数 id - editText的id
@返回值 成功:editText对象信息 失败:null
*/
ui.xml.getEditText = function (id) {
    return ui_xml_cls.getEditText(id)
}


/**
获取对应id的button对象
@权限 普通权限
@参数 id - button的id
@返回值 成功:button对象信息 失败:null
*/
ui.xml.getButton = function (id) {
    return ui_xml_cls.getButton(id)
}

/**
设置按钮监听事件
@权限 普通权限
@参数 b - 按钮对象 @参数 apc - 上下文
*/
ui.xml.setOnClickListener = function (b, apc) {
    apc = apc || getApc()
    ui_xml_cls.setOnClickListener(b, apc)
}
/**
监听Xml按钮点击
@权限 普通权限
@参数 callback - 回调
*/
ui.xml.onClick=function (callback) {
    ui_xml_cls.onClick(callback)
 }
/**
获取Vue界面指定id值
@权限 普通权限
@返回值 成功:对应值 失败:null
*/
ui.vue.getVueValue = function () {
    return ui_vue_cls.getVueValue()
}
/**
监听Vue按钮点击
@权限 普通权限
@参数 callback - 回调
*/
ui.vue.onClick=function (callback) {
    ui_vue_cls.onClick(callback)
}


/** @type {线程相关} */
const thread = {}

/**
启动线程
@权限 普通权限
@参数 myRunnable - Runnable对象
@返回值 成功:线程id 失败:null
*/
thread.execAsync = function (myRunnable) {
    let th = new java.lang.Thread(myRunnable);
    th.start();
    return th;
}



/** @type {窗口相关} */
const windows = {}
var windows_cls= magichands.core.OpenApi.windows

/**
申请悬浮窗权限
@权限 普通权限
*/
windows.requestFloatWindowPermission = function () {

    windows_cls.requestFloatWindowPermission()

}
/**
判断悬浮窗权限
@权限 普通权限
@返回值 成功:true 失败:false
*/
windows.canWindows = function () {

    return Bo(windows_cls.canWindows(), true)
}
/**
结束日志悬浮窗不可触摸状态
@权限 普通权限
*/
windows.stop = function () {

    windows_cls.stop()
}
/**
清除日志悬浮窗
@权限 普通权限
*/
windows.clear = function () {

    windows_cls.clear()
}


/**
展示日志悬浮窗
@权限 悬浮窗权限
*/

windows.show = function () {
    windows_cls.show()
}



/**
隐藏日志悬浮窗
@权限 普通权限
*/
windows.hide = function () {

    windows_cls.hide()

}



/** @type {插件相关} */
const plugins = {}
var plugins_cls= magichands.core.OpenApi.plugins
/**
判断内存有无类
@权限 普通权限
@参数 cls - 全类名
*/
plugins.isClass = function (cls) {

    return Bo(plugins_cls.isClass(cls), true)
}



/**
加载apk
@权限 储存权限
@参数 path - 路径
*/
plugins.loadApk = function (path) {
    plugins_cls.loadApk(path)
}

/**
启动python
@权限 普通权限
@参数 name - 模块名
*/
plugins.startPy = function (name) {
    plugins_cls.startPy(name)
}


/**
调用python方法
@权限 普通权限
@参数 arguments arguments[0] - 文件名 arguments[1] - 方法名 arguments[...] - 参数名
@返回值 成功:对应方法的返回值 失败:null
*/
plugins.invokePy = function () {
    switch (arguments.length) {
        case 2:
             return plugins_cls.invokePy(arguments[0], arguments[1]);
        case 3:
            return plugins_cls.invokePy(arguments[0], arguments[1],arguments[2]);
        case 4:
            return plugins_cls.invokePy(arguments[0], arguments[1],arguments[2], arguments[3]);
        case 5:
            return plugins_cls.invokePy(arguments[0], arguments[1],arguments[2], arguments[3],arguments[4]);
        case 6:
             return plugins_cls.invokePy(arguments[0], arguments[1],arguments[2], arguments[3],arguments[4], arguments[5],arguments[6]);
        case 7:
            return plugins_cls.invokePy(arguments[0], arguments[1],arguments[2], arguments[3],arguments[4], arguments[5],arguments[6],arguments[7]);
        default:
           logd("","invokePy","意料之外");
    }

}






/** @type {输入法相关} */
const input = {}

var input_cls= magichands.core.OpenApi.input

/**
输入法输入
@权限 输入法权限
@参数 str - 要输入的文本
*/
input.InputMethodInput = function (str) {
    input_cls.InputMethodInput(str)
}
/**
输入法删除
@权限 输入法权限
*/
input.InputMethodDeleteText = function () {
    input_cls.InputMethodDeleteText()
}


/**
输入法回车
@权限 输入法权限
*/
input.InputMethodEnterKey = function () {
    input_cls.InputMethodEnterKey()
}


/** @type {shell相关} */
const shell = {}
var shell_cls= magichands.core.OpenApi.shell



/**
执行shell
@权限 root/adb/普通
@参数 cmd - 执行的命令
@返回值 成功:对应的执行结果 失败:不正确返回值
*/
shell.executeCommand = function (cmd) {
    return shell_cls.executeCommand(cmd)
}

/**
超级shell执行
@权限 电脑激活
@参数 cmd - adb命令
@返回值 成功:执行结果 失败:null
*/
shell.superShell = function (cmd) {
    return shell_cls.superShell(cmd)
}


/** @type {ai相关} */
const ai = {}
var ai_cls= magichands.core.OpenApi.ai
/** @type {ocr相关} */
ai.ocr = {}
var ai_ocr_cls= magichands.core.OpenApi.ai.ocr
/** @type {学习模型相关} */
ai.model = {}
var ai_model_cls= magichands.core.OpenApi.ai.model
/** @type {检测相关} */
ai.detect={}
var ai_detect_cls= magichands.core.OpenApi.ai.detect

/**
YoloV8图片检测
@权限 普通权限
@参数 bit - Bit对象
@返回值 成功:V8Obj[] 失败:null
*/
ai.detect.V8Detect =function(bit){

return ai_detect_cls.V8Detect(bit)

}


/**
YoloV8 模型和工作模式选择-assets加载
@权限 普通权限
@参数 path - 模型路径 @参数 labellist - 标签数组 @参数 current_cpugpu - cpu/gpu切换(0/1)
@返回值 成功:true 失败:false
*/
ai.detect.V8Load =function(path,labellist,current_cpugpu){

    return ai_detect_cls.V8Load(path,labellist,0,current_cpugpu)
    
    }
/**
YoloV8 检测速度
@权限 普通权限
@返回值 成功:速度/ms 失败:0.0
*/
ai.detect.V8speed =function(){

    return ai_detect_cls.V8speed()

    }

// /**
// YoloV8 cpu/gpu切换
// @权限 普通权限
// @参数 current_cpugpu - cpu/gpu切换(0/1)
// @返回值 成功:true 失败:false
// */
// ai.detect.V8CpuorGpu =function(current_cpugpu){
// current_model=-2
// return js.V8Load(current_model,current_cpugpu)
// }

// /**
// YoloV8 加载模型
// @权限 普通权限
// @参数 current_model - 模型选择(0-1)
// @返回值 成功:true 失败:false
// */
// ai.detect.V8Model =function(current_model){
// current_cpugpu=-2
// return js.V8Load(current_model,current_cpugpu)
// }

/**
YoloV8 绘制检测框信息
@权限 普通权限
@参数 objects - V8Obj[]对象 @参数 bit - Bit对象
@返回值 成功:bit对象 失败:null
*/
ai.detect.V8Draw =function(objects,bit){
return ai_detect_cls.V8Draw(objects,bit)
}




/**
OcrEngine
@权限 普通权限
@返回值 成功:AOrc对象 失败:null
*/
ai.ocr.OcrEngine = function () {
    let OcrEngine = ai_ocr_cls.OcrEngine();
    return {
        getResult: function () {
            return OcrEngine;
        },
        setPadding: function (Int) {
            return OcrEngine.setPadding(Int);
        },
        setBoxThresh: function (Float) {
            return OcrEngine.setBoxThresh(Float);
        },
        setUnClipRatio: function (Float) {
            return OcrEngine.setUnClipRatio(Float);
        },
        setDoAngle: function (Boolean) {
            return OcrEngine.setDoAngle(Boolean);
        },
        setMostAngle: function (Boolean) {
            return OcrEngine.setMostAngle(Boolean);
        },
        boxScoreThresh: function (Float) {
            return OcrEngine.boxScoreThresh(Float);
        },

        /**
          * 识别Ocr
          */
        detect: function (b, max) {
            let detect = OcrEngine.detect(b, b, max);
            return {

                /**
                        * 所有识别结果集合列表
                        */
                getTextBlocks: function () {
                    let getTextBlocks = detect.getTextBlocks();
                    return {
                        /**
                         * 返回原始对象
                         */
                        getResult: function () {
                            return getTextBlocks;
                        },

                        /**
                         * 返回指定下标集合
                         */
                        get: function (index) {
                            let get = getTextBlocks.get(index);
                            return {
                                /**
                   * 返回指定下标集合文本信息
                   */
                                getText: function () {

                                    return get.getText()
                                },
                            };

                        },

                    };
                },
                getResult: function () {
                    return detect;
                },

            }
        },
    };
}

/**
ddddocr
@权限 普通权限
*/
ai.ocr.ddddocr = function () {
    return {
        init:function(ocr,det,old,beta,use_gpu,device_id,show_ad,import_onnx_path,charsets_path){
            plugins_cls.invokePy("ocr", "ddddocrinit", ocr ,det,old,beta,use_gpu,device_id,show_ad,import_onnx_path,charsets_path)
        },
        classification:function(path){
          return  plugins.invokePy("ocr", "classification", path)
        },
        slide_comparison:function(path,path1){
            return  plugins.invokePy("ocr", "slide_comparison", path,path1)
        },
        slide_match:function(path,path1,simple_target){
            simple_target = simple_target || false
            return  plugins.invokePy("ocr", "slide_match", path,path1,simple_target)
        }


    }
}

/**
onnxruntime
@权限 普通权限
*/
ai.model.onnxruntime = function () {
    return {
        create:function(path){
          return  ai_model_cls.onnxcreate(path)
        },
        illation:function(in4,label){
            return  ai_model_cls.illation(in4,label)
        },

    }
}




/** @type {usb相关} */
const usb = {}
var usb_cls= magichands.core.OpenApi.usb




/**
设置Usb通信Ip
@权限 普通权限
@参数 ip - 电脑内网ip
*/
usb.setUsbIp = function (ip) {
    usb_cls.setUsbIp(ip)
}

/**
查找Usb设备
@权限 Usb权限
@参数 vid - 设备的vid pid - 设备的pid
@返回值 成功:true 失败:false
*/
usb.findUsb = function (uid,serial,vid, pid) {
    return Bo(true, JSON.parse(usb_cls.findUsb(uid,serial,vid, pid)).data)
}

/**
Usb初始化
@权限 Usb权限
@返回值 成功:true 失败:false
*/
usb.usbInit = function (uid) {

    return Bo(true, JSON.parse(usb_cls.usbInit(uid)).data)
}




/**
设置Usb设备分辨率
@权限 Usb权限
@参数 uid - 对应设备uid width - 设备宽度 height - 设备高度
@返回值 成功:true 失败:false
*/
usb.usbResolution = function (uid, width,height) {

    return Bo(true, JSON.parse(usb_cls.usbResolution(uid, width,height)).data)
}


/**
Usb长按
@权限 Usb权限
@参数 uid - 对应设备uid x - 点击x坐标 y - 点击y坐标 dur - 长按时长
@返回值 成功:true 失败:false
*/
usb.usbLongClickPoint = function (uid,x, y, dur) {

    return Bo(true, JSON.parse(usb_cls.usbLongClickPoint(uid,x, y, dur)).data)
}


/**
Usb点击
@权限 Usb权限
@参数 uid - 对应设备uid x - 点击x坐标 y - 点击y坐标
@返回值 成功:true 失败:false
*/
usb.usbClickPoint = function (uid,x, y) {

    return Bo(true, JSON.parse(usb_cls.usbClickPoint(uid,x, y)).data)
}

/**
Usb触摸长按
@权限 Usb权限
@参数 uid - 对应设备uid x - 按下x坐标 y - 按下y坐标
@返回值 成功:true 失败:false
*/
usb.usbTouchDown = function (uid,x, y) {

    return Bo(true, JSON.parse(usb_cls.usbTouchDown(uid,x, y)).data)
}

/**
Usb触摸滑动
@权限 Usb权限
@参数 uid - 对应设备uid x - 目标x坐标 y - 目标y坐标
@返回值 成功:true 失败:false
*/
usb.usbTouchMove = function (uid,x, y) {

    return Bo(true, JSON.parse(usb_cls.usbTouchMove(uid,x, y)).data)
}

/**
Usb触摸抬起
@权限 Usb权限
@参数 uid - 对应设备uid
@返回值 成功:true 失败:false
*/
usb.usbTouchUp = function (uid) {

    return Bo(true, JSON.parse(usb_cls.usbTouchUp(uid)).data)
}



/**
Usb滑动
@权限 Usb权限
@参数 uid - 对应设备uid x - 开始x y - 开始y sx - 目标x sy - 目标y
@返回值 成功:true 失败:false
*/
usb.usbSwipeToPoint = function (uid,x, y, sx, sy) {

    return Bo(true, JSON.parse(usb_cls.usbSwipeToPoint(uid,x, y, sx, sy)).data)
}

/**
Usb按键
@权限 Usb权限
@参数 uid - 对应设备uid key - 按键
@返回值 成功:true 失败:false
*/
usb.usbKeyBoard = function (uid,key) {
    return Bo(true, JSON.parse(usb_cls.usbKeyBoard(uid,key)).data)
}

/** @type {视频相关} */
const video = {}
var video_cls= magichands.core.OpenApi.video
/**
捕获h264实时裸流
@权限 Usb权限
@参数 key - 按键
@返回值 成功:true 失败:false
*/
video.captureH264 = function () {
    video_cls.captureH264()
}



//module.exports = {
//    globals,node,point,button,app,screenshot,image,release,color,file,http,device,time,zip,ui,thread,windows,plugins,input,shell,ai,usb,video
//};


