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

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.junit.Test;

public class ListTest {

	IValueFactory vf = new ValueFactory();
	
	IValue i1 = vf.integer(1);
	IValue i2 = vf.integer(2);	
	IValue i3 = vf.integer(3);
	IValue i4 = vf.integer(4);	
	
	IList empty = vf.list(TypeFactory.getInstance().integerType());

	IList one = vf.list(i1);

	IList two = vf.list(i2);
	
	IList oneTwo = vf.list(i1, i2);

	IList oneOne = vf.list(i1, i1);
	
	IList twoOne = vf.list(i2, i1);

	IList twoTwo = vf.list(i2, i2);		
	
	IList oneTwoTwoOne = vf.list(i1, i2, i2, i1);

	IList oneTwoOneTwo = vf.list(i1, i2, i1, i2);	

	IList oneOneTwo = vf.list(i1, i1, i2);		

	IList oneTwoOne = vf.list(i1, i2, i1);			
	
	IList twoTwoOne = vf.list(i2, i2, i1);	
	
	
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
