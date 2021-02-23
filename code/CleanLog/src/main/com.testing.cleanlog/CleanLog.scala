import com.ggstar.util.ip.IpHelper
import org.apache.spark.sql.SparkSession

/*
将原始日志进行解析，获取请求的url，发送的字节数，请求的时间，发起请求的ip地址信息
 */
object CleanLog {

  def main(args: Array[String]): Unit = {
    // 创建spark对象
    val spark=SparkSession.builder().appName("CleanLog").master("local[2]").getOrCreate()

    // 打开本地文件
    val RDD=spark.sparkContext.textFile("file:///home/qingjiao/project/10000_access.log")

    // 获取需要的字段
    val logRDD=RDD.map(line=>{
      // 使用空格切分每行内容
      val fields = line.split(" ")
      try {
        // 获取IP地址
        val ip = fields(0)
        // 获取ip地址所在的区域
//        val region = IpHelper.findRegionByIp(ip)

        // 获取发送的字节数
        val flow = fields(9)

        // 获取请求的url地址
        val url = ParseURL.parse(fields(11).replaceAll("\"", ""))

        // 获取日期和时间并进行格式化
        val date = ParseDate.parse(fields(3) + " " + fields(4))

        // 返回值
        date + "\t" + url + "\t" + flow + "\t" + ip
      } catch {
        case e:Exception=>{
          0l
        }
      }
      // 查看清洗后的数据使用take().foreach(println)
      // saveAsTextFile()用来保存清洗后的日志，指定的是目录
    }).saveAsTextFile("file:///home/qingjiao/project/logs")

    // 关闭资源
    spark.stop()
  }

}
