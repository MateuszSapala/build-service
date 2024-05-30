package sapala.s2sauthservice.buildservice.build

import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class ZipUtil {
    companion object {
        fun extractZip(zip: ZipInputStream, extractFolder: File) {
            var zipEntry: ZipEntry?
            while ((zip.nextEntry.also { zipEntry = it }) != null) {
                val entry = zipEntry!!
                if (entry.isDirectory) {
                    zip.closeEntry()
                    continue
                }

                val targetFile = extractFolder.resolve(entry.name)
                targetFile.getParentFile().mkdirs()
                FileOutputStream(targetFile).use { fileOutputStream ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while ((zip.read(buffer).also { length = it }) > 0) {
                        fileOutputStream.write(buffer, 0, length)
                    }
                }
                zip.closeEntry()
            }
        }
    }
}
