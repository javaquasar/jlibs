/**
 * JLibs: Common Utilities for Java
 * Copyright (C) 2009  Santhosh Kumar T
 * <p/>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

package jlibs.xml.sax.sniff;

/**
 * @author Santhosh Kumar T
 */
class DescendantSelf extends Descendant implements Self{
    protected DescendantSelf(Node parent){
        super(parent);
    }

    @Override
    protected String getStep(){
        return "";
    }

    @Override
    protected boolean canMerge(Node node){
        return node.getClass()==getClass();
    }
}