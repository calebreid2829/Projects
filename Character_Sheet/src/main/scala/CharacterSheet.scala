import java.io.File
import scala.io.Source
import scala.util.matching.Regex
import scala.io.StdIn.readLine
import scala.collection.mutable
import scala.collection.immutable.ListMap

object CharacterSheet {
  val db = new Database()
  val read = new FileReader()
  val writer = new FileWriter()
  val select = new SelectRow()
  val insert = new InsertRow()

  val numCheck = (x:String) => try{x.toInt}catch{case _: Any => -1}
  //var sheet = ""
  def main(args: Array[String]): Unit = {


    var loop = true
    try {
      do {
        clear()
        println("What would you like to do?")
        print("[1] Make a new sheet\n[2] Open an existing sheet\n[3] Browse the database\n[4] Add new entries to the database\n")
        val line = readLine().toLowerCase
        val splitLine = line.split(" ")
        if(line == "1")makeSheet()
        else if(line == "2"){
          val sheet =chooseSheet();
          do {
            showSheet(read.read(sheet))
            println("[1]Edit\n[2]Back")
            val line2 = readLine().toLowerCase
            if(line2 == "1") editSheet(read.read(sheet))
            if(line2 == "2")loop = false
          }while(loop)
          loop = true
        }
        else if (splitLine(0) == "insert") {
          if (splitLine(1) == "race")do print("Insert Race Name: ")while(insert.Race(readLine()))
          else if (splitLine(1) == "item")do print("Insert Item Name, Cost, and Weight: ")while(insert.Item(readLine()))
          else if (splitLine(1) == "class")do print("Insert Class Name: ")while(insert.Class(readLine()))
          else if (splitLine(1) == "armor")do print("Insert Armor Type,name,Cost,AC,Strength,Stealth Disadvantage, And Weight: ")while(insert.Armor(readLine()) != -1)
        }
        else if (splitLine(0) == "show") {
          if(splitLine(1) == "items")showItems()
          else if (splitLine(1) == "races")showRaces()
          else if (splitLine(1) == "classes")showClasses()
          else if (splitLine(1) == "armor")showArmor()
        }
        //else if (line == "sheet") showSheet()
        else if (line == "help") showOptions()
        //else if(line == "save") writer.Write(read.read(sheet))
        else if (line == "quit") loop = false
      } while (loop)
    }
    catch{
      case _: ArrayIndexOutOfBoundsException => println("Not a recognized command, type help to see a list of accepted commands")
    }
  }
  def getBonus(stat: String): String={
    if(stat.toInt-10 < 0) math.floor((stat.toDouble-10)/2).toInt.toString
    else "+" + ((math.floor((stat.toDouble-10)/2).toInt))
  }
  def showSheet(p:Map[String,String]): Unit={

    val arm = select.SelectOne("armor",p("armorid"))
    val wep = select.SelectOne("weapons",p("weaponid"))
    print(s"\n ${p("name")}  |  Level ${p("level")} ${p("raceid")} ${p("classid")}  |  ${p("alignment")}")
    print(s"\n HP ${p("hp")}  |  Hit Dice ${p("hitDice")}  |  Death Save - Success[][][] - Failure[][][]")
    print(s"\n AC ${p("ac")}  |  Initiative ${getBonus(p("dex"))}  |  Speed ${p("speed")}")
    print(s"\n Strength ${p("str")}[${getBonus(p("str"))}]\t|  Athletics")
    print(s"\n Dexterity ${p("dex")}[${getBonus(p("dex"))}]\t|  Acrobatics - Sleight of Hand - Stealth")
    print(s"\n Constitution ${p("con")}[${getBonus(p("con"))}]|")
    print(s"\n Intelligence ${p("int")}[${getBonus(p("int"))}]|  Arcana - History - Investigation - Nature - Religion")
    print(s"\n Wisdom ${p("wis")}[${getBonus(p("wis"))}]\t\t|  Animal Handling - Insight - Medicine - Perception - Survival")
    print(s"\n Charisma ${p("char")}[${getBonus(p("char"))}]\t|  Deception - Intimidation - Performance - Persuasion")
    print(s"\n Armor: ${arm}")
    print(s"\n Weapon: ${wep}\n")
  }
  def showOptions(): Unit={
    print("--Commands--")
    print("\nInsert [item,race,class,weapon,armor]")
    print("\nShow [items,races,classes,weapons,armor]")
    print("\nSheet")
    println("\nQuit")
  }
  def showRaces(): Unit={
    val connection = db.Open()
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT race FROM races;")
    println("Race:")
    while(resultSet.next()){println(resultSet.getString(1))}
    db.Close(connection)
  }
  def showItems(): Unit={
    val connection = db.Open()
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT name,cost,weight FROM items;")

    println("Item\t\tCost\tWeight")
    while (resultSet.next()){println(resultSet.getString(1)+"\t\t"+resultSet.getString(2)+"\t"+resultSet.getString(3))}
    db.Close(connection)
  }
  def showClasses(): Unit={
    val connection = db.Open()
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT class FROM classes")
    println("Class:")
    while(resultSet.next()){println(resultSet.getString(1))}
    db.Close(connection)
  }
  def showWeapons(): Unit={
    val connection = db.Open()
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT name,simple_martial,melee_ranged,damage,damage_type,weight,cost,properties FROM classes")
  }
  def showArmor(): Unit={
    val connection = db.Open()
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT name,armor_type,ac,strength,stealth_disadvantage,weight FROM armor")
    println("Armor:")
    while(resultSet.next()){println(resultSet)}
    db.Close(connection)
  }
  def clear(): Unit={
    print("\u001b[H")
  }
  def getListOfFiles(dir: String):List[File] = {
    val file = new File(dir)
    file.listFiles.toList
  }
  def chooseSheet(): String={
    val files = getListOfFiles("sheets")
    //files.foreach(x => println(x))
    var sheet = ""
    do {
      val r = new Regex(raw"sheets\\\\K[^.]+")
      for (x <- 0 until files.length) println(s"[$x] ${files(x).toString.split(raw"sheets\\").mkString("").split(raw"\.json").mkString("")}")
      try{sheet = files(readLine().toInt).toString}
      catch{ case _: Any => println("Choose only a listed file")}
    }while(sheet == "")
    sheet
  }
  def editSheet(character:Map[String,String]): Unit={
    var p = character
    var loop = true

    do{
      showSheet(p)
      println("What do you want to change?")
      println("[1] Save\n[2] Discard Changes")
      val line = statNames(readLine().toLowerCase)
      //Saves the file
      line match {
        case "1"=> writer.Write (p, s"sheets/${p ("name")}.json");loop = false
        //Discards any changes made by not saving anything
        case "2" => loop = false
        case "str"|"dex"|"con"|"wis"|"int"|"char" =>
          print (s"Old Value: ${p(line)}\nNew Value: ")
          try {p = (p - line) + (line -> readLine ().toInt.toString)}
          catch {case _: NumberFormatException => println ("Make sure you are inputting a number")}
        case "raceid"|"classid" => p = changeRaceClass(line,p)
          //if(line == "races")
        case "weaponid" => p = changeWeapon(line,p)
        case "armorid" => p = changeArmor(line,p)
        case _ =>
      }
    }while(loop)
  }
  def makeSheet(): Unit={
      var character = Map.empty[String, String]
      val p = (x: String) => {
        print(s"$x: ");
        character = character + (x.toLowerCase -> readLine())
      }
      println("Name: ")
      character= character + ("name" -> readLine())
      println("Race: ")
      character= changeRaceClass(statNames("race"),character)
      println("Class: ")
      character = changeRaceClass(statNames("class"),character)
      println("Level: ");character += ("level" ->readLine())
      println("Armor: ");character = changeArmor(statNames("armor"),character)
      println("Weapon: ");character = changeWeapon(statNames("weapon"),character)
      println(character)
      /*p("Name")
      p("Race")
      p("Class")
      p("Level")
      p("Alignment")
      p("str")
      p("dex")
      p("con")
      p("int")
      p("wis")
      p("char")
      p("hp")
      p("ac")
      print("Hit Dice: ")
      character = character + ("hitDice" -> readLine())
      p("weapon")
      p("armor")
      p("speed")*/
      //var character:Map[String,String] = Map("name"->readLine())

      /*
      showSheet(character)
      print("[1] Save\n[2] Edit\n")
      var loop = true
      do {
        val line = readLine()
        if(line == "1"){ writer.Write(character,s"sheets/${character("name")}");loop = false}
        else if(line =="2")editSheet(character); loop = false
      }while(loop)*/
  }
  def statNames(x: String): String = x match{
    case "strength" => "str"
    case "dexterity" => "dex"
    case "constitution" => "con"
    case "wisdom" => "wis"
    case "intelligence" => "int"
    case "charisma" => "char"
    case "weapon" => "weaponid"
    case "armor" => "armorid"
    case "class" => "classid"
    case "race" => "raceid"
    case _ => x
  }
  def changeRaceClass(line: String,character: Map[String,String]): Map[String,String]={
    var p = character
    val resultSet = select.SelectMap(line)
    ListMap(resultSet.toSeq.sortBy(_._1):_*).foreach(x => println(s"[${x._1}] ${x._2}"))
    val num = readLine().toLowerCase
    if(resultSet.contains(numCheck(num)))p = (p-line) + (line ->resultSet(numCheck(num)))
    else{
      var i = true; resultSet.foreach(x => if(num == x._2.toLowerCase){ i = false;p = (p-line) + (line ->x._2)})
      if(i){
        println(s"${num.capitalize} not found in list. Would you like to add it to the database?\ny or n")
        if(readLine() == "y"){if(line == "raceid")insert.Race(num) else insert.Class(num);p=(p-line)+(line ->num.capitalize)}
      }
    }
    p
  }
  def changeArmor(line: String,character: Map[String,String]): Map[String,String]={
    var p = character
    val list = select.SelectArmor(line)
    list.foreach(x =>println(s"[${x._1}] ${x._2+"-"+x._3} | ${x._4}gp | ${x._5} AC\n\tStrength Required: ${x._6} |" +
      s"${if(x._7 == 1)"Stealth Disadvantage |"else ""} weight: ${x._8}\n"))
    println("Or [Add] new armor")
    val num = readLine().toLowerCase
    if(num == "add") {
      var statement = ""
      val red = () => readLine().capitalize + ","
      print("Type [Light,Medium,Heavy]: ");statement+=red()
      print("Name: "); statement+=red()
      print("Price: "); statement+=red()
      print("AC: "); statement+=red()
      print("Required Strength: ");statement+=red()
      print("Stealth Disadvantage [1] yes [0] no: ");statement+=red()
      print("Weight: "); statement+=readLine().capitalize
      //println(statement)
      //if(statement.contains("quit"))statement = "quit"
      val id = insert.Armor(statement)
      if(id != -1) p = (p-line) + (line ->id.toString)
    }
    else {
      var i = true
      list.foreach(x => if (x._1 == numCheck(num)) {i=false;p=(p-line)+(line->x._1.toString)})
      if(i){list.foreach(x=>if(num==x._3.toLowerCase){p=(p-line)+(line->x._1.toString)})
      }
    }
    p
  }
  def changeWeapon(line: String,character: Map[String,String]): Map[String,String]={
    var p = character
    val list = select.SelectWeapon()
    list.foreach(x =>println(s"[${x._1}] ${x._2} | ${if(x._3 ==0)"Simple"else "Martial"} | ${if(x._4 ==0)"Melee"else "Ranged"}" +
      s"\n\t${x._5} ${x._6} Damage | Weight: ${x._7} | Price: ${x._8} | ${if(x._9 != "")"Properties: "+x._9 else ""}\n"))
    println("Or [Add] new weapon")
    val num = readLine().toLowerCase
    if(num == "add"){
      var statement = ""
      val red = () => readLine().capitalize + ","
      print("Name: ");statement+=red()
      print("[1]Simple [2]Martial: ");statement+=red()
      print("[1]Melee [2]Ranged: ");statement+=red()
      print("Damage: ");statement+=red()
      print("Damage Type: ");statement+=red()
      print("Weight: ");statement+=red()
      print("Cost: ");statement+=red()
      print("Properties: ");statement+=readLine().capitalize
      val id = insert.Weapon(statement)
      if(id != -1)p=(p-line)+(line->id.toString)
    }
    else{
      var i = true
      list.foreach(x => if(x._1 == numCheck(num)){i=false;p=(p-line)+(line->x._1.toString)})
      if(i){list.foreach(x=>if(num==x._2){p=(p-line)+(line->x._1.toString)})}
    }
    p
  }

}



