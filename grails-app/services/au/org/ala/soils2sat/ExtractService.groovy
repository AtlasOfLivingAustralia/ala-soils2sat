package au.org.ala.soils2sat

import grails.converters.JSON
import org.apache.commons.io.FileSystemUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import org.apache.tomcat.jni.FileInfo
import org.grails.plugins.csv.CSVWriter
import org.h2.store.fs.FileUtils

import java.text.SimpleDateFormat
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ExtractService {

    def studyLocationService
    def layerService
    def logService
    def grailsApplication
    def grailsLinkGenerator
    def DOIService

    private String generateVisitPackageName() {
        // Create a unique package name based on date and number of packages already created.
        boolean fileExists = true // Assume the package already exists until it is actually tested
        def packageName = ""
        def sdf = new SimpleDateFormat("yyyyMMdd")
        while (fileExists) {
            def lastDailyId = LastDailyId.nextNumber
            packageName = String.format("SLV-%s-%04d",sdf.format(lastDailyId.date), lastDailyId.lastNumber)
            fileExists = new File(constructLocalFileArchivePath(packageName)).exists()
        }
        return packageName
    }

    private String constructLocalFileArchivePath(String packageName) {
        def localPath = FilenameUtils.concat((String) grailsApplication.config.extractRepositoryRoot, packageName + ".zip")
        return localPath
    }

    private String createArchiveFile(String packageName, User user, List<String> visitIds, List<String> samplingUnits) {

        def localPath = constructLocalFileArchivePath(packageName)

        try {
            def fileStream = new FileOutputStream(new File(localPath))
            def zipStream = new ZipOutputStream(fileStream)
            OutputStream bos = new BufferedOutputStream(zipStream)
            OutputStreamWriter writer = new OutputStreamWriter(bos)

            def manifestEntries = []

            manifestEntries << writeZipEntry(user, zipStream, writer, "environmentalLayers.txt", writeLayerData(visitIds))
            manifestEntries << writeZipEntry(user, zipStream, writer, "studyLocationDetails.txt", writeStudyLocations(visitIds))
            manifestEntries << writeZipEntry(user, zipStream, writer, "studyLocationVisitDetails.txt", writeStudyLocationVisits(visitIds))

            writeZipEntry(user, zipStream, writer, "manifest.text", writeManifestFactory(manifestEntries))

            zipStream.flush()
            zipStream.close()

        } catch (Exception ex) {
            ex.printStackTrace()
            // clean up...
            if (FileUtils.exists(localPath)) {
                FileUtils.delete(localPath)
            }
            throw ex
        }

        return localPath
    }


    def extractAndPackageData(User user, List<String> visitIds, List<String> samplingUnits) {

        try {
            logService.log("Extracting and Packaging visit data for ${user.username} Visits: ${visitIds} SamplingUnits: ${samplingUnits}")

            def packageName = generateVisitPackageName()
            logService.log("Package name is '${packageName}'")

            def localPath = createArchiveFile(packageName, user, visitIds, samplingUnits)
            logService.log("Download local filepath is '$localPath'")

            def downloadUrl = grailsLinkGenerator.link(controller: 'extract', action:'downloadPackage', params:[packageName: packageName], absolute: true)
            logService.log("Download URL is '$downloadUrl'")

            // Create a record for this extract
            def dataExtraction = new DataExtraction(packageName: packageName, username: user.username, date: new Date(), localFile: localPath)
            dataExtraction.save(failOnError: true)

            // Mint a DOI for this extract
            String doi = ""
            try {
                doi = this.DOIService.mintDOI(dataExtraction, user)
            } catch (DOIMintingFailedException doimex) {
                doi = "DOI Minting Failed: " + doimex.message
            }

            return [success: true, packageUrl: downloadUrl, DOI: doi, message:'']
        } catch (Exception ex) {
            return [success: false, packageUrl: '', DOI: '', message: ex.message]
        }
    }

    private ManifestEntry writeZipEntry(User user, ZipOutputStream zipStream, Writer writer, String filename, Closure closure) {
        def manifestEntry = new ManifestEntry(filename: filename)
        zipStream.putNextEntry(new ZipEntry(filename))
        if (closure) {
            closure(user, writer, manifestEntry)
        }
        writer.flush()
        zipStream.closeEntry()
        return manifestEntry
    }

    def writeManifestFactory(List<ManifestEntry> entries) {

        return { User user, Writer writer, manifestEntry ->
            entries.each { entry ->
                writer << entry.filename << "\t" << entry.comment << "\n"
            }
        }
    }

    def writeStudyLocationVisits(List<String> visitIds) {

        return { User user, Writer writer, manifestEntry ->

            def csvWriter = new CSVWriter(writer, {
                id { it.id }
                siteLocationId { it.siteLocationId }
                visitStartDate { it.visitStartDate }
                photopointsExistq { it.photopointsExistq }
                photopointLat1 { it.photopointLat1 }
                photopointLong1 { it.photopointLong1 }
                leafAreaIndexExistsq { it.leafAreaIndexExistsq }
                visitNotes { it.visitNotes }
                state { it.state }
                agency { it.agency }
                describedBy { it.describedBy }
                locationDescription { it.locationDescription }
                swMarkerEasting { it.swMarkerEasting }
                swMarkerNorthing { it.swMarkerNorthing }
                swMarkerMgaZones { it.swMarkerMgaZones }
                swMarkerDatum { it.swMarkerDatum }
                method { it.method }
                erosion { it.erosion }
                abundance { it.abundance }
                microreliefType { it.microreliefType }
                drainage { it.drainage }
                disturbance { it.disturbance }
                surfaceCoarseFragsAbundance { it.surfaceCoarseFragsAbundance }
                surfaceSoilCondition { it.surfaceSoilCondition }
                climaticCondition { it.climaticCondition }
                vegetationCondition { it.vegetationCondition }
                asc { it.asc }
                observer1 { it.observer1 }
                observer2 { it.observer2 }
                okToPublish { it.okToPublish }
            })

            visitIds.each { visitId ->
                def visitDetails = studyLocationService.getVisitDetails(visitId)
                csvWriter.write(visitDetails)

            }

            manifestEntry.comment = "Selected Study Location Visit details"
        }
    }

    def writeStudyLocations(List<String> visitIds) {
        def studyLocationNames = getStudyLocationNames(visitIds)
        return { User user, Writer writer, manifestEntry ->

            def csvWriter = new CSVWriter(writer, {
                studyLocationName { it.siteLocationName }
                establishedDate { it.establishedDate }
                description { it.description }
                bioregionName { it.bioregionName }
                property { it.property }
                zone { it.zone }
                easting { it.easting }
                northing { it.northing }
                method { it.method }
                datum { it.datum }
                plotPermanentlyMarkedq { it.plotPermanentlyMarkedq }
                plotAlignedToGridq { it.plotAlignedToGridq }
                landformPattern { it.landformPattern }
                landformElement { it.landformElement }
                siteSlope { it.siteSlope }
                siteAspect { it.siteAspect }
                surfaceStrewSize { it.surfaceStrewSize }
                plot100mBy100m { it.plot100mBy100m }
                comments { it.comments }
            })

            studyLocationNames.each { studyLocationName ->
                def details= studyLocationService.getStudyLocationDetails(studyLocationName)
                csvWriter.write(details)
            }

            manifestEntry.comment = "Study Location details for the selected visits"
        }
    }

    private List<String> getStudyLocationNames(List<String> visitIds) {
        def results = []
        if (visitIds) {
            visitIds.each { visitId ->
                def name = studyLocationService.getStudyLocatioNameForVisitId(visitId)
                if (name && !results.contains(name)) {
                    results << name
                }
            }
        }

        return results
    }

    def writeLayerData(List<String> visitIds) {

        return { User user, Writer writer, manifestEntry ->
            final UserApplicationState appState = user.applicationState
            def columnHeaders = ["field"]
            appState.selectedPlotNames?.each {
                columnHeaders << it
            }

            def results = getLayerDataForLocations(appState.selectedPlotNames, appState.layers)

            def csvWriter = new au.com.bytecode.opencsv.CSVWriter(writer)
            csvWriter.writeNext(columnHeaders as String[])

            results.fieldNames.each { fieldName ->
                def lineItems = [fieldName]
                appState.selectedPlotNames?.each { studyLocation ->
                    def value = results.data[studyLocation][fieldName]
                    lineItems << value ?: ''
                }
                csvWriter.writeNext(lineItems as String[])
            }

            manifestEntry.comment = "Values of selected environment layers at selected study locations"
        }
    }

    public Map getLayerDataForLocations(List<String> locationNames, List<EnvironmentalLayer> layers) {

        def data =[:]
        def fieldNames = ['latitude', 'longitude']
        def fieldUnits = [:]
        if (locationNames && layers) {
            def layerNames = layers.collect({ it.name }).join(",")
            for (String studyLocationName : locationNames) {
                def studyLocationSummary = studyLocationService.getStudyLocationSummary(studyLocationName)
                def url = new URL("${grailsApplication.config.spatialPortalRoot}/ws/intersect/${layerNames}/${studyLocationSummary.latitude}/${studyLocationSummary.longitude}")
                def studyLocationResults = JSON.parse(url.text)
                def temp = [:]
                temp.latitude = studyLocationSummary.latitude
                temp.longitude = studyLocationSummary.longitude

                studyLocationResults.each {
                    def fieldName = it.layername
                    if (!fieldNames.contains(fieldName)) {
                        fieldNames << fieldName
                    }
                    temp[fieldName] = it.value
                    fieldUnits[fieldName] = it.units
                }
                data[studyLocationName] = temp
            }
        }

        return [data: data, fieldNames: fieldNames, fieldUnits: fieldUnits]
    }

}

class ManifestEntry {
    String filename
    String comment
}
