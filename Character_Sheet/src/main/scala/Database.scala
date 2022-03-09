import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._

import java.io.File
import java.sql.{Connection, DriverManager}

class Database {

  def Open(): Connection={
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/charactersheet"
    val username = "root"
    val password = "password"

    DriverManager.getConnection(url, username, password)
  }
  def Close(connection: Connection): Unit={
    connection.close()
  }
}
