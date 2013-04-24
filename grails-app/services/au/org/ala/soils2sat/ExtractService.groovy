package au.org.ala.soils2sat

import au.com.bytecode.opencsv.CSVWriter
import org.apache.commons.io.FilenameUtils
import org.h2.store.fs.FileUtils

import java.text.SimpleDateFormat
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class ExtractService {

    def studyLocationService
    def layerService
    def logService
    def grailsApplication
    def grailsLinkGenerator

    def extractAndPackageData(User user, List<String> visitIds, List<String> samplingUnits) {

        logService.log("Extracting and Packaging visit data for ${user.username} Visits: ${visitIds} SamplingUnits: ${samplingUnits}")

        // Create a unique package name based on date and number of packages already created.

        def lastDailyId = LastDailyId.nextNumber

        def sdf = new SimpleDateFormat("yyyyMMdd")

        def packageName = String.format("SLV-%s-%04d",sdf.format(lastDailyId.date), lastDailyId.lastNumber)

        logService.log("Package name is '${packageName}'")
        def localPath = FilenameUtils.concat((String) grailsApplication.config.extractRepositoryRoot, packageName + ".zip")
        logService.log("Download local filepath is '$localPath'")

        def downloadUrl = grailsLinkGenerator.link(controller: 'extract', action:'downloadPackage', params:[packageName: packageName], absolute: true)

        try {

            def fileStream = new FileOutputStream(new File(localPath))
            def zipStream = new ZipOutputStream(fileStream)
            OutputStream bos = new BufferedOutputStream(zipStream)
            OutputStreamWriter writer = new OutputStreamWriter(bos)

            writeZipEntry(zipStream, writer, "manifest.text", writeManifest)


            zipStream.flush()
            zipStream.close()

        } catch (Exception ex) {

            ex.printStackTrace()
            // clean up...
            if (FileUtils.exists(localPath)) {
                FileUtils.delete(localPath)
            }
            return [success: false, packageUrl: '', DOI: '', message:ex.message]
        }

        // Create a record for this extract
        def dataExtraction = new DataExtraction(packageName: packageName, username: user.username, date: new Date(), localFile: localPath)
        dataExtraction.save()

        return [success: true, packageUrl: downloadUrl, DOI: '', message:'']
    }

    private void writeZipEntry(ZipOutputStream zipStream, Writer writer, String filename, Closure closure) {
        zipStream.putNextEntry(new ZipEntry(filename))
        if (closure) {
            closure(writer)
        }
        writer.flush()
        zipStream.closeEntry()
    }


    def writeManifest = { Writer writer ->
        writer << "This is the manifest File!"
    }

}
