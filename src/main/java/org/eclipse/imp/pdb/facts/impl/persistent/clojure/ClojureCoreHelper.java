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

import clojure.core$concat;
import clojure.core$conj;
import clojure.core$cons;
import clojure.core$drop;
import clojure.core$next;
import clojure.core$nil_QMARK_;
import clojure.core$nth;
import clojure.core$nthnext;
import clojure.core$rest;
import clojure.core$reverse;
import clojure.core$some;
import clojure.core$split_at;
import clojure.core$take;
import clojure.lang.IFn;

class ClojureCoreHelper {

	public final static IFn core$concat = new core$concat();
	public final static IFn core$conj = new core$conj();
	public final static IFn core$cons = new core$cons();
	public final static IFn core$drop = new core$drop();
	public final static IFn core$next = new core$next();
	public final static IFn core$nil_QMARK_ = new core$nil_QMARK_();
	public final static IFn core$nth = new core$nth();
	public final static IFn core$nthnext = new core$nthnext();
	public final static IFn core$rest = new core$rest();
	public final static IFn core$reverse = new core$reverse();
	public final static IFn core$some = new core$some();
	public final static IFn core$split_at = new core$split_at();
	public final static IFn core$take = new core$take();	
	
}
