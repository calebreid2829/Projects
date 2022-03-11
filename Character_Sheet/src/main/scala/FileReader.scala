import java.io.PrintWriter
import scala.io.Source
import java.io.File
import scala.util.matching.Regex
class FileReader{
  //val out = new PrintWriter("FF_sheet.json")
  //out.println("Somewhat New Sheet")
  //out.close()


  def read(json: String): Map[String,String]={
    val property = new Regex("\\b[^{}\\n,\":]+\\b|\\d+")
    val file = new File(json)
    val in = Source.fromFile(file,"UTF-8")
    val str = property.findAllIn(in.getLines().mkString("")).toArray
    in.close()
    val stats = (for(i <- 0 until str.length by 2) yield Map(str(i) ->str(i+1))).flatten.toMap
    stats
  }

}
