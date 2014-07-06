package common.util.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class JsonUtilTest {
	@Test
	public void test() {
		Object javaObj = new B();
		String jsonString = JsonUtil.toJsonString(javaObj);
		System.out.println(jsonString);
		
		B b = JsonUtil.toJavaObject(jsonString, B.class);
		System.out.println(JsonUtil.toJsonString(b));
	}
	
	public static class A {
		private int i = 10;
		private Float j;
		private List<Integer> ilist;
		private Map<String, Integer> imap;
		private String name = "ni hao";
		
		public int getI() {
			return i;
		}

		public void setI(int i) {
			this.i = i;
		}

		public Float getJ() {
			return j;
		}

		public void setJ(Float j) {
			this.j = j;
		}

		public List<Integer> getIlist() {
			return ilist;
		}

		public void setIlist(List<Integer> ilist) {
			this.ilist = ilist;
		}

		public Map<String, Integer> getImap() {
			return imap;
		}

		public void setImap(Map<String, Integer> imap) {
			this.imap = imap;
		}

		public A() {
			ilist = new ArrayList<Integer>();
			ilist.add(10);
			ilist.add(20);
			imap = new HashMap<String, Integer>();
			imap.put("age", 12);
			imap.put("height", 166);
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
	
	public static class B {
		private A a = new A();
		private List<A> alist;
		private List<A> blist = new ArrayList<JsonUtilTest.A>();
		private List<A> clist = new ArrayList<JsonUtilTest.A>();
		
		public B() {
			clist.add(new A());
			clist.add(new A());
			clist.get(1).setI(99);
		}

		public void setA(A a) {
			this.a = a;
		}

		public A getA() {
			return a;
		}
	}

}
