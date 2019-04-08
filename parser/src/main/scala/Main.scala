import java.io._

import com.github.tototoshi.csv.{CSVReader, CSVWriter}
import scala.collection.parallel.{ParSeq, ParSet}
import java.sql.DriverManager
import java.sql.Connection


/**
  * Program to clean up the data
  */
object Main{

  val nullVal = "NULL"

  val separator = System.lineSeparator()

  def main(args: Array[String]): Unit = {

    println("Main app for cleaning DB")

    cleanListings()
    println("Listings cleaned")
    cleanCalendar()
    println("Calendars cleaned")
    cleanReviews()
    println("Reviews cleaned")




    //Creation of new csv files for lists

    createLists()

    println("Finished generating csv for amenities and host verifications")


    createHost()

    println("Finished creating csv for hosts")

    createReviewer()

    println("Finished creating csv for reviewers")


  }




  /**
    * Method to generate csv file that contains all data related to hosts
    * copy past with createHost.... feel free to improve :)
    */
  def createReviewer():Unit = {

    //prepare IO objects

    val barcIn = "../../cleanedData/barcelona_reviews_cleaned.csv"
    val berIn = "../../cleanedData/berlin_reviews_cleaned.csv"
    val madIn = "../../cleanedData/madrid_reviews_cleaned.csv"

    val hosts = "../../cleanedData/reviewers.csv"

    val hostsWriter = new BufferedWriter(new FileWriter(hosts))

    val writer = CSVWriter.open(hostsWriter)

    val in1 = new BufferedReader(new FileReader(barcIn))
    val in2 = new BufferedReader(new FileReader(berIn))
    val in3 = new BufferedReader(new FileReader(madIn))


    val inputs = List(in1, in2, in3)

    val columnIndex = List(3, 4)

    //write column names

    val columnNames = List("reviewer_id", "reviewer_name")

    writer.writeRow(columnNames)


    var reviewerSet: Set[String] = Set.empty

    //read all input files for listings
    for(in <- inputs) {


      //IO Objects
      val reader = CSVReader.open(in)


      val it = reader.iterator

      it.next //don't take the first row



      val batchSize = 50

      def takeBatch(it: Iterator[Seq[String]], acc: List[List[String]], count: Int): List[List[String]] = {
        if (count < batchSize && it.hasNext) takeBatch(it, it.next().toList :: acc, count + 1)
        else acc
      }


      while (it.hasNext) {

        //take a few line (so there will be no memory problems)
        val data = takeBatch(it, Nil, 0)

        for(l <- data  if !reviewerSet.contains(l(columnIndex.head)) ){   //if the host isn't already taken

          reviewerSet += l(columnIndex.head)

          writer.writeRow(for(c <- columnIndex) yield l(c))

        }


      }

    }

    in1.close()
    in2.close()
    in3.close()
    writer.close()


  }


  /**
    * Method to generate csv file that contains all data related to hosts
    */
  def createHost():Unit = {

    //prepare IO objects

    val barcIn = "../../cleanedData/barcelona_listings_cleaned.csv"
    val berIn = "../../cleanedData/berlin_listings_cleaned.csv"
    val madIn = "../../cleanedData/madrid_listings_cleaned.csv"

    val hosts = "../../cleanedData/hosts.csv"

    val hostsWriter = new BufferedWriter(new FileWriter(hosts))

    val writer = CSVWriter.open(hostsWriter)

    val in1 = new BufferedReader(new FileReader(barcIn))
    val in2 = new BufferedReader(new FileReader(berIn))
    val in3 = new BufferedReader(new FileReader(madIn))


    val inputs = List(in1, in2, in3)

    val columnIndex = List(13,14,15,16, 17 , 18, 19 ,20, 21, 22, 23)

    //write column names

    val columnNames = List("host_id","host_url","host_name","host_since", "host_about", "host_response_time", "host_response_rate","host_thumbnail_url", "host_picture_url" , "host_neighbourhood" )

    writer.writeRow(columnNames)


    var hostSet: Set[String] = Set.empty

    //read all input files for listings
    for(in <- inputs) {


      //IO Objects
      val reader = CSVReader.open(in)


      val it = reader.iterator

      it.next //don't take the first row



      val batchSize = 50

      def takeBatch(it: Iterator[Seq[String]], acc: List[List[String]], count: Int): List[List[String]] = {
        if (count < batchSize && it.hasNext) takeBatch(it, it.next().toList :: acc, count + 1)
        else acc
      }


      while (it.hasNext) {

        //take a few line (so there will be no memory problems)
        val data = takeBatch(it, Nil, 0)

        for(l <- data  if !hostSet.contains(l(columnIndex.head)) ){   //if the host isn't already taken

          hostSet += l(columnIndex.head)

          writer.writeRow(for(c <- columnIndex) yield l(c))

        }


      }

      }

    in1.close()
    in2.close()
    in3.close()
    writer.close()


  }


