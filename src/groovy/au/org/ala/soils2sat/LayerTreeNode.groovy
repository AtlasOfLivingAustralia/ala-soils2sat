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