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

    val primaryKeysIndex = List(0, 13)    //add indexes of mandatory columns here

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
    true
  }

}
