package au.org.ala.soils2sat

import grails.converters.JSON
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

class SearchController {

    def springSecurityService
    def layerService
    def studyLocationService

    def index() {
        redirect(action: 'findStudyLocations')
    }

    def findStudyLocationFragment() {
        def userInstance = springSecurityService.currentUser as User
        [userInstance: userInstance, appState: userInstance?.applicationState]
    }

    def findStudyLocations() {
        def userInstance = springSecurityService.currentUser as User
        def userSearch = userInstance.applicationState.currentSearch
        if (!userSearch) {
            userSearch = new UserSearch(user: userInstance, name: 'default' )
            userSearch.save(failOnError: true)
            userInstance.applicationState.currentSearch = userSearch
        }

        [userInstance: userInstance, appState: userInstance?.applicationState, userSearch: userSearch]
    }

    def findStudyLocationResultsFragment() {

        def userInstance = springSecurityService.currentUser as User
        def userSearch = userInstance.applicationState.currentSearch

        if (!userSearch) {

        }

        def searchResults = null
        def searchPerformed = false
        userSearch.searchText = params.searchText

        userSearch.useBoundingBox = params.useBoundingBox == "on"
        if (userSearch.useBoundingBox) {
            userSearch.top = params.double("top")
            userSearch.left = params.double("left")
            userSearch.bottom = params.double("bottom")
            userSearch.right = params.double("right")
        }

        searchResults = studyLocationService.searchStudyLocations(userSearch)

        def appState = userInstance?.applicationState

        [results: searchResults, userInstance: userInstance, appState: appState, userSearch: userSearch]
    }

    def ajaxAddSearchCriteriaFragment() {
        def criteriaDefinitions = SearchCriteriaDefinition.list().sort { it.name }
        [criteriaDefinitions: criteriaDefinitions]
    }

    def ajaxSpatialFieldCriteriaFragment() {
        def criteriaDefinition = SearchCriteriaDefinition.get(params.int("searchCriteriaDefinitionId"))
        def criteria = SearchCriteria.get(params.int("criteriaId"))
        if (criteriaDefinition) {
        // get the list of allowed values
            def values = layerService.getValuesForField(criteriaDefinition.fieldName)
            return [criteriaDefinition: criteriaDefinition, allowedValues: values, criteria: criteria]
        }
    }

    def ajaxSpatialLayerCriteriaFragment() {
        def criteriaDefinition = SearchCriteriaDefinition.get(params.int("searchCriteriaDefinitionId"))
        def criteria = SearchCriteria.get(params.int("criteriaId"))
        def layerInfo = layerService.getLayerInfo(criteriaDefinition.fieldName)
        return [criteriaDefinition: criteriaDefinition, layerInfo: layerInfo, criteria: criteria]
    }

    def ajaxAusPlotsCriteriaFragment() {
        def criteriaDefinition = SearchCriteriaDefinition.get(params.int("searchCriteriaDefinitionId"))
        def criteria = SearchCriteria.get(params.int("criteriaId"))

        return [criteriaDefinition: criteriaDefinition, criteria: criteria]
    }

    def ajaxCriteriaDetailFragment() {
        try {

            def criteriaDefinition = SearchCriteriaDefinition.get(params.int("searchCriteriaDefinitionId"))
            if (criteriaDefinition) {
                switch (criteriaDefinition.type) {
                    case CriteriaType.SpatialPortalField:
                        redirect(action: 'ajaxSpatialFieldCriteriaFragment', params: params)
                        break
                    case CriteriaType.SpatialPortalLayer:
                        redirect(action: 'ajaxSpatialLayerCriteriaFragment', params: params)
                        break
                    case CriteriaType.StudyLocationVisit:
                        redirect(action: 'ajaxAusPlotsCriteriaFragment', params: params)
                        break
                    default:
                        throw new RuntimeException("Unhandled CriteriaType - " + criteriaDefinition.type)
                }
                return
            }
            throw new RuntimeException("No criteria specified!")
        } catch (Exception ex) {
            redirect(action:'ajaxErrorFragment', params: [title: 'Error getting criteria details', errorMessage: ex.message])
        }
    }

    def ajaxErrorFragment() {
        def title = params.title ?: "Error!"
        def errorMessage = params.errorMessage ?: "Unspecified Error!"
        [title: title, errorMessage: errorMessage]
    }

    private String joinMulti(Object val) {
        if (val instanceof String) {
            return val as String
        } else {
            return val.join("|")
        }
    }

