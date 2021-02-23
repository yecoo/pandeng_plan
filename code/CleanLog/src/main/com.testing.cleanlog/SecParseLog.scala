import com.ggstar.util.ip.IpHelper
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}

/*
工具类：
对清洗后的日志进行二次清洗，分离出url中的类型和id信息，并将ip地址解析为地理位置(省份),获取日期时间中的日期字段
 */
object SecParseLog {

  // 定义schema信息中字段
  val schemaField=Array(
    // 请求的url
    StructField("url",StringType),
    // 日期时间
    StructField("day",StringType),
    // 分类
    StructField("category",StringType),
    // 编号
    StructField("num",LongType),
    // 流量
    StructField("flow",LongType),
    // ip地址
    StructField("ip",StringType),
    // ip地址所在地区
    StructField("region",StringType)
  )

  // 定义StructType结构
  val schema=StructType(schemaField)

  // 解析清洗后的日志，并使用struct的方法，返回Row对象
  def parseLog(log:String)={

      // 转换每行数据 2017-05-11 14:09:14	http://www.imooc.com/video/4500	304	218.75.35.226
      try {
        val fields = log.split("\t")

        // 获取请求的url，第2个字段
        val url=fields(1)
        // 解析url，将分类信息及编号信息分离出来
        // 定义主站
        val site="http://www.imooc.com/"
        // 获取主站信息后的所有数据
        val content=url.substring(url.indexOf(site)+site.length)
        // 对获取到的数据按照/分隔符进行分割
        val typeID=content.split("/")
        // 定义初始的类型和id(可变)
        var category=""
        var num=0l
        if (typeID.length>1) {
          category=typeID(0)
          num=typeID(1).toLong
        }

        // 获取发送的字节数flow需要转换为long类型，第3个字段
        val flow=fields(2).toLong

        // 获取ip地址并将ip地址解析为地理位置，第4个字段
        val ip=fields(3)
        // 解析ip地址
        val region=IpHelper.findRegionByIp(ip)

        // 获取日期格式：20170511
        val day=fields(0).substring(0,10).replaceAll("-","")

        // 返回值Row对象：Row字段与Struct字段对应(注意：顺序要保持一致)
        Row(url,day,category,num,flow,ip,region)

      } catch {
        case e:Exception=>{
          Row("0","0","0",0l,0l,"0","0")
        }
      }

  }

  // 定义主函数，测试相关功能
//  def main(args: Array[String]): Unit = {
//    println(parseLog("2017-05-11 14:09:14\thttp://www.imooc.com/video/4500\t304\t218.75.35.226"))
//  }

}
