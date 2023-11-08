package magichands.core.model.accessibility.obj

import android.app.Activity
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.util.Pair
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import cn.vove7.andro_accessibility_api.*
import cn.vove7.auto.core.api.*
import cn.vove7.auto.core.utils.AdapterRectF
import cn.vove7.auto.core.utils.AutoGestureDescription
import cn.vove7.auto.core.utils.GestureResultCallback
import cn.vove7.auto.core.viewfinder.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import magichands.core.model.accessibility.AppAccessibilityService

lateinit var ctx: AppCompatActivity;
var paramete: MutableList<Any>? = null;
var mainjob: Job? = null
infix fun <A, B> A.t(that: B): Pair<A, B> = Pair(this, that)
class privateApi {
    companion object {


        @JvmStatic
        fun e(ctxx: AppCompatActivity) {
            AccessibilityApi.init(
                ctxx,
                AppAccessibilityService::class.java
            )
            ctx = ctxx;
        }

    }
    /**
     * 尝试点击
     * */
    fun click(a:Any):Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add(a)
        return main(mainlist[8],  paramete!!)
    }


    fun globalLongClick(a:Any):Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add(a)
        return main(mainlist[9],  paramete!!)
    }


    fun globalClick(a:Any):Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add(a)
        return main(mainlist[10],  paramete!!)
    }
    fun longClick(a:Any):Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add(a)
        return main(mainlist[11],  paramete!!)
    }
    fun doubleClick(a:Any):Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add(a)
        return main(mainlist[12],  paramete!!)
    }
    fun tryLongClick(a:Any):Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add(a)
        return main(mainlist[13],  paramete!!)
    }
    fun select(a:Any):Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add(a)
        return main(mainlist[14],  paramete!!)
    }

    fun scrollUp ():Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add("a")
        return main(mainlist[17],  paramete!!)
    }
    fun scrollDown ():Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add("a")
        return main(mainlist[18],  paramete!!)
    }
    fun find ():Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add("a")
        return main(mainlist[19],  paramete!!)
    }


    fun textNodeRegular (a:Any):Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add(a)

        return main(mainlist[20],  paramete!!)
    }


    fun gesture (a:Any,b:Any):Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add(a)
        paramete!!.add(b)
        return main(mainlist[7],  paramete!!)
    }

    fun gestures (a:Any,b:Any):Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add(a)
        paramete!!.add(b)
        return main(mainlist[16],  paramete!!)
    }

    fun pairArray(x:Int,y:Int,x1:Int,y1:Int): Array<Pair<Int, Int>> {
      return  arrayOf(
            x t y,
            x1 t y1,
        )

    }
    fun pairArray(x:Int,y:Int,x1:Int,y1:Int,x2:Int,y2:Int): Array<Pair<Int, Int>> {
        return  arrayOf(
            x t y,
            x1 t y1,
            x2 t y2,
        )

    }




    fun pairArray(x:Int,y:Int,x1:Int,y1:Int,x2:Int,y2:Int,x3:Int,y3:Int): Array<Pair<Int, Int>> {
        return  arrayOf(
            x t y,
            x1 t y1,
            x2 t y2,
            x3 t y3,
        )

    }



    fun listpairArray(a:Any,b:Any): Array<Array<Pair<Int, Int>>> {

        return  arrayOf(
            a as Array<Pair<Int, Int>>,b as Array<Pair<Int, Int>>

        )

    }
    fun listpairArray(a:Any,b:Any,c:Any): Array<Array<Pair<Int, Int>>> {

        return  arrayOf(
            a as Array<Pair<Int, Int>>,b as Array<Pair<Int, Int>>,c as Array<Pair<Int, Int>>

        )

    }
    fun listpairArray(a:Any,b:Any,c:Any,d:Any): Array<Array<Pair<Int, Int>>> {

        return  arrayOf(
            a as Array<Pair<Int, Int>>,b as Array<Pair<Int, Int>>,c as Array<Pair<Int, Int>>,d  as Array<Pair<Int, Int>>

        )

    }







    fun exist(a:Any):Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add(a)
        return main(mainlist[15],  paramete!!)
    }
    fun tryClick(a:Any):Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add(a)
        return main(mainlist[0],  paramete!!)
    }
    fun delay(a:Any):Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add(a)
        return main(mainlist[1],  paramete!!)
    }
    fun openApp(a:Any):Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add(a)
        return main(mainlist[2],  paramete!!)
    }
    fun clickPoint(a:Any,b:Any):Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add(a)
        paramete!!.add(b)
        return main(mainlist[3],  paramete!!)
    }
    fun longClickPoint(a:Any,b:Any):Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add(a)
        paramete!!.add(b)
        return main(mainlist[4],  paramete!!)
    }
    fun swipeToPoint(a:Any,b:Any,c:Any,d:Any,e:Any):Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add(a)
        paramete!!.add(b)
        paramete!!.add(c)
        paramete!!.add(d)
        paramete!!.add(e)
        return main(mainlist[5],  paramete!!)
    }
    fun  containsswich(NodeInfo:Any,node:Any) :Any?{
        when (NodeInfo) {
//            "id" -> return SF.containsId(node.toString())

            "text" -> return SF.containsText(node.toString())

            "desc " -> return SF.containsDesc(node.toString())
        }
        return null;
    }
    fun  allswich(NodeInfo:Any,node:Any):Any? {
        when (NodeInfo) {
            "text" -> return SF.text(node.toString())

            "id" -> return SF.id(node.toString())

            "desc" ->return  SF.desc(node.toString())
        }
        return null
    }
