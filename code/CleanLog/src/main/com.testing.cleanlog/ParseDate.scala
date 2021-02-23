import org.apache.commons.lang3.time.FastDateFormat

import java.util.Locale

object ParseDate {
  // 使用FastDateFormat进行日期时间格式化
  // 定义源日期时间格式  10/Nov/2016:00:01:02 +0800
  val source=FastDateFormat.getInstance("dd/MMM/yyyy:HH:mm:ss Z",Locale.ENGLISH)

  // 定义格式化后的目标日期格式  2016-11-10 00:01:02
  val des=FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")

  // 对日期时间进行格式化
  def parse(date:String)={
    // 移除日期时间前后的[]
    val newDate=date.substring(date.indexOf("[")+1,date.lastIndexOf("]"))

    // 将字符串类型的日期时间转换为日期格式
    val sourceDate=source.parse(newDate)

    // 将源日期时间格式化
    des.format(sourceDate)
  }

  // 测试
//  def main(args: Array[String]): Unit = {
//    println(parse("[10/Nov/2016:00:01:02 +0800]"))
//  }

}
