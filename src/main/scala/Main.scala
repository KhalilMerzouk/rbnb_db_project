import java.io._

import com.github.tototoshi.csv.{CSVReader, CSVWriter}

import scala.collection.parallel.ParSeq
import scala.io.BufferedSource

/**
  * Program to clean up the data
  */
object Main{

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

    //Perform checks
    val checkedData = checkListing(data).toList

    //Write in file
    writer.writeRow(column)
    writer.writeAll( checkedData  )


   //Close to save ressources
    reader.close
    writer.close()
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

    true

  }


}
