package au.org.ala.soils2sat

class DataExtraction implements Serializable {

    String packageName
    Date date
    String username
    String localFile
    Integer downloadCount = 0
    String doi
    List studyLocationVisits
    Date firstVisitDate
    Date lastVisitDate
    String appVersion

    static hasMany = [studyLocationVisits: String]

    static constraints = {
        downloadCount nullable: true
        doi nullable: true
        firstVisitDate nullable: true
        lastVisitDate nullable: true
        appVersion nullable: true
    }

    def afterDelete() {
        // Clean up the local file.
        try {
            def f = new File(localFile)
            if (f.exists()) {
                f.delete()
            }
        } catch (Exception ex) {
            println "Error occurred during 'afterDelete' event on DataExtraction (PackageName $packageName)"
            ex.printStackTrace()
        }
    }
}
