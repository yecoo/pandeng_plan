object ParseURL {

  // 对url进行分离，分别获取访问的类型及编号
  def parse(url:String)={
    // 定义主站
    val site="http://www.imooc.com/"

    try {
      // 获取主站后的所有内容
      val content = url.substring(site.length).split("/")

      val category = content(0)

      // 编号
      val num = content(1)

      // 返回值
      category + "\t" + num

    } catch {
      case exception: Exception => {
        ""+"\t"+""
      }

    }

  }

  // 测试
//  def main(args: Array[String]): Unit = {
//    println(parse("http://www.imooc.com/code/1852"))
//  }

}