  /**
    *
    * Method to generate the peak lists for amenities and host verifications
    * Not pretty but working.
    * A lot of code is duplicated from the cleanData function.... feel free to improve the code :)
    */
  def createLists():Unit = {

    //initialize paths

    val barcIn = "../../cleanedData/barcelona_listings_cleaned.csv"
    val berIn = "../../cleanedData/berlin_listings_cleaned.csv"
    val madIn = "../../cleanedData/madrid_listings_cleaned.csv"

    val amen = "../../cleanedData/amenities.csv"
    val verif = "../../cleanedData/verification.csv"
    val amenSet = "../../cleanedData/amenSet.csv"
    val verifSet = "../../cleanedData/verifSet.csv"

    //writers and writers

    val amenities = new BufferedWriter(new FileWriter(amen))
    val verification = new BufferedWriter(new FileWriter(verif))
    val amenitiesSet = new BufferedWriter(new FileWriter(amenSet))
    val verificationSet = new BufferedWriter(new FileWriter(verifSet))

    val in1 = new BufferedReader(new FileReader(barcIn))
    val in2 = new BufferedReader(new FileReader(berIn))
    val in3 = new BufferedReader(new FileReader(madIn))


    val inputs = List(in1, in2, in3)

    //indexes for amenities and verifications
    val columnAmen = 37
    val columnVerif = 23

    //compute set of amenities and verifications with different readers or else file pointers will be at the end of the file for the next part !!!!!!!!!!!!!!!!!!!
    val in1S = new BufferedReader(new FileReader(barcIn))
    val in2S = new BufferedReader(new FileReader(berIn))
    val in3S = new BufferedReader(new FileReader(madIn))
    val bar_list = CSVReader.open(in1S).all().tail.par
    val ber_list = CSVReader.open(in2S).all().tail.par
    val mad_list = CSVReader.open(in3S).all().tail.par

    //compute the set of amenities and host verifications and generate an ID for them
    val aSet = computeSet(bar_list, columnAmen) ++  computeSet(ber_list, columnAmen) ++ computeSet(mad_list, columnAmen)
    val vSet = computeSet(bar_list, columnVerif) ++ computeSet(ber_list, columnVerif) ++ computeSet(mad_list, columnVerif)

    in1S.close()
    in2S.close()
    in3S.close()


    //map with id's generated
    val aMap = aSet.zip(1 to aSet.size).map(p => p._1.toString -> p._2).toMap
    val vMap = vSet.zip(1 to vSet.size).map(p =>p._1.toString -> p._2).toMap

    //prepare writers

    val writer1 = CSVWriter.open(amenitiesSet)
    val writer2 = CSVWriter.open(verificationSet)
    val writer3 = CSVWriter.open(amenities)
    val writer4 = CSVWriter.open(verification)


    //prepare columns titles
    val column1 = Seq[String]("amenity_id","amenity_name")
    val column2 = Seq[String]("verification_id","verification_name")
    val column3 = Seq[String]("listing_id", "amenity_id")
    val column4 = Seq[String]("listing_id", "verification_id")

    //write column titles
    writer1.writeRow(column1)
    writer2.writeRow(column2)
    writer3.writeRow(column3)
    writer4.writeRow(column4)

    //write list of amenities and verifications with a generated id
    writer1.writeAll(aSet.zip(1 to aSet.size).map(p => List(p._2.toString , p._1)).toList)
    writer2.writeAll(vSet.zip(1 to vSet.size).map(p => List(p._2.toString , p._1)).toList)


    //read all input files for listings
    for(in <- inputs) {


      //IO Objects
      val reader = CSVReader.open(in)


      val it = reader.iterator

      it.next //don't take the first row



      val batchSize = 50

      def takeBatch(it: Iterator[Seq[String]], acc: List[List[String]], count: Int): List[List[String]] = {
        if (count < batchSize && it.hasNext) takeBatch(it, it.next().toList :: acc, count + 1)
        else acc
      }


      while (it.hasNext) {

        //take a few line (so there will be no memory problems)
        val data = takeBatch(it, Nil, 0).par


        //extract the amenities and verifications (with their corresponding listing id)
        val (amenities, verifications) = extractData(data)


        //Write in file (amenity_id -> listing_id)

        amenities.foreach{
          case l => writer3.writeAll(l.map(a => List(a._1, aMap(a._2))))
        }


        //Write in file (verification_id -> listing_id)
       verifications.foreach(

         v => writer4.writeAll(v.map(v => List(v._1, vMap(v._2))))

       )

      }


    }

    //free ressources

    amenities.close()
    verification.close()
    amenitiesSet.close()
    verificationSet.close()
    in1.close()
    in2.close()
    in3.close()

  }

