import java.sql.{ResultSet, SQLIntegrityConstraintViolationException}
import scala.util.matching.Regex

class InsertRow {
  val db = new Database()
  val quotes = (x:String) => "\""+x+"\""
  val i =(x:String) => x match{case "races"=>"raceid"case "classes"=>"classid"case "armor"=>"armorid"case "weapons"=>"weaponid"}
  val insert = (table: String,parameters: String,values: String) =>
      s"INSERT INTO $table ($parameters) VALUES (${values.replaceAll("[^0-9,.]+", (for(x<-values.split(",") if x.compare("[^0-9,]+".r.findFirstMatchIn(x).mkString("")) == 0) yield ("\""+x+"\"")).mkString(","))}) "
  val max = (x: String) => {val c = db.Open();val s = c.createStatement();
    val r = s.executeQuery(s"SELECT MAX(${i(x)}) FROM " + x);
    r.next();val a = r.getInt(1);db.Close(c);a}
  def test(): Unit={
    val connection = db.Open()
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT * FROM items;")

    while ( resultSet.next() ) {
      println(resultSet.getString(1)+", " +resultSet.getString(2) +", " +resultSet.getString(3))
    }
    db.Close(connection)
  }
  def Item(values: String): Boolean={
    val connection = db.Open()
    val statement = connection.createStatement()
    if(values != "quit") {
      val query = insert("items", "name,cost,weight", values)
      try {
        statement.executeUpdate(query)
        println("Item inserted successfully")
      }
      catch{
        case _: SQLIntegrityConstraintViolationException => println("This name has already been used, try something like: clothes,fine or rope,frayed");
        case _: java.sql.SQLException => println("The number of values input do not match the number of columns or their data types do not match\n"+
        "Make sure you enclose names in double quotes \"\"");
      }
      finally{
        db.Close(connection)
      }
      true
    }else false
  }
  def Race(race: String): Boolean={
    val connection = db.Open()
    val statement = connection.createStatement()
    if(race !="quit") {
      val query = insert("races","race",race)
      try {
        statement.executeUpdate(query)
        println("Race inserted successfully")
        db.Close(connection)
        true
      }
      catch {
        case _: SQLIntegrityConstraintViolationException => println(s"$race is already in the database. No insert performed");true
      }
      finally {
        db.Close(connection)
      }
    }else false
  }
  def Class(clss: String): Boolean={
    val connection = db.Open()
    val statement = connection.createStatement()
    val query = insert("classes","class",clss)
    if(clss != "quit"){
      try{
        statement.executeUpdate(query)
        println("Class inserted successfully")
      }
      catch{
        case _: SQLIntegrityConstraintViolationException => println("This class is already in the database, no insert performed");
        case _: java.sql.SQLSyntaxErrorException => println("Make sure the name is in double quotes \"\"");
      }
      finally{
        db.Close(connection)
      }
      true
    }else false

  }
  def Armor(armor: String):Int={
    val connection = db.Open()
    val statement = connection.createStatement()
    val values = armor.split(",")
    val query = s"INSERT INTO armor (armor_type,name,cost,ac,strength,stealth_disadvantage,weight) " +
      s"VALUES (${quotes(values(0))},${quotes(values(1))},${values(2)},${values(3)},${values(4)},${values(5)},${values(6)})"
    println(query)
    if(armor !="quit"){
      try{
        statement.executeUpdate(query)
        println("Armor inserted successfully")
        max("armor")
      }
      catch{
        case _: SQLIntegrityConstraintViolationException => println("This armor is already in the database, no insert performed");-1;
        case _: Any => println("I dunno chief");-1;
      }
      finally{
        db.Close(connection)
      }

    }else -1
  }
  def Weapon(weapon: String):Int={
    val c = db.Open()
    val s = c.createStatement()
    val values = weapon.split(",")
    val query = s"INSERT INTO weapons (name,simple_martial,melee_ranged,damage,damage_type,weight,cost,properties)" +
      s"VALUES (${quotes(values(0))},${values(1)},${values(2)},${quotes(values(3))},${quotes(values(4))},${values(5)}," +
      s"${values(6)},${quotes(values(7))})"
    if(weapon != "quit"){
      try{
        s.executeUpdate(query)
        println("Weapon inserted successfully")
        max("weapons")
      }
      catch{
        case _: SQLIntegrityConstraintViolationException => println("This weapon is already in the database, no insert performed");-1;
        //case _: Any => println("I dunno chief");-1;
      }
      finally{
        db.Close(c)
      }
    }else -1
  }
}