    private Map extractFieldValue(SearchCriteriaDefinition criteriaDefinition, GrailsParameterMap params) {

        switch (criteriaDefinition.valueType) {
            case CriteriaValueType.StringMultiSelect:
                if (params.fieldValue) {
                    return [value: joinMulti(params.fieldValue)]
                } else {
                    return [errorMessage: "Please select at least one value for " + criteriaDefinition.name]
                }
                break;
            case CriteriaValueType.StringSingleSelect:
                if (params.fieldValue) {
                    return [value: joinMulti(params.fieldValue)]
                } else {
                    return [errorMessage: "Please select a value for " + criteriaDefinition.name]
                }
                break;
            case CriteriaValueType.StringDirectEntry:
                if (params.fieldValue) {
                    return [value: joinMulti(params.fieldValue)]
                } else {
                    return [errorMessage: "Please enter a value for " + criteriaDefinition.name]
                }
                break;
            case CriteriaValueType.NumberRangeDouble:
                if (params.operator && params.numberValue) {
                    try {
                        def number = Double.parseDouble(params.numberValue)
                        if (params.operator == 'bt') {
                            def number2 = Double.parseDouble(params.numberValue2)
                            return [value:"${params.operator} ${number}:${number2}"]
                        } else {
                            return [value:"${params.operator} ${number}"]
                        }
                    } catch (Exception ex) {
                        return [errorMessage: "Value is not a valid number!"]
                    }
                } else {
                    return [errorMessage: "Please enter a value for " + criteriaDefinition.name]
                }
                break;
            case CriteriaValueType.DateRange:
                if (params.operator && params.dateValue1) {
                    try {
                        def startDate = Date.parse("dd/MM/yyyy", params.dateValue1 as String)
                        if (params.operator == 'bt') {
                            def endDate = Date.parse("dd/MM/yyyy", params.dateValue2 as String)
                            return [value:"${params.operator} ${params.dateValue1}:${params.dateValue2}"]
                        } else {
                            return [value:"${params.operator} ${params.dateValue1}"]
                        }
                    } catch (Exception ex) {
                        return [errorMessage: "Value is not a valid date!"]
                    }
                } else {
                    return [errorMessage: "Please enter a value for " + criteriaDefinition.name]
                }

                break;
        }

        return [errorMessage: "Unhandled criteria type - ${criteriaDefinition.valueType}"]
    }

    def addSearchCriteriaAjax() {
        def criteriaDefinition = SearchCriteriaDefinition.get(params.int("searchCriteriaDefinitionId"))
        def criteria = SearchCriteria.get(params.criteriaId)
        def results = [status:'ok']
        if (!criteriaDefinition) {
            flash.message = "Error! A search criteria definition was not selected"
        } else {
            String value = null

            switch (criteriaDefinition.type) {
                case CriteriaType.SpatialPortalField:
                case CriteriaType.SpatialPortalLayer:
                case CriteriaType.StudyLocationVisit:

                    def extractResults = extractFieldValue(criteriaDefinition, params)

                    if (extractResults.errorMessage) {
                        results = [status: 'failed', errorMessage:extractResults.errorMessage]
                    } else if (extractResults.value) {
                        value = extractResults.value
                    } else {
                        // Should never happen?
                        results = [status: 'failed', errorMessage:"No value!"]
                    }
                    break;
                default:
                    throw new RuntimeException("Unhandled CriteriaType")
            }

            if (value) {
                def userInstance = springSecurityService.currentUser as User
                def appState = userInstance?.applicationState
                if (!appState.currentSearch) {
                    appState.currentSearch = new UserSearch(user: userInstance, name: 'default' )
                    appState.currentSearch.save(failOnError: true)
                }

                if (!criteria) {
                    criteria = new SearchCriteria(criteriaDefinition: criteriaDefinition, value: value, displayUnits: params.units)
                    appState.currentSearch.addToCriteria(criteria)
                    appState.currentSearch.save(flush: true, failOnError: true)
                } else {
                    criteria.value = value
                }
                criteria.save(flush: true, failOnError: true)
            }
        }

        render(results as JSON)
    }

    def ajaxCriteriaListFragment() {

        def userSearch = UserSearch.get(params.int("userSearchId"))
        if (!userSearch) {
            def userInstance = springSecurityService.currentUser as User
            def appState = userInstance?.applicationState
            userSearch = appState.currentSearch
        }

        [userSearch: userSearch]
    }

    def deleteSearchCriteria() {

        def userSearch = UserSearch.get(params.int("userSearchId"))
        def searchCriteria = SearchCriteria.get(params.int("searchCriteriaId"))

        if (userSearch && searchCriteria) {
            userSearch.removeFromCriteria(searchCriteria)
            searchCriteria.delete();
        }
        render([status: 'ok'] as JSON)
    }

    def ajaxDeleteAllSearchCriteria() {
        def userSearch = UserSearch.get(params.int("userSearchId"))
        if (userSearch) {
            def purgeList = userSearch.criteria.collect { it }
            userSearch.criteria.clear();
            purgeList.each {
                it.delete(flush: true);
            }
        }

        render([status:'ok'] as JSON)
    }

    def ajaxEditSearchCriteriaFragment() {
        def criteria = SearchCriteria.get(params.int("criteriaId"))
        [criteria: criteria]
    }

}