  /**
    *
    * Extract the amenities and verifications for each listing and also compute the set of amenities and verifications
    * @param data chunck of csv data
    * @return 4-tuple containing the amenities, verifications, amenity set, vverification set
    */
  def extractData(data: ParSeq[List[String]]):(List[List[(String, String)]], List[List[(String, String)]]) = {


    val columnAmen = 37
    val columnVerif = 23



    val data1 = data.foldLeft[List[List[(String, String)]]](Nil){

      case (acc, l) =>

          extractArray(l, columnAmen).filter(s => !s.isEmpty).toList.map(e => (l(0), e)) :: acc

    }


    val data2 = data.foldLeft[List[List[(String, String)]]](Nil){

      case (acc, l) =>
        extractArray(l, columnVerif).filter(s => !s.isEmpty).toList.map(e => (l(0) , e)) :: acc
    }



    (data1, data2)
  }

  /**
    * Method to call to clean the Reviews files
    */
  def cleanReviews():Unit = {

    //IO Paths
    val barcIn = "../../dataset/barcelona_reviews.csv"
    val barcOut = "../../cleanedData/barcelona_reviews_cleaned.csv"
    val berIn = "../../dataset/berlin_reviews.csv"
    val berOut = "../../cleanedData/berlin_reviews_cleaned.csv"
    val madIn = "../../dataset/madrid_reviews.csv"
    val madOut = "../../cleanedData/madrid_reviews_cleaned.csv"

    //things to check
    val sensitiveColumnsListing = List((0, "PosInt"), (1, "PosInt"), (2, "dateFormat"), (3, "PosInt"))

    val mandatoryColumnsListings = List(0, 1, 2, 3, 4, 5)

    //prepare tasks
    val tasks = List((barcIn, barcOut),(berIn, berOut), (madIn, madOut)).par


    //launch computation
    tasks.foreach[Unit](path => cleanData(path._1, path._2, sensitiveColumnsListing, mandatoryColumnsListings))

  }

