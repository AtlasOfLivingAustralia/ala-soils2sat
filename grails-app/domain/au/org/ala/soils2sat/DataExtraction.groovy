package au.org.ala.soils2sat

class DataExtraction implements Serializable {

    String packageName
    Date date
    String username
    String localFile
    Integer downloadCount = 0

    static constraints = {
        downloadCount nullable: true
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
