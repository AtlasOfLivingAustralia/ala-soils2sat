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

import au.org.ala.soils2sat.Role
import au.org.ala.soils2sat.User
import au.org.ala.soils2sat.UserRole

class BootStrap {

    def grailsApplication

    def init = { servletContext ->

        def roles = ['ROLE_USER','ROLE_ADMIN']

        roles.each { role ->
            Role.findByAuthority(role) ?: new Role(authority: role).save(flush:  true, failOnError: true)
        }

        def admin = User.findByUsername('admin')
        if (!admin) {
            admin = new User(username: 'admin', password:'P@ssw0rd', enabled: true, accountExpired: false, accountLocked: false)
            admin.save()
        }

        ['ROLE_USER','ROLE_ADMIN'].each {
            def role= Role.findByAuthority(it)
            def userRole = UserRole.findByUserAndRole(admin, role)
            if (!userRole) {
                userRole = new UserRole(user:admin, role: role)
                userRole.save(flush: true, failOnError: true)
            }
        }

        def extractDirectory = new File(grailsApplication.config.extractRepositoryRoot)
        if (!extractDirectory.exists()) {
            extractDirectory.mkdirs()
        }

    }

    def destroy = {
    }

}
