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

enum EcologicalContextType1 {
    FAUNA("Fauna"),
    MICROORGANISM("Micro-organism"),
    SOIL("Soil"),
    VEGETATION("Vegetation/Flora")

    public EcologicalContextType1(String description) {
        _description = description
    }

    String getDescription() {
        return _description;
    }

    private String _description
}

enum EcologicalContextType2 {

    STRUCTURE("Structure"),
    COMPOSITION("Composition"),
    FUNCTION("Function")

    public EcologicalContextType2(String description) {
        _description = description
    }

    String getDescription() {
        return _description;
    }

    private String _description

}

enum EcologicalContextType3 {

    ECOSYSTEM("Ecosystem"),
    GENE("Gene"),
    INDIVIDUAL("Individual"),
    POPULATION("Population"),
    BIOME("Biome"),
    COMMUNITY("Community")

    public EcologicalContextType3(String description) {
        _description = description
    }

    String getDescription() {
        return _description;
    }

    private String _description

}


