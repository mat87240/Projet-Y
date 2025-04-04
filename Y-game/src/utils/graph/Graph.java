package utils.graph;

import java.util.ArrayList;
import java.util.Random;

public class Graph {
	private final int MIN_DISTANCE_NODE = 20;
	private final int MIN_DISTANCE_CONNEXION = 30;
	private final int MAX_DISTANCE_CONNEXION = 50;
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private ArrayList<Pair> connexions = new ArrayList<Pair>();

	public Graph() {
	}

	public void addNode(Node node) {
		nodes.add(node);
	}
	public void addConnexion(int node1, int node2) {
		this.connexions.add(new Pair(node1, node2));
	}

	public void generate(int num_node, long seed) {
		Random r = new Random(seed);
		boolean[] visited = new boolean[num_node];
		this.addNode(new Node(250, 250)); // Add central Node

		while (this.nodes.size() < num_node) {
			int current = r.nextInt(this.nodes.size());
			while (visited[current]) {
				current = r.nextInt(this.nodes.size());
			}
			visited[current] = true;
			int[] current_coord = this.nodes.get(current).getCoordinate();

			int max_neighbor = 0;
			int middle_distance = MIN_DISTANCE_CONNEXION + ((MAX_DISTANCE_CONNEXION - MIN_DISTANCE_CONNEXION) / 2);
			for (int i = 0; i < 4; i++) {
				int next_x = ((i & 1) == 0) ? 0 : middle_distance;
				int next_y = (((i >> 1) & 1) == 0) ? 0 : middle_distance;
				next_x = (((i >> 1) & 1) == 0) ? current_coord[0] + next_x : current_coord[0] - next_x;
				next_y = ((i & 1) == 0) ? current_coord[1] + next_y : current_coord[1] - next_y;
				boolean side_connected = false;
				for (int j = 0; j < this.nodes.size(); j++) {
					if (j == current) continue;
					if (this.nodes.get(j).isNearby(next_x, next_y, MIN_DISTANCE_NODE)) {
						if (this.connexions.contains(new Pair(j, current))) {
							side_connected = true;
							break;
						}
					}
				}
				if (!side_connected) {
					max_neighbor++;
				}
			}

			if (max_neighbor == 0) {
				continue;
			}
			int num_neighbor = r.nextInt(1, max_neighbor + 1);

			int added_neighbors = 0;
			while (added_neighbors < num_neighbor) {
				int deg = r.nextInt(4);
				int next_x = ((deg & 1) == 0) ? 0 : r.nextInt(MIN_DISTANCE_CONNEXION, MAX_DISTANCE_CONNEXION);
				int next_y = (((deg >> 1) & 1) == 0) ? 0 : r.nextInt(MIN_DISTANCE_CONNEXION, MAX_DISTANCE_CONNEXION);
				next_x = (((deg >> 1) & 1) == 0) ? current_coord[0] + next_x : current_coord[0] - next_x;
				next_y = ((deg & 1) == 0) ? current_coord[1] + next_y : current_coord[1] - next_y;

				boolean added = false;
				for (int j = 0; j < this.nodes.size(); j++) {
					if (j == current) {continue;}
					if (this.nodes.get(j).isNearby(next_x, next_y, MIN_DISTANCE_NODE)) {
						if (!this.connexions.contains(new Pair(j, current))) {
							this.connexions.add(new Pair(current, j));
							added = true;
							break;
						}
					}
				}
				if (!added) {
					Node next_node = new Node(next_x, next_y);
					this.addNode(next_node);
					this.addConnexion(current, this.nodes.size() - 1);
					added = true;
				}
				if (added) {
					added_neighbors++;
				}
			}
		}
	}

	public ArrayList<Node> getNodes() {return nodes;}
	public ArrayList<Pair> getPair() {return connexions;}


}