//
//    fun  nodeCallChain():SmartFinder? {
//        return SF
//    }
//
//    fun  我是ssss(){
//         nodeCallChain()!!.text("1").desc("2");
//        nodeCallChain()!!.id("1").desc("2").node.ge
//        SF.id("1").desc("2");
//    }





    fun getText(a:Any):Any?{
        paramete =mutableListOf<Any>()
        paramete!!.add(a)
        return main(mainlist[6],  paramete!!)
    }



}

/**
 * 坐标点击函数
 * */
class clickPoint  : Main() {
    override val name: String get() = "坐标点击"
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
        if (a != null) {
           return click(a[0].toString().toFloat().toInt(), a[1].toString().toFloat().toInt())
        }else{
            return "传入参数错误"
        }
    }

}

class ViewFinderWithLambda : Main() {
    override val name: String
        get() = "ViewFinderWithLambda"

    override suspend fun run(any: Any, act: Activity): Any? {
        val w=findAllWith { call.m.CallbackMethod(it) ==true}
//        Log.d("长度", w.size.toString())
        return w
    }
}

class touchDown  : Main() {
    override val name: String get() = "坐标点击"
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
        if (a != null) {
            return click(a[0] as Int,  a[1] as Int)
        }else{
            return "传入参数错误"
        }
    }

}
/**
 * 坐标点击函数
 * */
class longClickPoint  : Main() {
    override val name: String get() = "坐标点击"
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
        if (a != null) {
            return longClick(a[0].toString().toFloat().toInt(), a[1].toString().toFloat().toInt())
        }else{
            return "传入参数错误"
        }
    }

}
class TextMatcherCondition(private val regex: String) : MatchCondition {
    //此处注意直接创建Regex，防止在搜索时重复创建；另外可直接检查正则表达式有效性
    private val reg = regex.toRegex()
    override fun invoke(node: AcsNode) =
        node.text?.toString()?.let {
            reg.matches(it)
        } ?: false
}
/**
 * 坐标点击函数
 * */
class textNodeRegular  : Main() {
    override val name: String get() = "坐标点击"
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
        if (a != null) {
          return  SF.where(TextMatcherCondition(a[0] as String)).findAll()

        }else{
            return "传入参数错误"
        }
    }

}

/**
 * 坐标点击函数
 * */
class scrollUp  : Main() {
    override val name: String get() = "坐标点击"
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        return scrollUp()

    }

}


/**
 * 坐标点击函数
 * */
class scrollDown  : Main() {
    override val name: String get() = "坐标点击"
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        return scrollDown()

    }

}
class getText  : Main() {
    override val name: String get() = "获取文本"
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
        if (a != null) {

            var firstElement: Any? = null
            firstElement=a[0]
            if (firstElement is ConditionGroup) {
                System.out.println("firstElement2:"+firstElement.findFirst())
                return firstElement.findFirst()
            } else {
                println("类型错误")
                return  false
            }
        }else{
            return "传入参数错误"
        }
    }

}
class exist  : Main() {
    override val name: String get() = "获取文本"
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
        if (a != null) {

            var firstElement: Any? = null
            firstElement=a[0]
            if (firstElement is ConditionGroup) {
                System.out.println("firstElement2:"+firstElement.findFirst())
                return firstElement.exist()
            } else {
                println("类型错误")
                return  false
            }
        }else{
            return "传入参数错误"
        }
    }

}
class swipeToPoint  : Main() {
    override val name: String get() = "坐标滑动"
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
        if (a != null) {

            return swipe(a[0].toString().toFloat().toInt(), a[1].toString().toFloat().toInt(),a[2].toString().toFloat().toInt(), a[3].toString().toFloat().toInt(), a[4].toString().toFloat().toInt())
        }else{
            return "传入参数错误"
        }
    }

}
class gestures  : Main() {
    override val name: String
        get() = "手势画图 - Rect - Circle - Oval"
    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        requireGestureAccessibility()
//        toast("1s后开始绘制，请不要触摸屏幕")
//        delay(1000)
        //设置相对屏幕 非必须
//        setScreenSize(500, 500)
        //指定点转路径手势
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
        return gestures(
            a!![0].toString().toFloat().toLong(), a!![1] as Array<Array<Pair<Int, Int>>>)

//        delay(800)
        //点击clear按钮
//        drawCircle()
//        delay(800)
//        drawOval()
//        delay(800)
//        drawCircleAsync()
//        return null;
    }
}



