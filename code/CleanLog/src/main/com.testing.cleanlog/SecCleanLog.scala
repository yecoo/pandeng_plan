import org.apache.spark.sql.{SaveMode, SparkSession}

object SecCleanLog {
  def main(args: Array[String]): Unit = {

    // 创建spark对象
    val spark=SparkSession.builder().config("spark.sql.parquet.compression.codec","gzip")
      .appName("SecCleanLog")
      .master("local[2]").getOrCreate()

    // 打开第一次清洗后的日志文件
    val logRDD=spark.sparkContext.textFile("file:///home/qingjiao/project/access.log")

    // 将RDD转换为DF
    val logDF=spark.createDataFrame(logRDD.map(line=>SecParseLog.parseLog(line)),SecParseLog.schema)

    // 测试：查看schema信息
    logDF.printSchema()
    // 测试：查看DF数据20条
//    logDF.show()

    // 保存清洗后的日志，保存的格式采用parquet,为了兼容后期的数据(日志两比较大)，按照日期进行分区
    logDF.coalesce(1).write.format("parquet").partitionBy("day")
      .mode(SaveMode.Overwrite).save("file:///home/qingjiao/project/clean")

    // 关闭资源
    spark.stop()
  }


}
