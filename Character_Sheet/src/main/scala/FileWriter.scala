import java.io.PrintWriter
import scala.io.Source
import java.io.File
import scala.util.matching.Regex

class FileWriter {
  val db = new Database()
  val quotes = (x:String) => "\""+x+"\""
  val isString = (x:String) => x.compare("[^0-9]+|\\d+d\\d+".r.findFirstMatchIn(x).mkString(""))
  def Write(character: Map[String,String],sheet: String): Unit={
    val writer = new PrintWriter(sheet)
    val keys = character.keySet
    writer.println("{")
    //x.compare("[^0-9,]+".r.findFirstMatchIn(x).mkString("")) == 0
    keys.foreach(x =>
      writer.println(s"${quotes(x)}: ${if (isString(character(x)) == 0) quotes(character(x)) else character(x)}${if(x != keys.last)","else ""}"))
    writer.println("}")
    writer.close()

  }

}
