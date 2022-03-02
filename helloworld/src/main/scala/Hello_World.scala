/*
  Created by: Caleb Reid
  Created on: 3/1/2022
  Last Updated: 3/2/2022
*/
import Console.{UNDERLINED,BOLD,RESET}
import scala.util.matching.Regex
object Hello_World {
  def main(args: Array[String]): Unit = {
    val obj = new HW1()
    //Running each method
    obj.q1()
    obj.q2()
    obj.q3("http://allaboutscala", 8)
    obj.q4(10)
    obj.q5()
    obj.q6("Caleb","Reid","Enter the Spiderverse")
    obj.q7("Vanilla Donut 10 2.25")
    obj.q8()
    obj.q9()
    obj.q10()
  }

}
//Decided to create a class that holds everything just to keep it all in one place
class HW1{
  //Wanted the separation between each question to be clearer and was messing around with lambdas after they were shown
  //I hate copy and pasting things multiple times cause I know it can be done better if I'm doing that
  //So I made a lambda to print out the question and their number
  val q = (a:Int)=>println(s"\nQuestion $a:")
  def q1(): Unit ={
    q(1)
    println("{")
    println("\"donut_name\":\"Vanilla Donut\",")
    println("\"quantity_purchased\":\"10\",")
    println("\"price\":2.5")
    println("}")

  }
  def q2(): Unit={
    q(2)
    println(s"What is your ${UNDERLINED}${BOLD}name${RESET} and ${BOLD}age?${RESET}")
  }
  def q3(str: String,index: Int): Unit={
    /*
    //Got this to be a lot better than it was at first
    //No longer creates any variables in here for tracking it just changes what I need while showing it
    //I'm trying to get as much practice as I can with functional programming since I haven't really done it before
    //And I can already tell how useful it can be
    */
    q(3)
    try{
      println(s"The letter ${index-1} characters in the string ${"\"" + str + "\""} is the letter: ${str(index)}")
    }
    catch{
      case _: StringIndexOutOfBoundsException => println("Index is out of range")
    }
  }
  def q4(amount: Int): Unit ={
    q(4)
    println(s"The price of $amount donuts is: " + "$" + "%.2f".format(amount*2.50))
  }
  def q5(): Unit ={
    q(5)
    println("What is your favorite movie of all time?")
  }
  def q6(fName: String, lName: String, movie: String): Unit ={
    q(6)
    print(s"First Name: $fName \nLast Name: $lName\nFavorite Movie: $movie\n")
  }
  def q7(str: String): Unit={
    /*
    //This one feels ugly to me but it's about as good as I can make it I think
    //The biggest issue was converting from the found value with the regex to the correct type
    //--Leaving previous comments for record--
    //I made it a bit better now
    //It still feels a little roundabout by making it into a string,trimming it, and then converting
    //But I'm not sure if there is another way to do it
    //I couldn't find any methods from the findFirstMatchIn that converted except to string
    //But by using a lambda it at least looks much cleaner and still works the same
    //If I can remember this for the future then I won't have wasted time by typing each one out
    */
    q(7)
    val w = new Regex("\\b\\D*\\b")
    val i = new Regex(" \\d* ")
    val d = new Regex(" \\d*\\.\\d*")
    val a = (x:Regex)=>x.findFirstMatchIn(str).mkString("").trim
    val donut: String = a(w)
    val amount: Int = a(i).toInt
    val price: Double = a(d).toDouble

    print(s"The donut variable = $donut and is of type String\nThe amount variable = $amount and is of type Int\n" +
      s"The price variable = $price and is of type Double\n")
  }
  def q8(): Unit={
    /*
    //Complete honesty this one feels like a fluke
    //I know what to do, I've even done similar things in other languages like SQL
    //I guess it's just that there isn't some sort of simple sorting/ordering method for maps
    //Though that may just be because I missed it
    //I know how this works it just feels off
    */
    q(8)
    val kids: Map[String, Int] = Map("Bill" -> 9,"Jonny" ->8,"Tommy"->11,"Cindy"->13)
    val newkids = scala.collection.immutable.ListMap(kids.toSeq.sortBy(_._1):_*)
    newkids.foreach(x=> println(x))
  }
  def q9(): Unit={
    q(9)
    val left = List("Cake","Milk","Cheese","Toilet Paper")
    val right = List("Bread","Water","Juice","Milk","Cheese")
    right.foreach(x=> if(left.contains(x)) println(x))
  }
  def q10(): Unit={
    q(10)
    for(i<- 100 until 111) println(i)
  }
}
