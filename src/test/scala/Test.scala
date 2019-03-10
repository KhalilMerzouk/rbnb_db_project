import java.io.{BufferedReader, FileReader}

import com.github.tototoshi.csv.CSVReader
import org.scalatest.FunSuite



class Test extends FunSuite {

  test("testCheckBool") {
    assert(!Main.checkBool("r"))
    assert(!Main.checkBool("ft"))
    assert(Main.checkBool("t"))
  }

  test("testCheckPrice"){
    assert(Main.checkPrice("$120.00"))
    assert(Main.checkPrice("£0.55"))
    assert(!Main.checkPrice("12.5"))
  }

  test("testLongLat"){
    assert(Main.checkLongLat("46.00012"))
    assert(Main.checkLongLat("-46.00012"))
    assert(!Main.checkLongLat("95.00"))
    assert(!Main.checkLongLat("-90.5"))
    assert(!Main.checkLongLat("90.5"))
  }

  test("testCountryCode"){

    assert(Main.checkCountryCodeFormat("ES"))
    assert(!Main.checkCountryCodeFormat("ESP"))

  }

  test("testCheckRate"){

    assert(Main.checkRateFormat("99%"))
    assert(Main.checkRateFormat("1%"))
    assert(Main.checkRateFormat("0%"))
    assert(Main.checkRateFormat("100%"))
    assert(Main.checkRateFormat("0%"))
    assert(!Main.checkRateFormat("-5%"))
    assert(!Main.checkRateFormat("199%"))
    assert(!Main.checkRateFormat("99"))
  }

  test("testCheckDate"){

    assert(Main.checkDateFormate("2012-12-30"))
    assert(Main.checkDateFormate("2012-12-32"))
    assert(!Main.checkDateFormate("2012-13-30"))
    assert(!Main.checkDateFormate("-2012-12-30"))
    assert(!Main.checkDateFormate("2012--12-30"))
  }

  test("testCheckDouble"){

    assert(Main.checkPositiveDouble("12.57"))
    assert(!Main.checkPositiveDouble("-12.57"))
    assert(!Main.checkPositiveDouble("1257"))
  }

  test("testCheckInt"){

    assert(Main.checkPositiveInt("12"))
    assert(!Main.checkPositiveInt("-12"))
    assert(!Main.checkPositiveInt("12.0"))
    assert(!Main.checkPositiveInt("12.034"))
  }

  test("testCheckArray"){

    assert(Main.checkArray("{1,2}"))
    assert(!Main.checkArray("1,2"))
  }

  test("testExtractArray"){

    val line = List("1", "2","{TV,Internet,Wifi,Air conditioning,Wheelchair accessible,Kitchen,Elevator}")
    val array = Array("TV","Internet","Wifi","Air conditioning","Wheelchair accessible","Kitchen","Elevator")
    assert(Main.extractArray(line, 2) sameElements  array)
  }

  test("testComputeSet"){

    val data = List(List("1","2", "{TV,Internet,Wifi,Air conditioning}"), List("1", "2", "{Wheelchair accessible,Kitchen,Elevator}")).par
    val set = Set("TV","Internet","Wifi","Air conditioning","Wheelchair accessible","Kitchen","Elevator")

    assert(Main.computeSet(data, 2) sameElements set)

  }

  test("testFormatData"){

    val amenities = "{TV,Internet,Wifi,\"\"Air conditioning\"\",\"\"Wheelchair accessible\"\",Kitchen,Elevator}"
    val correctAmenities = "{TV,Internet,Wifi,Air conditioning,Wheelchair accessible,Kitchen,Elevator}"
    val data = List(List("$1","\"\"  data  \"\"", "18%"), List("data", amenities,"other data")).par
    val array = List(List("1","  data  ", "18"), List("data", correctAmenities,"other data")).par

    assert(Main.formatData(data) equals array)

  }


  test("end to end test"){

    val dataIn = "../testData/test.csv"
    val dataOut = "../testData/testResult.csv"

    val sensitiveColumnsListing = List((0, "PosInt"), (13, "PosInt"), (16, "dateFormat"), (19, "rateFormat"), (23, "Array"), (26, "countryCode"), (28, "longLat"), (29, "longLat"), (32, "PosInt"), (33, "PosInt"), (34, "PosDouble"), (35, "PosInt"), (37, "Array"), (38, "PosInt"),
      (39, "Price"), (40, "Price"), (41, "Price"), (42, "Price"), (43, "Price"), (44, "PosInt"), (45, "Price"), (46, "PosInt"), (47, "PosInt"), (48, "PosInt"), (49, "PosInt"), (50, "PosInt"), (51, "PosInt"), (52, "PosInt"), (53, "PosInt"),
      (54, "PosInt"), (55, "Bool"), (57, "Bool"), (58, "Bool"))

    val mandatoryColumnsListings = List(0, 1, 2, 14, 13)

    Main.cleanData(dataIn,dataOut,sensitiveColumnsListing,mandatoryColumnsListings)

    val in = new BufferedReader(new FileReader(dataOut))
    val reader = CSVReader.open(in)
    val result = reader.all

    assert(result.length == 1)


  }

}
