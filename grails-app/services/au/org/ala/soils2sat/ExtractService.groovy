package au.org.ala.soils2sat

import au.com.bytecode.opencsv.CSVWriter

import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class ExtractService {

    def studyLocationService
    def layerService
    def logService

    def extractAndPackageData(User user, List<String> visitIds, List<String> samplingUnits) {

        logService.log("Extracting and Packaging visit data for ${user.username} Visits: ${visitIds} SamplingUnits: ${samplingUnits}")

//        def zipStream = new ZipOutputStream(stream)
//
//        OutputStream bos = new BufferedOutputStream(zipStream)
//        OutputStreamWriter writer = new OutputStreamWriter(bos)
//        zipStream.putNextEntry(new ZipEntry("manifest.txt"))
//        writer << "This is the manifest File!"
//        writer.flush()
//        zipStream.closeEntry()
//        zipStream.flush()
//        zipStream.close()

        return [success: true, packageUrl: '', DOI: '', message:'']
    }

}
