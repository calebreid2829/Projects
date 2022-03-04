import java.util.LinkedList
import scala.collection.mutable.ArrayStack
import scala.util.matching.Regex
import scala.io.StdIn.readLine

object testing {
  def main(args: Array[String]): Unit = {
    //So technically this "failed" the challenge as it only passed 4 of the 10 test cases
    //But I'm still proud of this and not changing it cause fuck it
    //It litterally only failed cause some of the test cases used numbers that were larger than even Long would allow
    //Like stupidly huge numbers that likely don't even have names
    //If this was a job and it didn't work correctly I'd understand and redo it, but this is just for practice
    //And it led to me making my first recursive function that actually works so I'm proud of it
    //Still ticks me off though. -_-
    //val str: String = readLine()
    //val i = new Regex("\\b\\d*\\b")
    //val j = new Regex(" \\d*")
    //val a = (x:Regex)=>x.findFirstMatchIn(str).mkString("").trim
    //println(mega_digit(a(i).toLong,a(j).toInt))
    //val j = (x:Regex)=>x.findAllIn(str)
    //val a: List[String] = j(i).toList
    //println(digital_root(a.head))
    //var end: Int = 0
    //for(x <- 0 until a(2).toInt) end+=digital_root(a.head)
    //println(end)
    def mega_digit(num: Long, times:Int): Int={
      var end: Long = 0
      for(x <- 0 until times)end+=super_digit(num)
      super_digit(end).toInt
    }
    def digital_root(num: String): Int ={
      if(num.length > 1)digital_root((num(0).toString.toInt+digital_root(num.substring(1))).toString)
      else num.toInt
    }
    def super_digit(num: Long): Long ={

      if(num >9){/*println(s"${num%10} ${num/10}")*/;super_digit(num%10 + super_digit(num/10))}
      else num
    }
    println(s"Real Digital Root: ${real_dig("1234".split(""))}")
    def real_dig(arr:Array[String]): Int={
      var a: Int = 0
      arr.foreach(i => a+=i.toInt)
      if(a > 9) a=real_dig(a.toString.split(""))
      a
    }

  }

}
