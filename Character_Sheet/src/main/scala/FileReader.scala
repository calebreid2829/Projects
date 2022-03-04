import java.io.PrintWriter
import scala.io.Source
import java.io.File
import scala.util.matching.Regex
object FileReader extends App{
  //val out = new PrintWriter("Sheet.json")
  //out.println("Somewhat New Sheet")
  //out.close()

  val property = new Regex("\\b[^{}\\n,\":]+\\b|\\d+")
  val file = new File("Sheet.json")
  val in = Source.fromFile(file,"UTF-8")
  val str = property.findAllIn(in.getLines().mkString("")).toArray
  //str.map(x =>x).grouped(2).zipWithIndex.map(t =>(t._2,t._1)).toMap
  val stats = (for(i <- 0 until str.length by 2) yield Map(str(i) ->str(i+1))).flatten.toMap

  println(stats("name"))
  in.close()
  //val stats: Map = for(i <- str.toMap(_)) yield i
  //println(str)
  //println(stats(1))
  //println(stats(2))
  //println(str.size)
  //str.foreach(x => println(x))
}