  /**
    * Method to call to clean the Calendar files
    */
  def cleanCalendar():Unit = {

    //IO Paths
    val barcIn = "../../dataset/barcelona_calendar.csv"
    val barcOut = "../../cleanedData/barcelona_calendar_cleaned.csv"
    val berIn = "../../dataset/berlin_calendar.csv"
    val berOut = "../../cleanedData/berlin_calendar_cleaned.csv"
    val madIn = "../../dataset/madrid_calendar.csv"
    val madOut = "../../cleanedData/madrid_calendar_cleaned.csv"

    //things to check
    val sensitiveColumnsListing = List((0, "PosInt"), (1, "dateFormat"), (2, "Bool"), (3, "Price"))

    val mandatoryColumnsListings = List(0, 1, 2)  //price may be optional

    //prepare tasks
    val tasks = List((barcIn, barcOut),(berIn, berOut), (madIn, madOut)).par


    //launch computation
    tasks.foreach[Unit](path => cleanData(path._1, path._2, sensitiveColumnsListing, mandatoryColumnsListings))

  }

  /**
    * Method to call to clean the Listings files
    */
  def cleanListings():Unit = {

    //IO Paths
    val barcIn = "../../dataset/barcelona_listings.csv"
    val barcOut = "../../cleanedData/barcelona_listings_cleaned.csv"
    val berIn = "../../dataset/berlin_listings_filtered.csv"
    val berOut = "../../cleanedData/berlin_listings_cleaned.csv"
    val madIn = "../../dataset/madrid_listings_filtered.csv"
    val madOut = "../../cleanedData/madrid_listings_cleaned.csv"

    //things to check
    val sensitiveColumnsListing = List((0, "PosInt"), (13, "PosInt"), (16, "dateFormat"), (19, "rateFormat"), (23, "Array"), (26, "countryCode"), (28, "longLat"), (29, "longLat"), (32, "PosInt"), (33, "PosDouble"), (34, "PosInt"), (35, "PosInt"), (37, "Array"), (38, "PosInt"),
      (39, "Price"), (40, "Price"), (41, "Price"), (42, "Price"), (43, "Price"), (44, "PosInt"), (45, "Price"), (46, "PosInt"), (47, "PosInt"), (48, "PosInt"), (49, "PosInt"), (50, "PosInt"), (51, "PosInt"), (52, "PosInt"), (53, "PosInt"),
      (54, "PosInt"), (55, "Bool"), (57, "Bool"), (58, "Bool"))

    val mandatoryColumnsListings = List(0, 1, 2, 13, 14, 15)

    //prepare tasks
    val tasks = List((barcIn, barcOut),(berIn, berOut), (madIn, madOut)).par


    //launch computation
    tasks.foreach[Unit](path => cleanData(path._1, path._2, sensitiveColumnsListing, mandatoryColumnsListings))

  }


  /**
    * Method that will read, clean and write the cleaned data to files (or DB)
    * @param pathIn  path to the input file
    * @param pathOut path to the output file
    */
  def cleanData(pathIn: String, pathOut: String, sensitiveColumns: List[(Int, String)], mandatory: List[Int]): Unit = {

    val out = new BufferedWriter(new FileWriter(pathOut))
    val in = new BufferedReader(new FileReader(pathIn))

    //IO Objects
    val reader = CSVReader.open(in)
    val writer = CSVWriter.open(out)
    val it = reader.iterator

    val batchSize = 50

    def takeBatch(it: Iterator[Seq[String]], acc: List[List[String]], count: Int): List[List[String]] = {
      if(count < batchSize && it.hasNext) takeBatch(it, it.next().toList :: acc, count + 1)
      else acc
    }


    val column = it.next()   //column names
    writer.writeRow(column)

    while(it.hasNext){

      //take a few line (so there will be no memory problems)
      val data = takeBatch(it, Nil, 0).par


      //put NULL instead of empty strings
      val completedData = putNUll(data)


      //Perform integrity checks
      val checkedData = checkLines(completedData, sensitiveColumns, mandatory)


      //remove % $ , EOL and "" from data
      val formattedData = formatData(checkedData).toList


      //Write in file
      writer.writeAll(formattedData)
    }

    out.close()
    in.close()

  }



