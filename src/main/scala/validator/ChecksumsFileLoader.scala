package validator

import java.io.{File, FileNotFoundException, IOException}

import scala.io.Source
import scala.util.Try

case class FileLoaderException(filename: String, message: String) extends IOException

object ChecksumsFileLoader {

  private val expectedHeader = "file name,expected checksum"

  sealed trait Error

  final case class InvalidFile(fileName: String) extends Error {
    override def toString: String = s"Invalid File: $fileName"
  }

  final case class InvalidHeader(fileName: String, expected: String) extends Error {
    override def toString: String = s"Invalid Header: Expected '$expectedHeader'"
  }

  final case class FileNotFound(fileName: String) extends Error {
    override def toString: String = s"File Not Found: $fileName"
  }

  def load(fileName: String): Either[Error, List[ChecksumRequestValidator.Request]] = {
    val file = new File(fileName)
    val parentDirectory = file.getParent
    val source = Try(Source.fromFile(file))
    try {
      val lines = source.map(f => f.getLines.toList)
      val maybeRequests: Try[List[ChecksumRequestValidator.Request]] = lines
        .filter(ls => ls.head == expectedHeader)
        .map(ls => ls.tail
          .map(row => row.split(","))
          .map(data => ChecksumRequestValidator.Request(new File(parentDirectory + "/" + data(0)), data(1)))
        )

      maybeRequests.toEither.left.map {
        case _: FileNotFoundException => FileNotFound(fileName)
        case _: NoSuchElementException => InvalidHeader(fileName, expectedHeader)
        case _ => InvalidFile(fileName)
      }

    } finally {
      source.foreach(_.close);
    }
  }


}
