package org.emmansun.lcs.test;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {
	public static List<Character> createListFromString(String str) {
		List<Character> list = new ArrayList<Character>();
		for (int i=0; i<str.length(); i++) {
			list.add(str.charAt(i));
		}
		return list;
	}
}
