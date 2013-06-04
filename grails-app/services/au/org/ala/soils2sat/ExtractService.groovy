package au.org.ala.soils2sat

import grails.converters.JSON
import net.sf.json.JSONNull
import org.apache.commons.io.FilenameUtils
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

            visitIds.each { visitId ->
                def visitDetails = studyLocationService.getVisitDetails(visitId)
                if (visitDetails) {
                    visitDetails.samplingUnits.each { samplingUnit ->
                        String samplingUnitId = samplingUnit.id?.toString()
                        if (!samplingUnits || samplingUnits.contains(samplingUnitId)) {
                            manifestEntries << writeZipEntry(user, zipStream, writer, "Visit_${visitId}_${samplingUnit.description}.txt", writeSamplingUnit(visitId, samplingUnitId))
                        }
                    }
                }
            }

            writeZipEntry(user, zipStream, writer, "manifest.txt", writeManifestFactory(manifestEntries))

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

    def writeSamplingUnit(String visitId, String samplingUnitTypeId) {

        def samplingUnit = studyLocationService.getSamplingUnitDetails(visitId, samplingUnitTypeId)
        def data = samplingUnit?.samplingUnitData
        if (!samplingUnit || !data) {
            return { User user, Writer writer, manifestEntry ->
                writer.write("Sampling unit data could not be retrieved: SiteVisitId ${visitId}, samplingUnitTypeId ${samplingUnitTypeId}".toString())
                manifestEntry.comment = "Data for visit ${visitId} and sampling unit type ${samplingUnitTypeId}: Data could not be retrieved"
            }
        }

        return { User user, Writer writer, manifestEntry ->
            Map first = data.first()

            def sb = new StringBuilder()
            def keySet = first.keySet().sort { it } // always get them entries in the same order
            // print header row first...
            keySet.each {
                sb.append('"').append(it).append('",')
            }
            // trim off last comma
            sb.deleteCharAt(sb.length() - 1);
            writer << sb << "\n"
            // now dump all the data...
            data.each { row ->
                sb = new StringBuilder()
                keySet.each { String fieldName ->
                    def element = row[fieldName]
                    if (element == null || element == JSONNull) {
                        element = ""
                    }
                    sb.append('"').append(element).append('",')
                }
                // trim off last comma
                sb.deleteCharAt(sb.length() - 1);
                writer << sb << "\n"
            }

            manifestEntry.comment = "${samplingUnit.samplingUnit?.description} data for visit ${visitId}"
        }

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
                dataExtraction.doi = doi
            } catch (DOIMintingFailedException doimex) {
                doi = "DOI Minting Failed: " + doimex.message
            }

            return [success: true, packageUrl: downloadUrl, DOI: doi, message:'']
        } catch (Exception ex) {
            throw ex
            // return [success: false, packageUrl: '', DOI: '', message: ex.message]
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
                id { StudyLocationVisitDetailsTO visit -> visit.studyLocationVisitId }
                studyLocationId { StudyLocationVisitDetailsTO visit -> visit.studyLocationId }
                visitStartDate { StudyLocationVisitDetailsTO visit -> visit.visitStartDate }
                visitNotes { StudyLocationVisitDetailsTO visit -> visit.visitNotes }
                locationDescription { StudyLocationVisitDetailsTO visit -> visit.locationDescription }
                pitMarkerEasting { StudyLocationVisitDetailsTO visit -> visit.pitMarkerEasting }
                pitMarkerNorthing { StudyLocationVisitDetailsTO visit -> visit.pitMarkerNorthing }
                pitMarkerMgaZones { StudyLocationVisitDetailsTO visit -> visit.pitMarkerMgaZones }
                pitMarkerDatum { StudyLocationVisitDetailsTO visit -> visit.pitMarkerDatum }
                erosionType { StudyLocationVisitDetailsTO visit -> visit.erosionType }
                erosionAbundance { StudyLocationVisitDetailsTO visit -> visit.erosionAbundance }
                microrelief { StudyLocationVisitDetailsTO visit -> visit.microrelief }
                drainageType { StudyLocationVisitDetailsTO visit -> visit.drainageType }
                disturbance { StudyLocationVisitDetailsTO visit -> visit.disturbance }
                surfaceCoarseFragsAbundance { StudyLocationVisitDetailsTO visit -> visit.surfaceCoarseFragsAbundance }
                climaticCondition { StudyLocationVisitDetailsTO visit -> visit.climaticCondition }
                vegetationCondition { StudyLocationVisitDetailsTO visit -> visit.vegetationCondition }
                observer1 { StudyLocationVisitDetailsTO visit -> visit.observers?.size() > 0 ? visit.observers?.getAt(0) : '' }
                observer2 { StudyLocationVisitDetailsTO visit -> visit.observers?.size() > 1 ? visit.observers?.getAt(0) : '' }
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
                studyLocationName { it.studyLocationName }
                firstVisitDate { it.firstVisitDate }
                lastVisitDate { it.lastVisitDate }
                bioregionName { it.bioregionName }
                mgaZone { it.mgaZone }
                easting { it.easting }
                northing { it.northing }
                landformPattern { it.landformPattern }
                landformElement { it.landformElement }
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
                def name = studyLocationService.getStudyLocationNameForVisitId(visitId)
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
                def studyLocationSummary = studyLocationService.getStudyLocationDetails(studyLocationName)
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
