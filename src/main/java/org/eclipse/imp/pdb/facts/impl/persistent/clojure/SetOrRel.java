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

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

import clojure.lang.IPersistentSet;

public class SetOrRel {

	@SuppressWarnings("unchecked")
	public static <ISetOrRel extends ISet> ISetOrRel apply(Type et, IPersistentSet xs) {
		Type elementType = (xs.count() == 0) ? TypeFactory.getInstance().voidType() : et;
		
		if (elementType.isTupleType())
			return (ISetOrRel) new Relation(elementType, xs);
		else
			return (ISetOrRel) new Set(elementType, xs);
	}
	
}
