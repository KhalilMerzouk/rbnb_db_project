import java.io._

import com.github.tototoshi.csv.{CSVReader, CSVWriter}

import scala.collection.parallel.ParSeq

/**
  * Program to clean up the data
  */
object Main{

  val nullVal = "NULL"

  val separator = System.lineSeparator()

  def main(args: Array[String]): Unit = {

    println("Main app for cleaning DB")

    //IO Paths
    val filePath = "../dataset/barcelona_listings.csv"
    val outputPath = "../cleanedData/barcelona_listings_cleaned.csv"


    //IO Objects
    val reader = CSVReader.open(new File(filePath))
    val writer = CSVWriter.open(new File(outputPath))

  //Fetch Data
    val dataset = reader.all()

    val column = dataset.head   //column names

    val data = dataset.tail.par

    val completedData = putNUll(data)

    //Perform checks
    val checkedData = checkListing(completedData).toList

    //Write in file
    writer.writeRow(column)
    writer.writeAll(checkedData)


   //Close to save ressources
    reader.close
    writer.close()
  }


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

    primaryKeysOK(l) && fieldsFormatOK(l)   //add here all checks that necessitate to DROP the current line

  }

  /**
    * Check that all primary keys (or mandatory data) are present
    * @param l the csv line to check
    * @return true if all the primary keys are present fasle otherwise
    */
  def primaryKeysOK(l: List[String]):Boolean = {

    val primaryKeysIndex = List(0, 1, 14, 13)    //add indexes of mandatory columns here

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


    val sensitiveColumns = List((0, "PosInt"), (13, "PosInt"), (16, "dateFormat"), (19, "rateFormat"), (26, "countryCode"), (28, "longLat"), (29, "longLat"), (32, "PosInt"), (33, "PosInt"), (34, "PosDouble"), (35, "PosInt"), (38, "PosInt"),
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

    if(s == nullVal) true

    s.forall(Character.isDigit)
  }


  /**
    * Check that a string is a positive double
    * @param s the string to check
    * @return
    */
  def checkPositiveDouble(s: String): Boolean = {

    if(s == nullVal) true

    val split = s.split(".")

    split.size == 2 && split(0).forall(Character.isDigit)

  }


  /**
    * Check that the date format corresponds to yyyy-mm-dd
    * @param s the string to check
    * @return true if the date is correct false otherwise
    */
  def checkDateFormate(s : String): Boolean = {

    if(s == nullVal) true

    val ymd = s.split("-")

    if(ymd.size != 3 || !checkPositiveInt(ymd(0)) || !checkPositiveInt(ymd(1)) || !checkPositiveInt(ymd(2)) || ymd(0).toInt > 2019 || ymd(1).toInt > 24 || ymd(2).toInt > 32) false

    true

  }


  /**
    * Check that the string corresponds to a percentage (e.g : "50%")
    * @param s the string to check
    * @return true if the rate corresponds to the format false otherwise
    */
  def checkRateFormat(s: String): Boolean = {

    if(s == nullVal) true

    if(!s.endsWith("%") || !checkPositiveInt(s.substring(s.size-1)) || s.substring(s.size-1).toInt > 100) false

    true
  }


  /**
    * Check that a string corresponds to an ISO country code (2 letters)
    * @param s the string to check
    * @return true if the strings corresponds to an ISO code false otherwise
    */
  def checkCountryCodeFormat(s: String): Boolean = {

    if(s == nullVal) true

    if(s.size != 2 || !Character.isLetter(s(0)) || !Character.isLetter(s(1))) false

    true
  }

  /**
    * Check that a string corresponds to a latitude or longitude
    * @param s the string to check
    * @return true if it's a valid long/lat false otherwise
    */
  def checkLongLat(s: String): Boolean = {

    if(s == nullVal) true

    val deg = s.split(".")

    if(deg != 2 || !checkPositiveInt(deg(0)) || !checkPositiveInt(deg(1)) || deg(0).toInt > 180 || deg(1).toInt > 180) false

    true

  }

  /**
    * Check that a string represents a price (e.g : $120.00)
    * @param s
    * @return
    */
  def checkPrice(s: String): Boolean = {

    if(s == nullVal) true

   ! Character.isDigit(s.head) && !s.tail.forall(Character.isDigit)

  }

  /**
    * Check that a string represents a boolean (either f or t)
    * @param s the string to check
    * @return true if it' a boolean false otherwise
    */
  def checkBool(s: String): Boolean = s == "f" || s == "t"

}
