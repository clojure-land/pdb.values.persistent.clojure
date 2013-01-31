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

import org.eclipse.imp.pdb.facts.IListRelation;
import org.eclipse.imp.pdb.facts.IListRelationWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

import clojure.lang.IPersistentVector;

class VectorRelationWriter extends VectorWriter implements IListRelationWriter {

	protected VectorRelationWriter(Type et) {
		super(et);
	}

	@Override
	public IListRelation done() {
		return VectorOrRel.apply(et, (IPersistentVector) xs.persistent());
	}

}

class VectorRelationWriterWithTypeInference extends VectorRelationWriter {

	protected VectorRelationWriterWithTypeInference() {
		super(TypeFactory.getInstance().voidType());
	}
	
}