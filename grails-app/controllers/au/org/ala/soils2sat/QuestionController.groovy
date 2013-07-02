/*
 * ﻿Copyright (C) 2013 Atlas of Living Australia
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 */

package au.org.ala.soils2sat

class QuestionController {

    def springSecurityService

    def index() {
        def questions = Question.list()
        def userInstance = springSecurityService.currentUser as User

        [questions: questions, userInstance: userInstance]
    }

    def ecologicalContexts() {
        def question = Question.get(params.int("questionId"))
        def userInstance = springSecurityService.currentUser as User
        if (question && userInstance) {
            def values = MatrixValue.findAllByQuestionAndRequired(question, true)
            def contexts = values.collect { it.ecologicalContext }
            [question:question, contexts: contexts, userInstance: userInstance]
        }
    }

    def showSamplingUnits() {
        def userInstance = springSecurityService.currentUser as User
        def question = Question.get(params.int("questionId"))
        if (question) {
            def values = MatrixValue.findAllByQuestionAndRequired(question, true)
            def contexts = values.collect { it.ecologicalContext }
            List<EcologicalContext> selectedContexts = []
            contexts.each {
                def key = "context_${it.id}"
                if (params[key] == 'on') {
                    selectedContexts << it
                }
            }

            List<SamplingUnit> units = []
            selectedContexts.each { context ->
                context.samplingUnits.each { unit ->
                    if (!units.contains(unit)) {
                        units << unit
                    }
                }
            }

            [selectedContexts: selectedContexts, question:  question, userInstance: userInstance, samplingUnits: units]
        }

    }
}
