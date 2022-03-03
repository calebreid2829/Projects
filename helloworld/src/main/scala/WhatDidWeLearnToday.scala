import scala.io.StdIn.readLine

object WhatDidWeLearnToday {
  def main(args: Array[String]): Unit = {
    val l1 = new _3_3_22()
    l1.learned()
  }
  class _3_3_22{
    def learned(): Unit ={
      //3/3/22
      //Accidentally figured out how to use a lambda and still able to use it as a variable if needed
      //input is a lambda that takes input and converts it into an int with exception handling
      //By using the apply function from the lambda, input in this case, it seems to treat it as just a variable
      //So I'm able to use it to get input, parse it, and use it as a variable all at once
      //Though I'm not sure if this is recommended or not, I'm planning on asking later why this actually works cause brain empty :P
      val input = () => try{readLine().toInt}catch{case _ => 0}
      input.apply() match{
        case 1 => println("It's 1")
        case _ =>println("Ya goofed")
      }
      println(input.apply())
      //Learned how the enumerators work in scala
      //Seems you're able to have their values be any data type not just int
      //And because I'm obbsessed with them at the moment I tried having it equal functions and it still works
      //So I could possibly do the idea I had years ago that I thought I could do in c# which was to make a collection of functions in an enum
      //But again I'm not sure if this is actually something that's recommended to do or if it would be too messy and convoluted.
      object Bored extends Enumeration{
        type Bored = Value

        val line = Value(5,readLine())
        val balls = (x:Int) =>x+2
      }
      println(s"Testing enumerator id: ${Bored.line.id}\nTesting enumerator as lambda: ${Bored.line}")
      println(s"Testing enumerator as lambda with parameters: ${Bored.balls(4)}")
    }
  }
}
