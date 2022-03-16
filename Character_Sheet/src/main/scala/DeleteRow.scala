import java.sql.{ResultSet, SQLIntegrityConstraintViolationException}
import scala.util.matching.Regex
import java.io.File
class DeleteRow {
  val db = new Database()
  val i = (x:String) => x match{case "items" =>"itemid" case "weapons" => "weaponid" case "armor" => "armorid"
  case "classes" => "classid" case "races" => "raceid" case "characters" => "characterid"}

  val min = (x:String) => {val connection = db.Open();val statement = connection.createStatement();
    val resultSet = statement.executeQuery(s"SELECT min(${i(x)}) FROM " + x);
    resultSet.next();val a = resultSet.getInt(1);db.Close(connection);a}

  def Delete(table:String,id:Int):Unit ={
    val c = db.Open()
    val s = c.createStatement()
    val query = s"DELETE FROM $table WHERE ${i(table)} = $id"
    if(table !="items"&&table!="characters") {
      val query2 = s"UPDATE characters SET ${i(table)} = ${min(table)}"
      s.executeUpdate(query2)
      val select = new SelectRow()
      select.SelectAllCharacters()
    }
    else if(table =="characters"){
      val select = new SelectRow()
      val p = select.SelectCharacter(id)
      val writer = new FileWriter()
      writer.Delete(p.name)
    }
    s.executeUpdate(query)
    println("Entry successfully deleted")
  }

}
