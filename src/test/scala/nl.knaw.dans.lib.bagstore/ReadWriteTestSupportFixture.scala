package nl.knaw.dans.lib.bagstore

import java.nio.file.Paths

import better.files.File

trait ReadWriteTestSupportFixture extends ReadOnlyTestSupportFixture {
  protected val testDir: File = Paths.get(s"target/test/${getClass.getSimpleName}")
  if (testDir.exists) testDir.delete()
  testDir.createDirectories()
}
