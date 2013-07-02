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

import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUserDetailsService

class S2SUserDetailsService implements GrailsUserDetailsService {

    /**
     * Some Spring Security classes (e.g. RoleHierarchyVoter) expect at least
     * one role, so we give a user with no granted roles this one which gets
     * past that restriction but doesn't grant anything.
     */
    static final List NO_ROLES = [new GrantedAuthorityImpl(SpringSecurityUtils.NO_ROLE)]

    UserDetails loadUserByUsername(String username, boolean loadRoles) throws UsernameNotFoundException {
        return loadUserByUsername(username)
    }

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User.withTransaction { status ->

            User user = User.findByUsernameIlike(username)
            if (!user) {
                throw new UsernameNotFoundException('User not found', username)
            }

            def authorities = user.authorities.collect {
                new GrantedAuthorityImpl(it.authority)
            }

            if (!user.applicationState) {
                def applicationState = new UserApplicationState(user: user)
                applicationState.save(flush: true, failOnError: true)
            }

            // clear any existing UserSearch...
            def userSearch = user.applicationState.currentSearch
            if (userSearch) {
                userSearch.clear()
            }

            return new GrailsUser(user.username, user.password, user.enabled, !user.accountExpired, !user.passwordExpired, !user.accountLocked, authorities ?: NO_ROLES, user.id)
        }
    }
}

