object testing {
  def main(args: Array[String]): Unit = {
    //val result = (a:String) =>a.trim.toInt
    var a: Int = 0
    //q is impure
    val q = ()=>println(s"Question ${a+=1; a}:")
    //u is pure
    val u = (i:Int)=>println(s"Question $i:")
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