class gesture  : Main() {
    override val name: String
        get() = "手势画图 - Rect - Circle - Oval"
    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        requireGestureAccessibility()
//        toast("1s后开始绘制，请不要触摸屏幕")
//        delay(1000)
        //设置相对屏幕 非必须
//        setScreenSize(500, 500)
        //指定点转路径手势
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
     return gesture(
         a!![0].toString().toFloat().toLong(), a!![1] as Array<Pair<Int, Int>>)

//        delay(800)
        //点击clear按钮
//        drawCircle()
//        delay(800)
//        drawOval()
//        delay(800)
//        drawCircleAsync()
//        return null;
    }


    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun drawCircle() {
        val p = Path().apply {
            addOval(RectF(500f, 500f, 800f, 800f), Path.Direction.CW)
        }
        if (!gesture(2000L, p)) {
            toast("打断")
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun drawCircleAsync() {
        val p = Path().apply {
            addOval(RectF(500f, 500f, 800f, 800f), Path.Direction.CW)
        }
        gestureAsync(2000L, p, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: AutoGestureDescription?) {
                toast("gestureAsync 完成")
            }

            override fun onCancelled(gestureDescription: AutoGestureDescription?) {
                toast("gestureAsync 中断")
            }
        })
        delay(800)
        // 测试手势中断
        click(250, 250)
        delay(3000)
    }


    //AdapterRectF 会根据设置的相对屏幕大小换算
    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun drawOval() {
        val p = Path().apply {
            addOval(AdapterRectF(200f, 200f, 300f, 300f), Path.Direction.CW)
        }
        if (!gesture(2000L, p)) {
            toast("打断")
        }
    }



}
/**
 * 节点点击函数
 * */
class tryClick : Main() {
    override val name: String get() = "尝试点击"
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
        if (a != null) {
            var firstElement = a[0]
            if (firstElement is ConditionGroup) {
                System.out.println("firstElement:"+firstElement)
//                System.out.println("firstElement1:"+firstElement.findFirst())
                return firstElement.tryClick()
            }else {
                println("类型错误")
                return  false
            }
        }else{
            return "传入参数错误"
        }
    }

}


/**
 * 节点点击函数
 * */
class click : Main() {
    override val name: String get() = "点击"
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
        if (a != null) {
            var firstElement = a[0]
            if (firstElement is ConditionGroup) {
                System.out.println("firstElement:"+firstElement)
//                System.out.println("firstElement1:"+firstElement.findFirst())
                return firstElement.click()
            }else {
                println("类型错误")
                return  false
            }
        }else{
            return "传入参数错误"
        }
    }

}


/**
 * 节点点击函数
 * */
class globalLongClick : Main() {
    override val name: String get() = "全局点击"
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
        if (a != null) {
            var firstElement = a[0]
            if (firstElement is ConditionGroup) {
                System.out.println("firstElement:"+firstElement)
//                System.out.println("firstElement1:"+firstElement.findFirst())
                return firstElement.globalLongClick()
            }else {
                println("类型错误")
                return  false
            }
        }else{
            return "传入参数错误"
        }
    }

}

/**
 * 节点点击函数
 * */
class globalClick : Main() {
    override val name: String get() = "全局点击"
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
        if (a != null) {
            var firstElement = a[0]
            if (firstElement is ConditionGroup) {
                System.out.println("firstElement:"+firstElement)
//                System.out.println("firstElement1:"+firstElement.findFirst())
                return firstElement.globalClick()
            }else {
                println("类型错误")
                return  false
            }
        }else{
            return "传入参数错误"
        }
    }

}

