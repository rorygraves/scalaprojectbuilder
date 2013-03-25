package code.builder

import java.util.zip.ZipOutputStream
import java.util.zip.ZipEntry
import java.io.ByteArrayOutputStream

import scala.collection.mutable


class ZipBuilder {

  private val baos = new ByteArrayOutputStream()
  private val zos = new ZipOutputStream(baos)
  private var closed = false

  def addDir(name : String) = {
    if(closed) 
      throw new IllegalStateException("ZipBuilder is closed, can not add content")

    zos.putNextEntry(new ZipEntry(name))
    zos.closeEntry();
  }

  def addTextFile(name : String,content : String) {
    if(closed) 
      throw new IllegalStateException("ZipBuilder is closed, can not add content")

    zos.putNextEntry(new ZipEntry(name))
    val data = content.getBytes()
    zos.write(data, 0, data.length)
    zos.closeEntry()
  }

  
  def build(): Array[Byte] = {
		  
    if(!closed) {
      zos.close();
      closed = true;
    }
    
    baos.toByteArray()
  }
}