package com.leetcode;

import java.util.Stack;

public class ReversePolishNotation {
	public int evalRPN(String[] tokens) throws Exception {
		Stack<Integer> stack = new Stack<Integer>();
		for(int i=0; i<tokens.length; i++) {
			evalRPN(stack, i, tokens);
		}
		if(stack.size()>1)
			throw new Exception("invalid expression");
		return stack.pop();
	}
	
	private void evalRPN(Stack<Integer> stack, int next, String[] tokens) {
		String token = tokens[next];
		if(token.equals("+")) {
			int n1 = stack.pop();
			int n2 = stack.pop();
			int result = n2 + n1;
			stack.push(result);
		}
		else if(token.equals("-")) {
			int n1 = stack.pop();
			int n2 = stack.pop();
			int result = n2 - n1;
			stack.push(result);
		}
		else if(token.equals("*")) {
			int n1 = stack.pop();
			int n2 = stack.pop();
			int result = n2 * n1;
			stack.push(result);
		}
		else if(token.equals("/")) {
			int n1 = stack.pop();
			int n2 = stack.pop();
			int result = n2 / n1;
			stack.push(result);
		}
		else {
			int number = Integer.parseInt(token);
			stack.push(number);
		}
	}
	
	public static void main(String[] args) throws Exception {
		args = new String[]{"2", "1", "+", "3", "*"};
		args = new String[]{"4", "13", "5", "/", "+"};
	    System.out.println(new ReversePolishNotation().evalRPN(args));
    }
}