  /**
    * Delete "$", "%", line separator and quotes from the data
    * the function could be recursive to check all possible cases (instead of else if)
    * but for this particular dataset it's not a problem
    * @param data the data to check
    * @return a cleaned version of the dataset
    */
  def formatData(data: ParSeq[List[String]]): ParSeq[List[String]] = {


    def replaceInLine(line : List[String]): List[String] ={

      for(column <- line) yield {
        if(column.contains(separator)) column.split(separator).fold("")((a,b) => a + " " + b)    //replace end of line by whitespace

        else if(column.endsWith("%")) column.take(column.length - 1)

        else if(column.startsWith("$")) column.tail

        else if(column.trim.startsWith("'") && column.trim.endsWith("'")) column.tail.drop(column.tail.length -1)

        else if(column.contains("\"")) column.split("\"").fold("")(_++_)

        else column.trim
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

    if(line.isEmpty || line(column).isEmpty || line(column).size < 3) Array.empty

    line(column).tail.reverse.tail.reverse.split(',')


  }



  /**
    * Compute the set of a certain selection (e.g amenities)
    * @param data the listings
    * @return the set for the given selection
    */
  def computeSet(data : ParSeq[List[String]], column: Int): ParSet[String] = (for(line <- data) yield extractArray(line, column).filter(s => !s.isEmpty)).flatten.toSet




  /**
    * Replace missing values (empty strings) by "NULL"
    * @param seq the sequance of csv lines
    * @return a sequence of csv lines where missing values are replaced by "NULL"
    */
  def putNUll(seq: ParSeq[List[String]]): ParSeq[List[String]] = {

    def replaceInLine(line : List[String]): List[String] ={

      for(data <- line) yield if(data.isEmpty || data == "N/A")  nullVal else data

    }

    for(line <- seq) yield replaceInLine(line)

  }


  /**
    * Method for filtering bad lines (missing essential information etc...)
    * @param b the buffered source from the file
    * @return an iterator on a sequence of strings
    */
  def checkLines(l : ParSeq[List[String]], sensitiveColumns: List[(Int, String)], mandatory: List[Int]): ParSeq[List[String]] = for(list <- l if check(list, sensitiveColumns, mandatory)) yield list


  /**
    *Mdethod for checking if an entry is valid
    * @param l the list of all fields
    * @return true if the data is correct false otherwise
    */
  def check(l: List[String],sensitiveColumns: List[(Int, String)], mandatory: List[Int]):Boolean = primaryKeysOK(l, mandatory) && fieldsFormatOK(l, sensitiveColumns)

  /**
    * Check that all primary keys (or mandatory data) are present
    * @param l the csv line to check
    * @return true if all the primary keys are present fasle otherwise
    */
  def primaryKeysOK(l: List[String],mandatory: List[Int]):Boolean = mandatory.forall(i => !(l(i) == nullVal || l(i) == ""))




  /**
    * Check that the format of the data is consistent (date, strings, numbers etc...)
    * @param l the csv line to check
    * @return true if the data is consistent false otherwise
    */
  def fieldsFormatOK(l: List[String], sensitiveColumns: List[(Int, String)]):Boolean = {


    def checkHelper(column: (Int, String)):Boolean =  column._2 match{

      case "PosInt" => checkPositiveInt(l(column._1))

      case "PosDouble" => checkPositiveDouble(l(column._1))

      case "dateFormat" => checkDateFormate(l(column._1))

      case "rateFormat" => checkRateFormat(l(column._1))

      case "countryCode" => checkCountryCodeFormat(l(column._1))

      case "longLat" => checkLongLat(l(column._1))

      case "Price" => checkPrice(l(column._1))

      case "Bool" => checkBool(l(column._1))

      case "Array" => checkArray(l(column._1))

      case _ => println("ERROR"); false

    }

    sensitiveColumns.forall(checkHelper)

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

    split.size == 2 && split(0).forall(Character.isDigit) && split(1).forall(Character.isDigit)

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
  def checkArray(s: String): Boolean = (s.startsWith("{") && s.endsWith("}")) || (s.startsWith("[") && s.endsWith("]"))


  /**
    * Compute absolute value
    * @param i integer
    * @return the absolute value of i
    */
  def abs(i: Int): Int = if(i >= 0) i else -i

}
