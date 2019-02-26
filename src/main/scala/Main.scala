import java.io.{BufferedWriter, FileWriter}

import scala.io.BufferedSource

/**
  * Program to clean up the data
  */
object Main{


  def main(args: Array[String]): Unit = {

    println("Main app for cleaning DB")

    val filePath = "/home/hedi/Documents/EPFL/sem_6/intro_db/DB_project/dataset/barcelona_listings.csv"

    val outputPath = "/home/hedi/Documents/EPFL/sem_6/intro_db/DB_project/cleanedData/barcelona_listings_cleaned.csv"

    val bufferedSource = io.Source.fromFile(filePath)
    val outputFile = new BufferedWriter(new FileWriter(outputPath))



    val checkedData = checkListing(bufferedSource)

    checkedData.foreach(s => outputFile.write(s))


    bufferedSource.close
    outputFile.close()

  }


  /**
    * Method for filtering bad lines (missing essential information etc...)
    * @param b the buffered source from the file
    * @return an iterator on a sequence of strings
    */
  def checkListing(b: BufferedSource): Iterator[String] = for(line <- b.getLines if(checkEmpty(line))) yield line


  /**
    * Method for checking if an essential field is missing
    * @param line csv line to ckeck
    * @return true if the line is correct, false otherwise
    */
  def checkEmpty(line: String):Boolean = {

    val cols = line.split(",").map(_.trim).toList

    if(cols.contains("")) false else true

  }


}
