import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{count, row_number}
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.mutable.ListBuffer

object GetTopNData {
  def main(args: Array[String]): Unit = {
    // 创建spark对象
    val spark=SparkSession.builder().config("spark.sql.sources.partitionColumnTypeInference.enabled","false")
      .appName("GetTopNData")
      .master("local[2]").getOrCreate()

    // 打开文件:注意路径的写法
    val logDF=spark.read.format("parquet").load("file:///home/qingjiao/project/clean/")

    // 测试:打印schema信息
//    logDF.printSchema()

    // 测试:查看20条数据
//    logDF.show()

    // 测试：查看20条数据
//    topVideoDF.show()

    // 将数据插入数据库(根据访问次数统计TopN video)
//    insertDayTopVideo(spark,logDF)

    // 将数据插入数据库(area_top_videos)
//    insertAreaTopVideo(spark,logDF)

    // 将数据插入数据库(flow_top_videos)
    insertFlowTopVideo(spark,logDF)

    // 关闭spark
    spark.stop()
  }

  // 从第二次清洗后的parquet文件中按照视频访问次数进行查询，并将查询出的数据插入到MySQL中的day_top_videos数据表中
  def insertDayTopVideo(spark: SparkSession, logDF: DataFrame): Unit = {
    // 创建临时视图
    logDF.createOrReplaceTempView("logs")

    // 执行的sql语句,根据访问次数统计受欢迎的视频
    val sql="select day,num,count(1) as times from logs where category='video' and day='20170511' group by day,num "+
      "order by times desc"

    // 执行sql查询
    val topVideoDF = spark.sql(sql)

//    topVideoDF.show()
    try {
      // parquet文件是按照day进行分区的，对每个分区中的数据都进行处理，2.12后要在添加rdd否则会包value for each is not a member of Object的错误
      topVideoDF.rdd.foreachPartition(eachPartition=>{

        // 每个分区使用一个列表
        val list=new ListBuffer[CaseVideoIDTimes]
        eachPartition.foreach(content=>{
          // 获取分区中的day,num,times字段数据
          val day=content.getAs[String]("day")
          val video_id=content.getAs[Long]("num")
          val times=content.getAs[Long]("times")

          // 将获取到的数据封装为样例类并添加到list中
          list.append(CaseVideoIDTimes(day,video_id,times))
        })

        // 调用方法，将list中的数据插入到数据库
        DataToMySQL.insertDayTopVideos(list)
      })

    } catch {
      case e:Exception=>e.printStackTrace()
    }

  }

  // 从第二次清洗后的parquet文件中按照地区和视频访问次数进行查询，将查询出的数据插入到MySQL中的area_top_video数据表
  def insertAreaTopVideo(spark:SparkSession,logDF:DataFrame): Unit = {
    // 导入隐式转换
    import spark.implicits._

    // 使用函数的方式进行查询:查询当前日期的视频，按照日期、地区、视频id进行分组,使用分组聚合函数agg统计视频个数
    val areaTopVideo=logDF.filter($"day" === "20170511" && $"category" === "video")
      .groupBy("day", "region", "num")
      .agg(count("num").as("times"))

    // 测试:
//    areaTopVideo.show()

    // 配合窗口函数执行查询
    val resDF=areaTopVideo.select(
      areaTopVideo("day"),
      areaTopVideo("region"),
      areaTopVideo("num"),
      areaTopVideo("times"),

      // 窗口函数:row_number()从1-n的唯一序列号,选择Top3
      row_number().over(Window.partitionBy(areaTopVideo("region"))
      .orderBy(areaTopVideo("times").desc)).as("times_rank")
    ).filter("times_rank<=3")
      // 测试
//      .show(false)

    // 测试
//    resDF.printSchema()
//    resDF.show(false)

    // 调用函数将查询出的数据插入到数据库
    try {
      resDF.rdd.foreachPartition(eachPartition=>{
        val list=new ListBuffer[CaseVideoIDTimesRank]

        eachPartition.foreach(content=>{
          val day=content.getAs[String]("day")
          val num=content.getAs[Long]("num")
          val region=content.getAs[String]("region")
          val times=content.getAs[Long]("times")
          val times_rank=content.getAs[Int]("times_rank")

          list.append(CaseVideoIDTimesRank(day,region,num,times,times_rank))

        })

        // 调用函数
        DataToMySQL.insertAreaTopVideos(list)
      })
    } catch {
      case e:Exception=>e.printStackTrace()
    }

  }


  // 从第二次清洗后的parquet文件中按照视频总流量进行查询，将查询出的数据插入到MySQL中的flow_top_videos数据表
  def insertFlowTopVideo(spark:SparkSession,logDF:DataFrame): Unit = {
    // 创建临时视图
    logDF.createOrReplaceTempView("logs")

    // 要执行的sql语句
    val sql="select day,num,sum(flow) as flow from logs where day='20170511' and category='video'"+
    "group by day,num order by flow desc"

    // 执行sql查询
    val resDF=spark.sql(sql)

    // 测试
//    resDF.show(false)

    // 将DF数据插入到数据库
    try {
      resDF.rdd.foreachPartition(eachPartition=>{

        val list=new ListBuffer[CaseVideoIDFlow]
        eachPartition.foreach(content=>{
          val day=content.getAs[String]("day")
          val num=content.getAs[Long]("num")
          val flow=content.getAs[Long]("flow")

          list.append(CaseVideoIDFlow(day,num,flow))
        })

        // 调用函数执行插入数据库操作
        DataToMySQL.insertFlowTopVideos(list)

      })

    } catch {
      case e:Exception=>e.printStackTrace()
    }

  }


}
