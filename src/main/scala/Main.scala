import java.io._
import java.util.concurrent.ForkJoinPool

import com.github.tototoshi.csv.{CSVReader, CSVWriter}

import scala.collection.parallel.{ForkJoinTasks, ParSeq, ParSet, Task}
import scala.concurrent._
/**
  * Program to clean up the data
  */
object Main{

  val nullVal = "NULL"

  val separator = System.lineSeparator()

  def main(args: Array[String]): Unit = {

    println("Main app for cleaning DB")

    //IO Paths
    val barcIn = "../dataset/barcelona_listings.csv"
    val barcOut = "../cleanedData/barcelona_listings_cleaned.csv"
    val berIn = "../dataset/berlin_listings_filtered.csv"
    val berOut = "../cleanedData/berlin_listings_cleaned.csv"
    val madIn = "../dataset/madrid_listings_filtered.csv"
    val madOut = "../cleanedData/madrid_listings_cleaned.csv"


    //prepare tasks
    val tasks = List((barcIn, barcOut),(berIn, berOut), (madIn, madOut)).par


    //launch computation
    tasks.foreach[Unit](path => cleanDataset(path._1, path._2))


    def cleanDataset(pathIn: String, pathOut: String): Unit = {

      //IO Objects
      val reader = CSVReader.open(new File(pathIn))
      val writer = CSVWriter.open(new File(pathOut))

      //Fetch Data
      val dataset = reader.all()

      val column = dataset.head   //column names

      val data = dataset.tail.par

      val completedData = putNUll(data)   //put NULL instead of empty strings

      //Perform integrity checks
      val checkedData = checkListing(completedData)

      //remove % $ and "" from data
      val formattedData = formatData(checkedData).toList


      //TODO insert data into DB

      //Write in file
      writer.writeRow(column)
      writer.writeAll(formattedData)


      //Close to save ressources
      reader.close
      writer.close()

    }






  }


  /**
    * Delete "$", "%" and double quotes from the data
    * @param data the data to check
    * @return a cleaned version of the dataset
    */
  def formatData(data: ParSeq[List[String]]): ParSeq[List[String]] = {


    def replaceInLine(line : List[String]): List[String] ={

      for(column <- line) yield {

        if(column.endsWith("%")) column.take(column.length - 1)

        else if(column.contains("\"\"")) column.split("\"\"").fold("")(_++_)  //TODO not working ??

        else if(column.startsWith("$")) column.tail

        else column
      }

    }

    for(line <- data) yield replaceInLine(line)
  }


  /**
    * Method to extract the list of selections (e.g amenities) from a listing
    * @param line a line from the listing dataset
    * @return the list for the given selection
    */
  def extractArray(line: List[String], column: Int): Array[String] = {

    val splitted = line(column).split(',')

    Array(splitted.head.tail) ++ splitted.tail.reverse.tail.reverse ++ Array(splitted.reverse.head.takeWhile(_ != '}'))
  }



  /**
    * Compute the set of a certain selection (e.g amenities)
    * @param data the listings
    * @return the set for the given selection
    */
  def computeSet(data : ParSeq[List[String]], column: Int): ParSet[String] = (for(line <- data) yield extractArray(line, column)).flatten.toSet




  /**
    * Replace missing values (empty strings) by "NULL"
    * @param seq the sequance of csv lines
    * @return a sequence of csv lines where missing values are replaced by "NULL"
    */
  def putNUll(seq: ParSeq[List[String]]): ParSeq[List[String]] = {

    def replaceInLine(line : List[String]): List[String] ={

      for(data <- line) yield if(data.isEmpty)  nullVal else data

    }

    for(line <- seq
    )yield replaceInLine(line)

  }


  /**
    * Method for filtering bad lines (missing essential information etc...)
    * @param b the buffered source from the file
    * @return an iterator on a sequence of strings
    */
  def checkListing(l : ParSeq[List[String]]): ParSeq[List[String]] = for(list <- l if(check(list))) yield list


  /**
    *Mdethod for checking if an entry is valid
    * @param l the list of all fields
    * @return true if the data is correct false otherwise
    */
  def check(l: List[String]):Boolean = {

    if(primaryKeysOK(l) && fieldsFormatOK(l)) return true   //add here all checks that necessitate to DROP the current line

    else
      println(l.toString())
      false
  }

  /**
    * Check that all primary keys (or mandatory data) are present
    * @param l the csv line to check
    * @return true if all the primary keys are present fasle otherwise
    */
  def primaryKeysOK(l: List[String]):Boolean = {

    val primaryKeysIndex = List(0, 1, 2, 14, 13)    //add indexes of mandatory columns here

    for(i <- primaryKeysIndex){
      if(l(i) == nullVal) false
    }

    true

  }

