import java.sql.{Connection, DriverManager, PreparedStatement}

object MySQLUtils {

  // 打开数据库连接
  def getConnection()={
    Class.forName("com.mysql.jdbc.Driver")
    // 指定utf-8编码方式，防止中文出现乱码
    DriverManager.getConnection("jdbc:mysql://localhost:3306/log_data?characterEncoding=UTF-8&user=dba&password=123456")
  }

  // 关闭连接
  def release(connection:Connection,state:PreparedStatement): Unit = {
    try {
      if (state!=null) {
        state.close()
      }
    }catch {
      case e:Exception=>e.printStackTrace()
    } finally {
      if (connection!=null) {
        connection.close()
      }
    }
  }

  // 测试
//  def main(args: Array[String]): Unit = {
//    println(getConnection())
//  }

}
