package au.org.ala.soils2sat

class LogService {

    def log(String message) {
        String fullMsg = "[${new Date().format("yyyy-MM-dd HH:mm:ss")}] ${message}"
        println fullMsg
    }
}
