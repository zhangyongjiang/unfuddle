package com.leetcode;

public class PostOrderTree {
	private static class Node {
		private Node left;
		private Node right;
		private Object value;
	}
	
	public void traversal(Node node) {
		if(node == null)
			return;
		if(node.left != null)
			traversal(node.left);
		if(node.right != null)
			traversal(node.right);
		System.out.println(node.value);
	}
}
