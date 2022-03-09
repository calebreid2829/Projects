import java.sql.SQLIntegrityConstraintViolationException
import scala.util.matching.Regex

class InsertRow {
  val db = new Database()
  val insert = (table: String,parameters: String,values: String) =>
      s"INSERT INTO $table ($parameters) VALUES (${values.replaceAll("[^0-9,.]+", (for(x<-values.split(",") if x.compare("[^0-9,]+".r.findFirstMatchIn(x).mkString("")) == 0) yield ("\""+x+"\"")).mkString(","))}) "

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
      val query = insert("race","race",race)
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
  def Armor(armor: String):Boolean={
    val connection = db.Open()
    val statement = connection.createStatement()
    val query = insert("armor","name,armor_type,ac,strength,stealth_disadvantage,weight",armor)
    if(armor !="quit"){
      try{
        statement.executeUpdate(query)
        println("Armor inserted successfully")
      }
      catch{
        case _: SQLIntegrityConstraintViolationException => println("This armor is already in the database, no insert performed");
      }
      finally{
        db.Close(connection)
      }
      true
    }else false
  }
}
