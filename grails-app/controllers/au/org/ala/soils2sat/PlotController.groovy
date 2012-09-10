package au.org.ala.soils2sat

class PlotController {

    def springSecurityService

    def detailsFragment() {

        def plotName = params.plotName;
        def userInstance = springSecurityService.currentUser as User

        [plotName:plotName, userInstance: userInstance]
    }

    def findPlotFragment() {
        [userInstance:  springSecurityService.currentUser]
    }
}
