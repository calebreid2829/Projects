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
  def Write(p: Character): Unit={
    val writer = new PrintWriter(s"sheets/${p.name}.json")
    val n = (x:String,y:Any,z:Int) => quotes(x)+": "+s"${y.toString}${if(z==1)","else ""}"
    writer.println("{")
    writer.println(n("characterid",p.characterid,1))
    writer.println(n("name",p.name,1))
    writer.println(n("raceid",p.raceid,1))
    writer.println(n("classid",p.classid,1))
    writer.println(n("level",p.level,1))
    writer.println(n("alignment",p.alignment,1))
    writer.println(n("str",p.str,1))
    writer.println(n("dex",p.dex,1))
    writer.println(n("con",p.con,1))
    writer.println(n("int",p.int,1))
    writer.println(n("wis",p.wis,1))
    writer.println(n("char",p.char,1))
    writer.println(n("weaponid",p.weaponid,1))
    writer.println(n("armorid",p.armorid,1))
    writer.println(n("ac",p.ac,1))
    writer.println(n("hp",p.hp,1))
    writer.println(n("hitdice",p.hitdice,1))
    writer.println(n("speed",p.speed,0))
    writer.println("}")
    writer.close()
  }
  def Delete(name:String): Unit={
    val file = new File(s"sheets/$name.json")
    file.delete()
  }

}
