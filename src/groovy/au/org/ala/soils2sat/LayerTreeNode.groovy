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

class LayerTreeNode {
    String label
    List<LayerTreeNode> childFolders = new ArrayList<LayerTreeNode>()
    List<LayerDefinition> layers = new ArrayList<LayerDefinition>()

    def getOrAddFolder(String name) {
        def folder = childFolders.find {
            it.label == name
        }
        if (!folder) {
            folder = new LayerTreeNode(label: name)
            childFolders.add(folder)
        }
        return folder
    }

    def addLayer(LayerDefinition layer) {
        layers.add(layer)
    }

    def dump(int indent = 0) {
        def s = " " * (indent * 2)
        println s + label
        layers.each {
            println s + "  " + it.name + " (layer)"
        }
        childFolders.each {
            it.dump(indent + 1)
        }
    }

}