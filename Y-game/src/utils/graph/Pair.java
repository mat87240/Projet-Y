package utils.graph;

public class Pair {

	private int index_n1, index_n2;

	public Pair(int node1, int node2) {
		if (node1==node2) {
			throw new Error("Invalid pair, same node");
		}
		if (node1<0 || node2<0) {
			throw new Error("Invalid pair, node index negative");
		}
		this.index_n1 = node1;
		this.index_n2 = node2;
	}

	public int[] getPair() {
		int[] pair = new int[2];
		pair[0] = this.index_n1;
		pair[1] = this.index_n2;
		return pair;
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Pair pair = (Pair) obj;
		return index_n1 == pair.index_n1 && index_n2 == pair.index_n2;
	}

}