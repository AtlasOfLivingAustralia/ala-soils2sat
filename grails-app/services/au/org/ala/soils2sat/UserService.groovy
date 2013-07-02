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

class UserService {

    def deleteUser(User user) {
        // First delete user roles...
        def userRoles = UserRole.findAllByUser(user)
        userRoles.each {
            it.delete(flush: true)
        }
        // now delete the user itself
        user.delete(flush: true)
    }

    def isAdmin(User user) {
        UserRole.findByUserAndRole(user, Role.findByAuthority("ROLE_ADMIN")) ? true : false
    }

    def removeRoles(User user, List<String> authorities) {

        authorities.each { authority ->
            def role = Role.findByAuthority(authority)
            if (role) {
                def userRole = UserRole.findByUserAndRole(user, role)
                if (userRole) {
                    userRole.delete()
                }
            }
        }
    }

    def addRoles(User user, List<String> authorities) {

        authorities.each { authority ->
            def role= Role.findByAuthority(authority)
            def userRole = UserRole.findByUserAndRole(user, role)
            if (!userRole) {
                userRole = new UserRole(user:user, role: role)
                userRole.save(flush: true, failOnError: true)
            }
        }

    }
}
