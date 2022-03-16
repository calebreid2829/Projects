import scala.io.StdIn
import scala.io.StdIn.readLine
case class Item(itemid:Int,name:String,cost:Int,weight:Double)
case class Race(raceid:Int,race:String)
case class Class(classid:Int,clss:String)
case class Weapon(weaponid:Int,name:String,simple_martial:Boolean,melee_ranged:Boolean,damage:String,damage_type:String
                  ,weight:Double,cost:Int,properties:String)
case class Armor(armorid:Int,armor_type:String,name:String,cost:Int,ac:Int,strength:Int,stealth_disadvantage:Boolean,weight:Double)
case class Character(characterid:Int,var name:String,raceid:Int,classid:Int,level:Int,alignment:String,str:Int,dex:Int,con:Int
                    ,int:Int,wis:Int,char:Int,weaponid:Int,armorid:Int,ac:Int,hp:Int,hitdice:String,speed:Int)
class Back() extends Exception(){}
