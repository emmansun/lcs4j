package org.emmansun.lcs.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.emmansun.lcs.LcsPair;
import org.emmansun.lcs.NormalLcs;
import org.junit.Test;

public class NormalLcsTest {
	@Test
	public void test_Sample() {
		List<Character> model = new ArrayList<Character>();
		model.add('B');
		model.add('A');
		model.add('D');
		model.add('C');
		model.add('D');
		model.add('C');
		model.add('B');
		model.add('A');		
		List<Character> sample = new ArrayList<Character>();
		sample.add('A');
		sample.add('B');
		sample.add('C');
		sample.add('D');
		sample.add('C');
		sample.add('D');
		sample.add('A');
		sample.add('B');
		NormalLcs<Character> lcs = new NormalLcs<Character>(model, sample);
		Collection<LcsPair> result = lcs.findAllLcs();
		assertEquals(8, result.size());
	}

	@Test
	public void test_wiki_Sample1() {
		List<Character> model = new ArrayList<Character>();
		model.add('G');
		model.add('A');
		model.add('C');
		List<Character> sample = new ArrayList<Character>();
		sample.add('A');
		sample.add('G');
		sample.add('C');
		sample.add('A');
		sample.add('T');
		NormalLcs<Character> lcs = new NormalLcs<Character>(model, sample);
		Collection<LcsPair> result = lcs.findAllLcs();
		System.out.println(result);
		assertEquals(3, result.size());
	}
	
	@Test
	public void test_loop1() {
		List<Character> model = new ArrayList<Character>();
		model.add('A');
		model.add('B');
		model.add('A');
		model.add('B');
		List<Character> sample = new ArrayList<Character>();
		sample.add('A');
		sample.add('B');
		sample.add('A');
		sample.add('B');
		sample.add('A');
		sample.add('B');
		NormalLcs<Character> lcs = new NormalLcs<Character>(model, sample);
		Collection<LcsPair> result = lcs.findAllLcs();
		System.out.println(result);
		assertEquals(5, result.size());
	}
	
	@Test
	public void test_complexCase() {
		List<Character> model = TestUtils.createListFromString("ABCAAABBABBCCABCBACABABABCCBC");
		List<Character> sample = TestUtils.createListFromString("ABCABABABCBACABCBACABABACBCB");
		NormalLcs<Character> lcs = new NormalLcs<Character>(model, sample);
		Collection<LcsPair> result = lcs.findAllLcs();
		assertEquals(220, result.size());
	}

}