  /**
    * Check that the format of the data is consistent (date, strings, numbers etc...)
    * @param l the csv line to check
    * @return true if the data is consistent false otherwise
    */
  def fieldsFormatOK(l: List[String]):Boolean = {


    val sensitiveColumns = List((0, "PosInt"), (13, "PosInt"), (16, "dateFormat"), (19, "rateFormat"), (26, "countryCode"), (28, "longLat"), (29, "longLat"), (32, "PosInt"), (33, "PosInt"), (34, "PosDouble"), (35, "PosInt"), (37, "Array"), (38, "PosInt"),
      (39, "Price"), (40, "Price"), (41, "Price"), (42, "Price"), (43, "Price"), (44, "PosInt"), (45, "Price"), (46, "PosInt"), (47, "PosInt"), (48, "PosInt"), (49, "PosInt"), (50, "PosInt"), (51, "PosInt"), (52, "PosInt"), (53, "PosInt"),
      (54, "PosInt"), (55, "Bool"), (57, "Bool"), (58, "Bool"))


    for(column <- sensitiveColumns){
      column._2 match{
        case "PosInt" =>
          if(!checkPositiveInt(l(column._1))) false

        case "PosDouble" =>
          if(!checkPositiveDouble(l(column._1))) false

        case "dateFormat" =>
          if(!checkDateFormate(l(column._1))) false

        case "rateFormat" =>
          if(!checkRateFormat(l(column._1))) false

        case "countryCode" =>
          if(!checkCountryCodeFormat(l(column._1))) false

        case "longLat" =>
          if(!checkLongLat(l(column._1))) false

        case "Price" =>
          if(!checkPrice(l(column._1))) false

        case "Bool" =>
          if(!checkBool(l(column._1))) false

        case "Array" =>
          if(!checkArray(l(column._1))) false

      }
    }

    true
  }

  /**
    * Check that a string is a positive integer (no "-" and all characters are digits)
    * @param s the string to check
    * @return true if the string is a positive integer false otherwise
    */
  def checkPositiveInt(s: String): Boolean = {

    if(s == nullVal) return true

    s.forall(Character.isDigit)
  }


  /**
    * Check that a string is a positive double
    * @param s the string to check
    * @return
    */
  def checkPositiveDouble(s: String): Boolean = {

    if(s == nullVal) return true

    val split = s.split('.')

    split.size == 2 && split(0).forall(Character.isDigit)

  }


  /**
    * Check that the date format corresponds to yyyy-mm-dd
    * @param s the string to check
    * @return true if the date is correct false otherwise
    */
  def checkDateFormate(s : String): Boolean = {

    if(s == nullVal) return true

    val ymd = s.split('-')

    if(ymd.size != 3 || !checkPositiveInt(ymd(0)) || !checkPositiveInt(ymd(1)) || !checkPositiveInt(ymd(2)) || ymd(0).toInt > 2019 || ymd(1).toInt > 12 || ymd(2).toInt > 32) return false

    true

  }


  /**
    * Check that the string corresponds to a percentage (e.g : "50%")
    * @param s the string to check
    * @return true if the rate corresponds to the format false otherwise
    */
  def checkRateFormat(s: String): Boolean = {

    if(s == nullVal) return true

    if(!s.endsWith("%") || !checkPositiveInt(s.take(s.length -1)) || s.take(s.length -1).toInt > 100) return false

    true
  }


  /**
    * Check that a string corresponds to an ISO country code (2 letters)
    * @param s the string to check
    * @return true if the strings corresponds to an ISO code false otherwise
    */
  def checkCountryCodeFormat(s: String): Boolean = {

    if(s == nullVal) return true

    if(s.length != 2 || !Character.isLetter(s(0)) || !Character.isLetter(s(1))) return false

    true
  }

  /**
    * Check that a string corresponds to a latitude or longitude
    * @param s the string to check
    * @return true if it's a valid long/lat false otherwise
    */
  def checkLongLat(s: String): Boolean = {

    if(s == nullVal) return true

    val deg = s.split('.')

    if(deg.size != 2 || (!checkPositiveInt(deg(0)) && !(deg(0).startsWith("-") && checkPositiveInt(deg(0).tail))) || !checkPositiveInt(deg(1)) || BigInt(deg(0)) > 90 || BigInt(deg(0)) < -90 || (BigInt(deg(1)) != 0 && abs(deg(0).toInt) == 90)) return false

    true

  }

  /**
    * Check that a string represents a price (e.g : $120.00)
    * @param s
    * @return
    */
  def checkPrice(s: String): Boolean = {

    if(s == nullVal) return true

   ! Character.isDigit(s.head) && !s.tail.forall(Character.isDigit)

  }

  /**
    * Check that a string represents a boolean (either f or t)
    * @param s the string to check
    * @return true if it' a boolean false otherwise
    */
  def checkBool(s: String): Boolean = s == "f" || s == "t"


  /**
    * Check that the string represents an array
    * @param s the string to check
    * @return true if the string is an array false otherwise
    */
  def checkArray(s: String): Boolean = s.startsWith("{") && s.endsWith("}")


  /**
    * Compute absolute value
    * @param i integer
    * @return the absolute value of i
    */
  def abs(i: Int): Int = if(i >= 0) i else -i

}
