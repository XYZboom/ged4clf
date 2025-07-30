package io.github.xyzboom.gedlib

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GEDEnvTest {
    @Test
    fun getEditTimesTest() {
        val env = GEDEnv()
        val graph1 = env.addGraph()
        val graph2 = env.addGraph()
        graph1.apply {
            val node1 = addNode(GEDNode("1"))
            val node2 = addNode(GEDNode("2"))
            val node3 = addNode(GEDNode("3"))
            addEdge(node1, node2)
            addEdge(node2, node3)
            addEdge(node1, node3)
        }
        graph2.apply {
            val node1 = addNode(GEDNode("1"))
            val node2 = addNode(GEDNode("2"))
            val node3 = addNode(GEDNode("3"))
            addEdge(node1, node2)
            addEdge(node2, node3)
        }
        val lower = env.getLowerBound(graph1, graph2)
        val upper = env.getUpperBound(graph1, graph2)
        assertEquals(1.0, lower)
        assertEquals(1.0, upper)
    }

}