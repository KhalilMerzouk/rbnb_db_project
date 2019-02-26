import java.io.{BufferedReader, BufferedWriter, FileReader, FileWriter}

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
    val filePath = "/home/hedi/Documents/EPFL/sem_6/intro_db/DB_project/dataset/barcelona_listings.csv"
    val outputPath = "/home/hedi/Documents/EPFL/sem_6/intro_db/DB_project/cleanedData/barcelona_listings_cleaned.csv"


    //IO Objects
    val bufferedSource = io.Source.fromFile(filePath)
    val outputFile = new BufferedWriter(new FileWriter(outputPath))


    //concatenate lines that were separated by a line separator inside the data
    def collectList(s: Stream[String], acc: List[String], counter: Int): List[String] = s match{

      case Stream.Empty => acc

      case h +: Stream.Empty => h :: acc

      case h +: tail =>
        if(h.split(",").size < 58) collectList((s.head + s.tail.head) +: s.tail.tail, acc, counter + 1)   //count the number of columns to know if the line is really finished
        else collectList(s.tail, s.head :: acc, 0)

    }

    val stream = bufferedSource.getLines().toStream

    val column = stream.head
    val data = collectList(stream.tail, Nil,0).par


    //Perform checks
    val checkedData = checkListing(data)

    //Write in file
    outputFile.write(column + separator)
    checkedData.foreach(s => outputFile.write(s))


    bufferedSource.close
    outputFile.close()
  }


  /**
    * Method for filtering bad lines (missing essential information etc...)
    * @param b the buffered source from the file
    * @return an iterator on a sequence of strings
    */
  def checkListing(l : ParSeq[String]): ParSeq[String] = for(line <- l if(checkEmpty(line))) yield line + separator


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
