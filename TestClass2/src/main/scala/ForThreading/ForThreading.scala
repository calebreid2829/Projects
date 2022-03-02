package ForThreading

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ForThreading {
  def job(n:Int): Future[Int]= Future[Int]{
    Thread.sleep(1000)
    println(n)
    n+1
  }
}
