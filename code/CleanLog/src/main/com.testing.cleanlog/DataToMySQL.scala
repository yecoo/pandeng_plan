import java.sql.{Connection, PreparedStatement}
import scala.collection.mutable.ListBuffer

object DataToMySQL {

  // 批量将每天的课程访问次数插入到数据库：list中存放的是要插入到数据库中的数据
  def insertDayTopVideos(list:ListBuffer[CaseVideoIDTimes]): Unit = {

    // 打开数据库连接
    var conn:Connection=null
    var state:PreparedStatement=null

    try {
      // 创建连接
      conn=MySQLUtils.getConnection()

      // 将事务设置为手动提交(默认为自动提交)
      conn.setAutoCommit(false)

      // 执行的sql语句:第一个问号代表day，第二个代表video_id,第三个代表出现times
      val sql="insert into day_top_videos values (?,?,?)"
      state=conn.prepareStatement(sql)

      // 执行批量插入(注意数据类型要与case class中对应)
      for (item<-list) {
        state.setString(1,item.day) // 第一个问号
        state.setLong(2,item.num)  // 第二个问号
        state.setLong(3,item.times)  // 第三个问号

        // 添加批处理:提高写入数据库的性能
        state.addBatch()
      }

      // 执行批处理
      state.executeBatch()

      // 手动提交事务
      conn.commit()

    }catch {
      case e:Exception=>e.printStackTrace()
    } finally {
      // 关闭连接释放资源
      MySQLUtils.release(conn,state)
    }

  }

  // 批量将地区访问次数Top3的视频插入数据库
  def insertAreaTopVideos(list:ListBuffer[CaseVideoIDTimesRank]): Unit = {
    var conn:Connection=null
    var state:PreparedStatement=null

    try {
      // 打开连接
      conn=MySQLUtils.getConnection()

      // 设置事务手动提交
      conn.setAutoCommit(false)

      // 插入数据库的sql语句
      val sql="insert into area_top_videos(day,area,num,times,times_rank) values (?,?,?,?,?)"
      state=conn.prepareStatement(sql)

      // 遍历list中的数据
      for(item<-list) {
        // 获取day
        state.setString(1,item.day)
        // 获取area
        state.setString(2,item.region)
        // 获取num
        state.setLong(3,item.num)
        // 获取times
        state.setLong(4,item.times)
        // 获取times_rank
        state.setLong(5,item.times_rank)

        // 添加到批处理
        state.addBatch()
      }

      // 执行批处理
      state.executeBatch()

      // 提交事务
      conn.commit()

    } catch {
      case e:Exception=>e.printStackTrace()
    } finally {
      // 关闭连接释放资源
      MySQLUtils.release(conn,state)
    }

  }

  // 批量将流量Top的视频插入数据库
  def insertFlowTopVideos(list:ListBuffer[CaseVideoIDFlow]): Unit = {

    var conn:Connection=null
    var state:PreparedStatement=null

    try {
      conn=MySQLUtils.getConnection()
      conn.setAutoCommit(false)
      val sql="insert into flow_top_videos(day,num,flow) values (?,?,?)"
      state=conn.prepareStatement(sql)

      // 遍历list
      for(item<-list) {
        state.setString(1,item.day)
        state.setLong(2,item.num)
        state.setLong(3,item.flow)

        // 添加批处理
        state.addBatch()
      }

      state.executeBatch()
      conn.commit()
    } catch {
      case e:Exception=>e.printStackTrace()
    } finally {
      // 关闭数据库，释放资源
      MySQLUtils.release(conn,state)
    }

  }

}
