/*******************************************************************************
 * Copyright (c) 2012-2013 CWI
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

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

import clojure.lang.IPersistentVector;

public class VectorOrRel {	
	
	@SuppressWarnings("unchecked")
	public static <IListOrRel extends IList> IListOrRel apply(Type et, IPersistentVector xs) {
		Type elementType = (xs.count() == 0) ? TypeFactory.getInstance().voidType() : et;
		
		if (elementType.isTupleType())
			return (IListOrRel) new VectorRelation(elementType, xs);
		else
			return (IListOrRel) new Vector(elementType, xs);
	}
	
}
