package ForThreading
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
class TestObj{

}
object TestObj{
  def main(args: Array[String]): Unit = {
    val ft = new ForThreading()
/*    val f = for{
      f1<-ft.job(1)
      f2<-ft.job(f1)
      f3<-ft.job(f2)
      f4<-ft.job(f3)
      f5<-ft.job(f4)
    } yield List(f1,f2,f3,f4,f5)
    f.map(z=>println(s"Done. ${z.size} jobs run"))
    Thread.sleep(6000)*/
    val g = for{
      g1 <-ft.job(1+2)
      g2 <-Future.sequence(List(ft.job(g1),ft.job(g1)))
      g3 <-ft.job(g2.head)
      g4 <-Future.sequence(List(ft.job(g3),ft.job(g3)))
      //g5 <-ft.job(g4.head)
    } yield g2.size+g4.size
    g.foreach(z=>println(s"Done. $z jobs run in parallel"))
    Thread.sleep(6000)
  }
}