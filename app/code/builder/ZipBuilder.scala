package code.builder

import org.apache.commons.io.output.ByteArrayOutputStream
import java.util.zip.ZipOutputStream
import java.util.zip.ZipEntry

object ZipBuilder {

  def build() : Array[Byte] = {
    val baos = new ByteArrayOutputStream();
    val zos = new ZipOutputStream(baos);
    
    zos.putNextEntry(new ZipEntry("README.txt"))
      val data = "TEST1234".getBytes();
      zos.write(data, 0, data.length);
    zos.closeEntry();
    
    
    return baos.toByteArray();
  } 
}