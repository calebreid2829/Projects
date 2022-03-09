import java.io.File
import scala.io.Source
import scala.util.matching.Regex
import scala.io.StdIn.readLine

object CharacterSheet {
  val db = new Database()
  def main(args: Array[String]): Unit = {
    val insert = new InsertRow()
    var loop = true
    val wd = new Regex("[^0-9,]+")
    val str = "bob,violence,4,2"
    do{
      clear()

      val line = readLine().toLowerCase
      val splitLine = line.split(" ")
      if(line.split(" ")(0).toLowerCase == "insert") {
        if (line.split(" ")(1).toLowerCase == "race"){do{print("Insert Race Name: ")}while(insert.Race(readLine()))}
        else if (line.split(" ")(1).toLowerCase == "item"){do{print("Insert Item Name, Cost, and Weight: ")}while(insert.Item(readLine()))}
        else if(line.split(" ")(1).toLowerCase == "class"){do{print("Insert Class Name: ")}while(insert.Class(readLine()))}
        else if(line.split(" ")(1).toLowerCase == "armor"){do{print("Insert Armor Type,name,Cost,AC,Strength,Stealth Disadvantage, And Weight: ")}while(insert.Armor(readLine()))}
      }
      else if(line.split(" ")(0).toLowerCase == "show"){
        if(line.split(" ")(1).toLowerCase == "items"){showItems()}
        else if(line.split(" ")(1).toLowerCase == "races"){showRaces()}
        else if(line.split(" ")(1).toLowerCase == "classes"){showClasses()}
        else if(splitLine(1) == "armor"){showArmor()}
      }
      else if(line.toLowerCase == "sheet")showSheet()
      else if(line.toLowerCase == "help")showOptions()
      else if(line.toLowerCase == "quit") loop = false
    }while(loop)
  }
  def getBonus(stat: String): String={
    if(stat.toInt-10 < 0) math.floor((stat.toDouble-10)/2).toInt.toString
    else "+" + ((math.floor((stat.toDouble-10)/2).toInt))
  }
  def showSheet(): Unit={
    val read = new FileRead()
    val p = read.read("Sheet.json")
    print(s"\n ${p("name")}  |  Level ${p("level")} ${p("race")} ${p("class")}  |  ${p("alignment")}")
    print(s"\n HP ${p("hp")}  |  Hit Dice ${p("hitDice")}  |  Death Save - Success[][][] - Failure[][][]")
    print(s"\n AC ${p("ac")}  |  Initiative ${getBonus(p("dex"))}  |  Speed ${p("speed")}")
    print(s"\n Strength ${p("str")}[${getBonus(p("str"))}]\t|  Athletics")
    print(s"\n Dexterity ${p("dex")}[${getBonus(p("dex"))}]\t|  Acrobatics - Sleight of Hand - Stealth")
    print(s"\n Constitution ${p("con")}[${getBonus(p("con"))}]|")
    print(s"\n Intelligence ${p("int")}[${getBonus(p("int"))}]|  Arcana - History - Investigation - Nature - Religion")
    print(s"\n Wisdom ${p("wis")}[${getBonus(p("wis"))}]\t\t|  Animal Handling - Insight - Medicine - Perception - Survival")
    print(s"\n Charisma ${p("char")}[${getBonus(p("char"))}]\t|  Deception - Intimidation - Performance - Persuasion\n")
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

}


