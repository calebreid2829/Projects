import java.sql.ResultSet
import java.sql.ResultSetMetaData
class SelectRow {
  val db = new Database()
  //val rst = (x:String) =>{val c = db.Open();val s = c.createStatement();s.executeQuery(x)}
  val i =(x:String) => x match{case "races"=>"raceid"case "classes"=>"classid"case "armor"=>"armorid"case "weapons"=>"weaponid"}
  val count = (x:String) => {val connection = db.Open();val statement = connection.createStatement();
    val resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + x);
    resultSet.next();val a = resultSet.getInt(1);db.Close(connection);a}
  val inc = (x:Int,y:ResultSet) => {y.next();x}
  def SelectMap(table: String): Map[Int,String]={
    val inc = (x:Int,y:ResultSet) => {y.next();x}
    val i:String = table match{case "raceid" => "races" case "classid" => "classes"}
    val count = () => {val connection = db.Open();val statement = connection.createStatement();
      val resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + i);
      resultSet.next();val a = resultSet.getInt(1);db.Close(connection);a}
    val cn2 = db.Open()
    val st2 = cn2.createStatement()
    val resultSet = st2.executeQuery("SELECT * FROM "+i+" ORDER BY "+table+" ASC")
    val a = (for(x<- 0 until count())
      yield Map(resultSet.getString(inc(1,resultSet)).toInt ->resultSet.getString(2))).flatten.toMap
   // db.Close(connection)
    db.Close(cn2)
    a
  }
  def SelectNew(table: String): Unit={
    val inc = (x:Int,y:ResultSet) =>{y.next();x}
    val connection = db.Open()
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(s"SELECT MAX(*) FROM $table;")
  }
  def SelectArmor(table: String): IndexedSeq[(Int,String,String,Int,Int,Int,Int,Int)]={
    val connection = db.Open()
    val statement = connection.createStatement()
    val rs = statement.executeQuery(s"SELECT * FROM armor;")
    //val cn = rs.getMetaData().getColumnCount()
    val a = (for(x<- 0 until count("armor"))
      yield (rs.getInt(inc(1,rs)),rs.getString(2),rs.getString(3),rs.getInt(4)
        ,rs.getInt(5),rs.getInt(6),rs.getInt(7),rs.getInt(8)))
    db.Close(connection)
    //a.foreach(x =>println(x))
    a
  }
  def SelectWeapon(): IndexedSeq[(Int,String,Int,Int,String,String,Int,Int,String)]={
    val c = db.Open()
    val s = c.createStatement()
    val r = s.executeQuery(s"SELECT * FROM weapons;")

    val a = (for(x<-0 until count("weapons"))
      yield (r.getInt(inc(1,r)),r.getString(2),r.getInt(3),r.getInt(4),r.getString(5),
        r.getString(6),r.getInt(7),r.getInt(8),r.getString(9)))
    db.Close(c)
    a
  }
  def SelectOne(table: String,id: String): String={
    val c = db.Open(); val s = c.createStatement();
    val resultSet = s.executeQuery(s"SELECT * FROM $table WHERE ${i(table)} = $id")
    resultSet.next()
    val r = resultSet.getString("name")
    db.Close(c)
    r
  }
}
