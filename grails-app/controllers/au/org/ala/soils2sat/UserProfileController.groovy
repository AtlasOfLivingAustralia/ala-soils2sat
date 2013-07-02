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

class UserProfileController {

    def springSecurityService

    def index() {
        redirect(action:"edit")
    }

    def edit() {
        def user = springSecurityService.currentUser as User
        def userProfile = user.userProfile
        if (!userProfile) {
            userProfile = new UserProfile(user: user)
            userProfile.save()
        }
        [user: user, userProfile: userProfile]
    }

    def update() {
        def user = springSecurityService.currentUser as User
        def userProfile = user.userProfile
        if (!userProfile) {
            userProfile = new UserProfile(user: user)
        }

        userProfile.setProperties(params)
        flash.message = "User profile updated"
        redirect(action: "edit")
    }

    def extractions() {
        def user = springSecurityService.currentUser as User

        params.q = user.username
        params.max = params.max ?: 10
        params.sort= params.sort ?: "date"
        params.order= params.order ?: "desc"

        def c = DataExtraction.createCriteria()

        def extractions = c.list(params) {
            eq("username", user.username)
        }

        // def extractions = DataExtraction.list(params)

//        def extractions = DataExtraction.findAllByUsername(user?.username, params)
        [userInstance: user, extractions: extractions]
    }

}
