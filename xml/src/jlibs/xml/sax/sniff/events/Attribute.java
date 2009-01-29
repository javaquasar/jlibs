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

package jlibs.xml.sax.sniff.events;

import org.xml.sax.Attributes;

/**
 * @author Santhosh Kumar T
 */
public class Attribute extends Event{
    public Attribute(DocumentOrder documentOrder){
        super(documentOrder);
    }

    @Override
    public int type(){
        return ATTRIBUTE;
    }

    public String uri;
    public String name;
    public String qname;
    public String value;

    public void setData(Attributes attrs, int index){
        uri = attrs.getURI(index);
        name = attrs.getLocalName(index);
        qname = attrs.getQName(index);
        value = attrs.getValue(index);
        setResultWrapper(value);
    }
}