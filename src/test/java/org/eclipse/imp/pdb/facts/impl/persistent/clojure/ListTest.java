/*******************************************************************************
 * Copyright (c) 2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI  
 *******************************************************************************/
package org.eclipse.imp.pdb.facts.impl.persistent.clojure;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.IntegerValue;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.junit.Test;

import clojure.lang.RT;

public class ListTest {

	IValue i1 = new IntegerValue(1);
	IValue i2 = new IntegerValue(2);	
	IValue i3 = new IntegerValue(3);
	IValue i4 = new IntegerValue(4);	
	
	List empty = new List(TypeFactory.getInstance().integerType());

	List one = new List(TypeFactory.getInstance().integerType(),
			RT.seq(Arrays.asList(i1)));

	List two = new List(TypeFactory.getInstance().integerType(),
			RT.seq(Arrays.asList(i2)));	
	
	List oneTwo = new List(TypeFactory.getInstance().integerType(),
			RT.seq(Arrays.asList(i1, i2)));

	List oneOne = new List(TypeFactory.getInstance().integerType(),
			RT.seq(Arrays.asList(i1, i1)));
	
	List twoOne = new List(TypeFactory.getInstance().integerType(),
			RT.seq(Arrays.asList(i2, i1)));	

	List twoTwo = new List(TypeFactory.getInstance().integerType(),
			RT.seq(Arrays.asList(i2, i2)));		
	
	List oneTwoTwoOne = new List(TypeFactory.getInstance().integerType(),
			RT.seq(Arrays.asList(i1, i2, i2, i1)));

	List oneTwoOneTwo = new List(TypeFactory.getInstance().integerType(),
			RT.seq(Arrays.asList(i1, i2, i1, i2)));	

	List oneOneTwo = new List(TypeFactory.getInstance().integerType(),
			RT.seq(Arrays.asList(i1, i1, i2)));		

	List oneTwoOne = new List(TypeFactory.getInstance().integerType(),
			RT.seq(Arrays.asList(i1, i2, i1)));			
	
	List twoTwoOne = new List(TypeFactory.getInstance().integerType(),
			RT.seq(Arrays.asList(i2, i2, i1)));	
	
	
	@Test
	public void testAppend() {
		assertTrue(empty.append(i1).append(i2).equals(oneTwo));
	}
	
	@Test
	public void testConcat() {
		assertTrue(oneTwo.concat(twoOne).equals(oneTwoTwoOne));
	}

	@Test
	public void testContains() {
		assertTrue(oneTwo.contains(i1));
		assertTrue(oneTwo.contains(i2));
		assertFalse(oneTwo.contains(i3));
		assertFalse(oneTwo.contains(i4));
		
		assertFalse(empty.contains(i1));
		assertFalse(empty.contains(i2));		
	}
	
	@Test
	public void testDelete() {
		assertEquals(one, oneTwo.delete(1));
		assertEquals(two, oneTwo.delete(0));
		assertEquals(oneOne, oneTwoTwoOne.delete(1).delete(1));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testDeleteFromInvalidIndexSmaller() {
		assertEquals(oneTwo, oneTwo.delete(-1));
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testDeleteFromInvalidIndexBigger() {
		assertEquals(oneTwo, oneTwo.delete(2));
	}
	
	@Test
	public void testDeleteValue() {
		assertEquals(one, oneTwo.delete(i2));
		assertEquals(two, oneTwo.delete(i1));
		assertEquals(oneTwo, oneTwo.delete(i3));
		assertEquals(oneTwo, oneTwo.delete(i4));
		assertEquals(oneTwoOne, oneTwoTwoOne.delete(i2));
		assertEquals(twoTwoOne, oneTwoTwoOne.delete(i1));		
	}	
	
	@Test
	public void testGet() {
		assertEquals(i1, oneTwo.get(0));
		assertEquals(i2, oneTwo.get(1));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetFromInvalidIndexSmaller() {
		oneTwo.get(-1);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetFromInvalidIndexBigger() {
		oneTwo.get(2);
	}
	
	@Test
	public void testPut() {
		assertEquals(oneTwo, oneOne.put(1, i2));
		assertEquals(twoOne, oneOne.put(0, i2));
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testPutFromInvalidIndexSmaller() {
		assertEquals(oneTwo, oneTwo.put(-1, i1));
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testPutFromInvalidIndexBigger() {
		assertEquals(oneTwo, oneTwo.put(2, i2));
	}
	
	@Test
	public void testReverse() {
		assertTrue(empty.reverse().equals(empty));
		assertTrue(oneTwo.reverse().equals(twoOne));
	}

	@Test
	public void testSublist() {
		assertEquals(oneTwo, oneTwoTwoOne.sublist(0, 2));
		assertEquals(twoOne, oneTwoTwoOne.sublist(2, 2));
		assertEquals(oneTwoTwoOne, oneTwoTwoOne.sublist(0, 4));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testSublistFromInvalidIndexSmaller() {
		oneTwoTwoOne.sublist(-1, 2);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testSublistFromInvalidIndexBigger() {
		oneTwoTwoOne.sublist(4, 2);
	}	

	@Test(expected=IndexOutOfBoundsException.class)
	public void testSublistLengthToLong() {
		oneTwoTwoOne.sublist(0, 5);
	}	
		
}
