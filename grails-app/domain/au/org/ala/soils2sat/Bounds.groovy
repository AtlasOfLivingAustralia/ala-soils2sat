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

class Bounds {

    double left = 0
    double top = 0
    double right = 0
    double bottom = 0


    static constraints = {
        left nullable: false
        top nullable: false
        right nullable: false
        bottom nullable: false
    }

    static mapping = {
   		left column: '`left`'
        right column: '`right`'
        top column:  '`top`'
        bottom column: '`bottom`'
   	}

}
