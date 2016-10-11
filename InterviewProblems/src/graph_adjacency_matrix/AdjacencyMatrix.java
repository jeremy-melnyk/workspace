package graph_adjacency_matrix;

public class AdjacencyMatrix
{

}

// Adjacency Matrix
// Pros:
// Better for dense graphs
// Faster lookup for presence of an edge [firstNodeIndex][secondNodeIndex]
//
// Cons:
// Uses n^2 memory
// Slow to iterate over edges (n)

// Adjacency List
// Pros:
// Better for sparse graphs
// O(numberOfEdges) space
// Slower lookups for presence of an edge (n)
// Fast to iterate over edges (n - nonEdgeNodes)

