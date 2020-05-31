package validator

import java.io.File

import org.scalatest._

class ChecksumsFileLoaderSpec extends FlatSpec with Matchers {

  "ChecksumFileLoader" should "load test file with 3 videos" in {
    val loadedData = ChecksumsFileLoader.load("src/test/resources/testChecksums.csv")

    val expected2 = ChecksumRequestValidator.Request(new File("src/test/resources/video5.mp4"), "d2aa249b4bb07e646cc9a90d29a98826")
    val expected1 = ChecksumRequestValidator.Request(new File("src/test/resources/video4.mp4"), "114b769462fa209f914dd6872883e170")
    val expected3 = ChecksumRequestValidator.Request(new File("src/test/resources/video6.mp4"), "1526614416ec96b8a8e9817c9c680490")
    loadedData shouldEqual Right(List(expected1, expected2, expected3))
  }

  "ChecksumFileLoader" should "load test file with 0 videos" in {
    val loadedData = ChecksumsFileLoader.load("src/test/resources/emptyChecksums.csv")
    loadedData shouldEqual Right(Nil)
  }

  "ChecksumFileLoader" should "return a FileNotFound error if file doesn't exist" in {
    val file = "blahBlah.csv"
    val loadedData = ChecksumsFileLoader.load(file)
    loadedData shouldEqual Left(ChecksumsFileLoader.FileNotFound(file))
  }

  "ChecksumFileLoader" should "return an InvalidFile error if file is invalid because checksum is missing" in {
    val file = "src/test/resources/invalidFile.csv"
    val loadedData = ChecksumsFileLoader.load(file)
    loadedData shouldEqual Left(ChecksumsFileLoader.InvalidFile(file))
  }

  "ChecksumFileLoader" should "return an InvalidHeader error if file is invalid because header is wrong" in {
    val file = "src/test/resources/invalidHeader.csv"
    val loadedData = ChecksumsFileLoader.load(file)
    loadedData shouldEqual Left(ChecksumsFileLoader.InvalidHeader(file, "file name,expected checksum"))
  }

  // can't commit this file without read perms because intellij has a meltdown when it can't read file in
  // test resources
  "ChecksumFileLoader" should "return a file not found error if file exists but has the wrong perms" in {
    // given
    val path = "src/test/resources/noReadPermissions.csv"

    import java.nio.file.Files
    import java.nio.file.attribute.PosixFilePermission
    val file = new File(path)
    file.createNewFile
    file.deleteOnExit()

    import scala.jdk.CollectionConverters._
    val perms = Set(PosixFilePermission.OWNER_WRITE, PosixFilePermission.OTHERS_WRITE).asJava

    Files.setPosixFilePermissions(file.toPath, perms)

    // when
    val loadedData = ChecksumsFileLoader.load(path)

    // then
    loadedData shouldEqual Left(ChecksumsFileLoader.FileNotFound(path))


  }
}
