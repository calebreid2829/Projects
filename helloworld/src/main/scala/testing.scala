object testing {
  def main(args: Array[String]): Unit = {
    val result = (a:String) =>a.trim.toInt
    var a: Int = 0
    val q = ()=>println(s"Question ${a+=1; a}")
    q()

    println(result("5"))
    println(result("7"))
    q()
  }
}
