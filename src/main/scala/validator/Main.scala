package validator

object Main {

  def main(args: Array[String]): Unit = {
    if (args.length == 0) {
      Console.err.println("Expecting 1 argument but found 0. Please provide path to input CSV file eg \"resources/checksums.csv\" ")
      System.exit(-1)
    }
    else {
      generateCsvReport(args(0)).foreach(println)
    }
  }

  def generateCsvReport(pathToInputFile: String): Seq[String] = {
    val request = ChecksumsFileLoader.load(pathToInputFile)
    val csvResults = request
      .map(requests => ChecksumRequestValidator.validate(requests))
      .map(results => ChecksumsResultFormatter.generateCsvReport(results))
    csvResults match {
      case Left(error) => List(s"Error generating report: ${error.toString}")
      case Right(csvReport) => csvReport.lines
    }
  }
}
