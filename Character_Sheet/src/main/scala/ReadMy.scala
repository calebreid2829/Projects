 object ReadMy {
  def readln(): String={
    val in = scala.io.StdIn.readLine()
    if(in.toLowerCase =="b")throw new Back()
    else in
  }
}
