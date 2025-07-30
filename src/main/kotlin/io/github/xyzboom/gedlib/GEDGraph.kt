package io.github.xyzboom.gedlib

class GEDGraph internal constructor(
    @Suppress("unused")
    private val nativePointer: Long,
    private val env: GEDEnv
) {
    private external fun addNodeNative(env: GEDEnv, nodeId: String, properties: Map<String, String>)
    private external fun addEdgeNative(
        env: GEDEnv, fromNode: String, toNode: String,
        properties: Map<String, String>, ignoreDuplicates: Boolean
    )

    fun addNode(node: GEDNode, properties: Map<String, String> = emptyMap()): GEDNode {
        addNodeNative(env, node.id, properties)
        return node
    }

    fun addEdge(
        from: GEDNode, to: GEDNode,
        properties: Map<String, String> = emptyMap(),
        ignoreDuplicates: Boolean = true
    ) {
        addEdgeNative(env, from.id, to.id, properties, ignoreDuplicates)
    }
}