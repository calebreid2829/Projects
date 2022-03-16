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
  val delete = new DeleteRow()
  val readmy = () => {val a = readLine(); if(a.toLowerCase == "b") throw new Exception;else a}
  val numCheck = (x:String) => try{x.toInt}catch{case _: Any => -1}
  //val Jolyne = select.SelectCharacter(0)
  //println(Jolyne.name)
  //Jolyne.name = "Jolyne Kujo"
  //println(Jolyne.name)
  //val items:Array[Item] = select.SelectAllItems()
  //var sheet = ""
  def main(args: Array[String]): Unit = {
    select.SelectAllCharacters()
    var loop = true
      do {
        try {
          println("\nWhat would you like to do?")
          print("[1] Make a new sheet\n[2] Open an existing sheet\n[3] Browse the database\n[4] Exit to desktop\n")
          val line = readLine().toLowerCase
          line match {
            case "1" => makeSheet()
            case "2" => {
              val sheet = chooseSheet();
              if (sheet != "back") {
                do {
                  editSheet(read.read(sheet))
                } while (loop)
              }
              loop = true
            }
            case "3" =>
              do {
                println("[1] Items\n[2] Weapons\n[3] Armor\n[4] Races\n[5] Classes\n[B]ack")
                val line2 = numCheck(readmy()).toString
                try {
                  try {
                    line2 match {
                      case "1"|"items" => Show("items")
                      case "2"|"weapons" => Show("weapons")
                      case "3"|"armor" => Show("armor")
                      case "4"|"races" => Show("races")
                      case "5"|"classes" => Show("classes")
                    }
                  }
                  catch{case _: Exception =>}
                }
                catch {case _: Exception =>}
              } while (loop)
            case "4" => println("Are you sure?\n[Y]es\n[B]ack"); if (readLine.toLowerCase == "y") loop = false;
          }
        }
        catch {case _: Exception =>}
      } while (loop)
    }
  def getBonus(stat: String): String={
    if(stat.toInt-10 < 0) math.floor((stat.toDouble-10)/2).toInt.toString
    else "+" + ((math.floor((stat.toDouble-10)/2).toInt))
  }
  def showSheet(p:Map[String,String]): Unit={
    val arm = select.SelectOne("armor",p("armorid"))
    val wep = select.SelectOne("weapons",p("weaponid"))
    val race = select.SelectMap("raceid")
    val clss = select.SelectMap("classid")
    print(s"\n ${p("name")}  |  Level ${p("level")} ${race(p("raceid").toInt)} ${clss(p("classid").toInt)}  |  ${p("alignment")}")
    print(s"\n HP ${p("hp")}  |  Hit Dice ${p("hitdice")}  |  Death Save - Success[][][] - Failure[][][]")
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
  def Show(toShow:String):Unit={
    toShow match{
      case "items" =>
        val connection = db.Open()
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT * FROM items;")

        println("Item\t\tCost\tWeight")
        while (resultSet.next()){println(s"${if(resultSet.getString(1).length==1)" ["else "["}" +
          s"${resultSet.getString(1)}] "+Spacing(resultSet.getString(2),7)
          +Spacing(resultSet.getString(3).trim,4)+resultSet.getString(4).trim)}
        db.Close(connection)
      case "weapons" =>
        val list = select.SelectWeapon()
        list.foreach(x =>println(s"[${x._1}] ${x._2} | ${if(x._3 ==0)"Simple"else "Martial"} | ${if(x._4 ==0)"Melee"else "Ranged"}" +
          s"\n\t${x._5} ${x._6} Damage | Weight: ${x._7} | Price: ${x._8} | ${if(x._9 != "")"Properties: "+x._9 else ""}\n"))
      case "armor" =>
        val list = select.SelectArmor()
        list.foreach(x =>println(s"[${x._1}] ${x._2+"-"+x._3} | ${x._4}gp | ${x._5} AC\n\tStrength Required: ${x._6} |" +
          s"${if(x._7 == 1)"Stealth Disadvantage |"else ""} weight: ${x._8}\n"))
      case "races" =>
        val resultSet = select.SelectMap("raceid")
        var num = 1
        println("|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|")
        ListMap(resultSet.toSeq.sortBy(_._1):_*).foreach(x =>{print(s"${if(x._1 <10)" ["+x._1 else "["+x._1}] ${Spacing(x._2.trim,5)}");
          if(num%3 == 0)print("\n");num+=1;})
        if(num%3!=1)print("\n")
        println("|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|")

      case "classes" =>
        val resultSet = select.SelectMap("classid")
        var num = 1
        println("|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|")
        ListMap(resultSet.toSeq.sortBy(_._1):_*).foreach(x =>{print(s"${if(x._1 <10)" ["+x._1 else "["+x._1}] ${Spacing(x._2,5)}");
          if(num%3 == 0)print("\n");num+=1;})
        if(num%3!=1)print("\n")
        println("|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|")
        println()
    }
    print("[A]dd\n[D]elete\n[B]ack\n")
    val line = readmy()
    if(line.toLowerCase == "a")Add(toShow)
    if(line.toLowerCase == "d")Delete(toShow)
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
      for (x <- 0 until files.length) println(s"[${x+1}] " +
        s"${files(x).toString.split(raw"sheets\\").mkString("").split(raw"\.json").mkString("")}")
      println("\n[B]ack")
      val line = numCheck(readmy())
      if(line >= 1) try{sheet = files(line-1).toString}catch{ case _: Any => println("Choose only a listed file")}
    }while(sheet == "")
    sheet
  }
  def editSheet(character:Map[String,String]): Unit={
    var p = character
    var loop = true
    val set = (name: String,value: String,c: Map[String,String]) =>{val b=(c-name)+(name->value);b}
    val oldName = p("name")
      do {
        showSheet(p)
        println("What do you want to change?")
        println("[S]ave\n[B]ack\n[D]elete Character Sheet")
        val line = statNames(readmy().toLowerCase)
        try {
        //Saves the file
        line match {
          case "s" => writer.Delete(oldName);writer.Write(p, s"sheets/${p("name")}.json");loop = false;insert.CharacterUpload(p)
          //Discards any changes made by not saving anything
          case "b" => loop = false
          case "str" | "dex" | "con" | "wis" | "int" | "char" =>
            print(s"Old Value: ${p(line)}\nNew Value: ")
            try {
              p = set(line, numCheck(readmy()).toString, p)
            }
            catch {
              case _: NumberFormatException => println("Make sure you are inputting a number")
            }
          case "raceid" | "classid" => p = changeRaceClass(line, p)
          //if(line == "races")
          case "weaponid" => p = changeWeapon(line, p)
          case "armorid" => p = changeArmor(line, p)
          case "name" => print("Name: ");p = set("name", readmy(), p)
          case "level" => print("Level: ");p = set("level", numCheck(readmy()).toString, p)
          case "alignment" => print("Alignment: ");p = set("alignment", readmy(), p)
          case "hp" => print("HP: ");p = set("hp", numCheck(readmy()).toString, p)
          case "hitdice" => print("Hit Dice: ");p = set("hitdice", readmy(), p)
          case "speed" => print("Speed: ");p = set("speed", numCheck(readmy()).toString, p)
          case "d" =>
            println("Are you sure you want to delete this character?\n[Y]es\n[N]o")
            val line2 = readmy().toLowerCase
            if(line2 == "y") delete.Delete("characters",p("characterid").toInt)
            loop = false
          case _ =>
        }
        }
        catch{case _: Exception =>loop = false}
      } while (loop)
  }
  def makeSheet(): Unit= {
    try{
      var character = Map.empty[String, String]
      val p = (x: String) => {
        val a = readmy()
        character = character + (x.toLowerCase -> a)
      }
      println("Name: ")
      p("name")
      println("Race: ")
      character = changeRaceClass(statNames("race"), character)
      println("Class: ")
      character = changeRaceClass(statNames("class"), character)
      println("Alignment: "); p("alignment")
      print("Level: "); p("level")
      print("Strength: "); p("str")
      print("Dexterity: "); p("dex")
      print("Constitution: "); p("con")
      print("Intelligence: ");p("int")
      print("Wisdom: ");p("wis")
      print("Charisma: ");p("char")
      print("HP: ");p("hp")
      print("Hit Dice: ");p("hitdice")
      print("Speed: ");p("speed")
      var loop = true
      do {
        try {
          println("Armor: ");
          character = changeArmor(statNames("armor"), character)
          loop = false
        }
        catch{
          case _: Exception =>
        }
      }while(loop)
      loop = true
      do {
        try {
          println("Weapon: ");
          character = changeWeapon(statNames("weapon"), character)
          loop = false
        }
        catch{
          case _: Exception =>
        }
      }while(loop)
      character += ("characterid" -> select.SelectNewId("characters").toString)
      //character += ("ac"->10.toString)
      showSheet(character)
      print("[1] Save\n[2] Edit\n")
      loop = true
      do {
        val line = readLine()
        if (line == "1") {
          writer.Write(character, s"sheets/${character("name")}.json");
          insert.CharacterUpload(character);
          loop = false
        }
        else if (line == "2") editSheet(character);
        loop = false
      } while (loop)
    }
    catch{
      case _: Exception =>
    }
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
    case "hit dice" =>"hiddice"
    case "health" =>"hp"
    case _ => x
  }
  def changeRaceClass(line: String,character: Map[String,String]): Map[String,String]={
    var p = character
    val resultSet = select.SelectMap(line)
    var count = 1
    var loop = true
    do {
      ListMap(resultSet.toSeq.sortBy(_._1): _*).foreach(x => {
        print(s"${if (x._1 < 10) " [" + x._1 else "[" + x._1}] ${Spacing(x._2.trim, 5)}");
        if (count % 3 == 0) print("\n");
        count += 1;
      })
      if (count % 3 != 1) print("\n")
      println("|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|\t|")
      val num = readmy().toLowerCase
      if (resultSet.contains(numCheck(num))){ p = (p - line) + (line -> num); loop = false}
      else {
        var i = true;
        resultSet.foreach(x => if (num == x._2.toLowerCase) {
          i = false;
          p = (p - line) + (line -> x._1.toString);
          loop = false
        })
        if (i) {
          println(s"${num.capitalize} not found in list. Would you like to add it to the database?\ny or n")
          var id = -1
          if (readLine() == "y") {
            loop = false
            if (line == "raceid") {
              insert.Race(num);
              id = select.SelectNewId("races")
            }
            else {
              id = select.SelectNewId("classes")
            };
            insert.Class(num);
            p = (p - line) + (line -> num)
          }
        }
      }
    }while(loop)
    p
  }
  def changeArmor(line: String,character: Map[String,String]): Map[String,String]={
    var loop = true
    val update = (character: Map[String,String],field: String,value: String) =>{
      loop = false
      (character-field)+(field->value)}
    var p = character
    val list = select.SelectArmor()
    do {
      list.foreach(x => println(s"[${x._1}] ${x._2 + "-" + x._3} | ${x._4}gp | ${x._5} AC\n\tStrength Required: ${x._6} |" +
        s"${if (x._7 == 1) "Stealth Disadvantage |" else ""} weight: ${x._8}\n"))
      println("[A]dd\n[B]ack")
      val num = readmy().toLowerCase
      if (num == "a") {
        var statement = ""
        val red = () => {
          readmy()+","
        }
        print("Type [Light,Medium,Heavy]: ");
        statement += red()
        print("Name: ");
        statement += red()
        print("Price: ");
        statement += red()
        print("AC: ");
        val ac = readLine();
        statement += ac + ","
        print("Required Strength: ");
        statement += red()
        print("Stealth Disadvantage [1] yes [0] no: ");
        statement += red()
        print("Weight: ");
        val a = readLine()
        if (a == "b") throw new Exception
        else statement += a.capitalize
        val id = insert.Armor(statement)
        if (id != -1){ p = update(p, line, id.toString);p = update(p, "ac", ac);loop = false}
      }
      else {
        var i = true
        list.foreach(x => if (x._1 == numCheck(num)) {
          i = false;
          p = update(p, line, x._1.toString);
          p = update(p, "ac", x._5.toString)
          loop = false
        })
        if (i) {
          list.foreach(x => if (num == x._3.toLowerCase) {
            p = update(p, line, x._1.toString);
            p = update(p, "ac", x._5.toString)
            loop = false
          })
        }
      }
    }while(loop)
    p
  }
  def changeWeapon(line: String,character: Map[String,String]): Map[String,String]={
    var loop = true
    val update = (character: Map[String,String],field: String,value: String) =>{
      loop = false
      (character-field)+(field->value)}
    var p = character
    val list = select.SelectWeapon()
    list.foreach(x =>println(s"[${x._1}] ${x._2} | ${if(x._3 ==0)"Simple"else "Martial"} | ${if(x._4 ==0)"Melee"else "Ranged"}" +
      s"\n\t${x._5} ${x._6} Damage | Weight: ${x._7} | Price: ${x._8} | ${if(x._9 != "")"Properties: "+x._9 else ""}\n"))
    println("[A]dd\n[B]ack")
    do {
      val num = readmy().toLowerCase
      if (num == "a") {
        var statement = ""
        val red = () => {val a = readLine(); if(a =="b") throw new Exception; else a + ","}
        print("Name: ");
        statement += red()
        print("[1]Simple [2]Martial: ");
        statement += red()
        print("[1]Melee [2]Ranged: ");
        statement += red()
        print("Damage: ");
        statement += red()
        print("Damage Type: ");
        statement += red()
        print("Weight: ");
        statement += red()
        print("Cost: ");
        statement += red()
        print("Properties: ");
        val a = readLine()
        if(a=="b") throw new Exception
        else statement +=a.capitalize
        println(statement)
        val id = insert.Weapon(statement)
        if (id != -1) {p=update(p,line,id.toString);loop = false}
      }
      else {
        var i = true
        list.foreach(x => if (x._1 == numCheck(num)){i=false;p=update(p,line,x._1.toString);loop=false})
        if(i){list.foreach(x=>if(num==x._2.toLowerCase){p=update(p,line,x._1.toString);loop=false})
        }
      }
    }while(loop)
    p
  }
  def Spacing(str: String,tabs:Int):String={
    val tab = (x:Int) =>{var a = ""; for(i<-0 until x)a+="\t";a}
    //if(str.length%4 ==3){val thing = "thing"}
    var a = 0
    val b = str.length
    a = tabs - (b / 4)
    if(tabs%2 !=0) {
      if (b % 2 != 0 && b % 4 == 2) a -= 1
      else if (b == 3) a = tabs - 1
      else if (b % 4 == 3) a -= 1
    }
    else{
      if (b % 2 != 0 && b % 4 == 2) a -= 1
      //else if(str.length%2 ==0&&str.length%4 ==0)
    }

    str+tab(a)
  }
  def Add(toAdd:String):Unit={
    toAdd match{
      case "items" =>
        var statement =""
        print("Name: ");statement+=readmy()+","
        print("Price: ");statement+=readmy()+","
        print("Weight: ");statement+=readmy()
        insert.Item(statement)
      case "weapons" =>
        var statement =""
        print("")
        print("Name: ");statement+=readmy()+","
        print("[1]Simple [2]Martial: ");statement+=readmy()+","
        print("[1]Melee [2]Ranged: ");statement+=readmy()+","
        print("Damage: ");statement+=readmy()+","
        print("Damage Type: ");statement+=readmy()+","
        print("Weight: ");statement+=readmy()+","
        print("Cost: ");statement+=readmy()+","
        print("Properties: ");statement+=readmy()
        insert.Weapon(statement)
      case "armor"=>
        var statement=""
        print("Type [Light,Medium,Heavy]: ");statement+=readmy()+","
        print("Name: "); statement+=readmy()+","
        print("Price: "); statement+=readmy()+","
        print("AC: "); statement+=readmy()+","
        print("Required Strength: ");statement+=readmy()+","
        print("Stealth Disadvantage [1] yes [0] no: ");statement+=readmy()+","
        print("Weight: ");statement+=readmy()
        insert.Armor(statement)
      case "races" =>
        print("Race: ");insert.Race(readmy())
      case "classes" =>
        print("Class: ");insert.Class(readmy())
    }
  }
  def Delete(toDelete:String):Unit={
    println("Type the [id] number for the entry you wish to delete")
    val line = numCheck(readmy())
    val entry = select.SelectOne(toDelete,line.toString)
    println(entry)
    if(entry!= "Entry does not exist") {
      println("Are you sure you want to delete this entry?\n[Y]es\n[N]o")
      if (readmy().toLowerCase == "y") delete.Delete(toDelete, line)
    }
  }
}



