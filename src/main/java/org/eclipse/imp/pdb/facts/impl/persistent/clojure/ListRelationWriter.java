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

class ListRelationWriter extends ListWriter implements IListRelationWriter {

	protected ListRelationWriter(Type et) {
		super(et);
	}

	@Override
	public IListRelation done() {
		return (IListRelation) super.done();
	}

}

class ListRelationWriterWithTypeInference extends ListRelationWriter {

	protected ListRelationWriterWithTypeInference() {
		super(TypeFactory.getInstance().voidType());
	}
	
}