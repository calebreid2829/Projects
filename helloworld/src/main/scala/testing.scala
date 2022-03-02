object testing {
  def main(args: Array[String]): Unit = {
    val result = (a:String) =>a.trim.toInt
    var a: Int = 0
    val q = ()=>println(s"Question ${a+=1; a}:")
    val u = (a:Int)=>println(s"Question $a:")
    q()
    u(7)
    q()
    u(15)
    q()
    u(7)
    q()
    u(36)
  }
}
