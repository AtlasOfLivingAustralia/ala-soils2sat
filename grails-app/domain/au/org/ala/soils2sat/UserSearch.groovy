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

import javax.persistence.Transient

class UserSearch implements Serializable {

    User user
    String name
    String searchText
    Boolean useBoundingBox
    Double top
    Double left
    Double bottom
    Double right
    List<SearchCriteria> criteria

    static belongsTo = [user: User]
    static hasMany = [criteria: SearchCriteria]

    static mapping = {
        top column: "bbox_top"
        left column: "bbox_left"
        right column: "bbox_right"
        bottom column: "bbox_bottom"
    }

    static constraints = {
        user nullable: false
        name nullable:  false
        searchText nullable: true
        top nullable: true
        left nullable: true
        bottom nullable: true
        right nullable: true
        useBoundingBox nullable: true
    }

    @Transient
    public void clear() {
        searchText = ""
        useBoundingBox = false
        top = null
        left = null
        bottom = null
        right = null
        criteria?.clear()
    }

}