/**
 * 节点点击函数
 * */
class longClick : Main() {
    override val name: String get() = "长点击"
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
        if (a != null) {
            var firstElement = a[0]
            if (firstElement is ConditionGroup) {
                System.out.println("firstElement:"+firstElement)
//                System.out.println("firstElement1:"+firstElement.findFirst())
                return firstElement.longClick()
            }else {
                println("类型错误")
                return  false
            }
        }else{
            return "传入参数错误"
        }
    }

}


/**
 * 节点点击函数
 * */
class doubleClick : Main() {
    override val name: String get() = "双点击"
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
        if (a != null) {
            var firstElement = a[0]
            if (firstElement is ConditionGroup) {
                System.out.println("firstElement:"+firstElement)
//                System.out.println("firstElement1:"+firstElement.findFirst())
                return firstElement.doubleClick()
            }else {
                println("类型错误")
                return  false
            }
        }else{
            return "传入参数错误"
        }
    }

}


/**
 * 节点点击函数
 * */
class tryLongClick : Main() {
    override val name: String get() = "尝试长点击"
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
        if (a != null) {
            var firstElement = a[0]
            if (firstElement is ConditionGroup) {
                System.out.println("firstElement:"+firstElement)
//                System.out.println("firstElement1:"+firstElement.findFirst())
                return firstElement.tryLongClick()
            }else {
                println("类型错误")
                return  false
            }
        }else{
            return "传入参数错误"
        }
    }

}


/**
 * 节点点击函数
 * */
class select : Main() {
    override val name: String get() = "尝试长点击"
    override suspend fun run(any: Any,act: Activity): Any? {
        requireBaseAccessibility(true)
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
        if (a != null) {
            var firstElement = a[0]
            if (firstElement is ConditionGroup) {
                System.out.println("firstElement:"+firstElement)
//                System.out.println("firstElement1:"+firstElement.findFirst())

                return firstElement.select()
            }else {
                println("类型错误")
                return  false
            }
        }else{
            return "传入参数错误"
        }
    }

}

/**
 * 休眠
 * */
class delay : Main() {
    override val name: String get() = "延迟"
    override suspend fun run(any: Any,act: Activity): Any? {
//        requireBaseAccessibility(true)
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
        if (a != null) {

            val xInt = Math.round(a[0].toString().toFloat())
            val longValue: Long = xInt.toLong()
            delay(longValue)
            return "无聊随便返回一下"
        }else{
            return "传入参数错误"
        }
    }

}
class waitAppAction : Main() {
    override val name: String
        get() = "打开app"

    override suspend fun run(any: Any,act: Activity): Any? {
        waitBaseAccessibility()
        var a: MutableList<Any>? =null
        a=any as MutableList<Any>?
        if (a != null) {
            toast("start chrome after 1s")
            delay(1000)
            val targetApp = a[0] as String
            act.startActivity(act.packageManager.getLaunchIntentForPackage(targetApp))
            return waitForApp(targetApp, 5000).also {
                toast("wait " + if (it) "success" else "failed")
            }
        }else{
            return "传入参数错误"
        }

    }
}
abstract class Main {
    abstract val name: String
    abstract suspend fun run( any:Any,act:Activity): Any?
    override fun toString() = name
}
var mainlist = mutableListOf(
    tryClick(),
    delay(),
    waitAppAction(),
    clickPoint(),
    longClickPoint(),
    swipeToPoint(),
    getText(),
    gesture (),
    click(),
    globalLongClick(),
    globalClick(),
    longClick(),
    doubleClick(),
    tryLongClick(),
    select(),
    exist(),
    gestures(),
    scrollUp(),
    scrollDown(),
    ViewFinderWithLambda(),
    textNodeRegular(),

    object : Main() {
        override val name = "Stop"
        override suspend fun run(any:Any,act: Activity) : Any?{
            return null
        }
    }
)
fun main(action: Main, any:Any):Any?{
    if (action.name == "Stop") {
        mainjob?.cancel()
        return null
    }
//    if (mainjob?.isCompleted.let { it != null && !it }) {
//        toast("有正在运行的任务")
//        return null
//    }
    var re: Any? = null
    mainjob = launchWithExpHandler {
        re = action.run(any, ctx)
    }
    mainjob?.invokeOnCompletion {
        toast("执行结束")
    }
    runBlocking {
        mainjob?.join() // 等待异步任务结束
    }
    return re // 返回修改后的 re 变量的值
}

