import java.sql.{ResultSet, ResultSetMetaData, SQLException}
import java.io.File
class SelectRow {
  val db = new Database()

  //val rst = (x:String) =>{val c = db.Open();val s = c.createStatement();s.executeQuery(x)}
  val i =(x:String) => x match{case "races"=>"raceid"case "classes"=>"classid"case "armor"=>"armorid"
  case "weapons"=>"weaponid"case "characters"=>"characterid" case "items" =>"itemid"}
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
  def SelectNewId(table: String): Int={
    val connection = db.Open()
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(s"SELECT MAX(${i(table)}) FROM $table;")
    resultSet.next()
    val a = resultSet.getInt(1) +1
    db.Close(connection)
    a
  }
  def SelectArmor(): IndexedSeq[(Int,String,String,Int,Int,Int,Int,Int)]={
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
    try {
      val resultSet = s.executeQuery(s"SELECT * FROM $table WHERE ${i(table)} = $id")

      resultSet.next()
      var r = ""
      if (table == "races") r = resultSet.getString("race")
      else if (table == "classes") r = resultSet.getString("class")
      else r = resultSet.getString("name")
      db.Close(c)
      r
    }
    catch{
      case _: SQLException => "Entry does not exist"
    }
  }
  def SelectAllItems():Array[Item]={
    val c = db.Open()
    val s = c.createStatement()
    val rs = s.executeQuery(s"SELECT * FROM items")
    val items:Array[Item] = new Array[Item](count("items"))
    var num = 0
    while(rs.next()){items(num) = Item(rs.getInt(1),rs.getString(2)
      ,rs.getInt(3),rs.getDouble(4));num+=1}
    items
  }
  def SelectAllCharacters():Array[Character]={
    val c = db.Open()
    val s = c.createStatement()
    val rs = s.executeQuery(s"SELECT * FROM characters")
    val chars:Array[Character] = new Array[Character](count("characters"))
    var num = 0
    while(rs.next()){
      chars(num)= Character(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4)
      ,rs.getInt(5),rs.getString(6),rs.getInt(7),rs.getInt(8),rs.getInt(9)
      ,rs.getInt(10),rs.getInt(11),rs.getInt(12),rs.getInt(13),rs.getInt(14)
      ,rs.getInt(15),rs.getInt(16),rs.getString(17),rs.getInt(18));num+=1
    }
    StoreCharacters(chars)
    chars
  }
  def SelectCharacter(id:Int):Character={
    val c = db.Open()
    val s = c.createStatement()
    val rs = s.executeQuery(s"SELECT * FROM characters WHERE characterid = $id")
    rs.next()
    val p = Character(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4)
      ,rs.getInt(5),rs.getString(6),rs.getInt(7),rs.getInt(8),rs.getInt(9)
      ,rs.getInt(10),rs.getInt(11),rs.getInt(12),rs.getInt(13),rs.getInt(14)
      ,rs.getInt(15),rs.getInt(16),rs.getString(17),rs.getInt(18))
    p
  }
  private def StoreCharacters(chars:Array[Character]): Unit={
    val writer = new FileWriter()
    val file = new File("sheets")
    val files = file.listFiles.toList
    files.foreach(x => x.delete())
    chars.foreach(x => writer.Write(x))
  }
}
