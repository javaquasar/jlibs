/**
 * JLibs: Common Utilities for Java
 * Copyright (C) 2009  Santhosh Kumar T <santhosh.tekuri@gmail.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

package jlibs.nblr.editor.serialize;

import jlibs.core.lang.NotImplementedException;
import jlibs.nblr.Syntax;
import jlibs.nblr.actions.Action;
import jlibs.nblr.actions.BufferAction;
import jlibs.nblr.actions.EventAction;
import jlibs.nblr.actions.PublishAction;
import jlibs.nblr.matchers.*;
import jlibs.nblr.rules.Edge;
import jlibs.nblr.rules.Node;
import jlibs.nblr.rules.Rule;
import jlibs.xml.sax.XMLDocument;
import org.xml.sax.SAXException;

import javax.xml.transform.Result;
import javax.xml.transform.TransformerConfigurationException;
import java.util.ArrayList;

/**
 * @author Santhosh Kumar T
 */
public class SyntaxDocument extends XMLDocument{
    public SyntaxDocument(Result result) throws TransformerConfigurationException{
        super(result, false, 4, null);
    }

    public void add(Syntax syntax) throws SAXException{
        startElement("syntax");
        for(Matcher matcher: syntax.matchers.values())
            add(matcher);
        for(Rule rule: syntax.rules.values())
            add(rule);
        endElement();
    }

    public void add(Matcher matcher) throws SAXException{
        startElement(matcher.getClass().getSimpleName().toLowerCase());
        addAttribute("name", matcher.name);

        if(matcher instanceof Any){
            Any any = (Any)matcher;
            if(any.chars!=null)
                addAttribute("chars", new String(any.chars, 0, any.chars.length));
        }else if(matcher instanceof Range){
            Range range = (Range)matcher;
            addAttribute("from", ""+range.from);
            addAttribute("to", ""+range.to);
        }else if(matcher instanceof Not){
            Not not = (Not)matcher;
            add(not.delegate);
        }else if(matcher instanceof And){
            And and = (And)matcher;
            for(Matcher operand: and.operands)
                add(operand);
        }else if(matcher instanceof Or){
            Or or = (Or)matcher;
            for(Matcher operand: or.operands)
                add(operand);
        }else
            throw new NotImplementedException(matcher.getClass().getName());

        endElement();
    }

    public void add(Rule rule) throws SAXException{
        startElement("rule");
        addAttribute("name", rule.name);

        ArrayList<Node> nodes = new ArrayList<Node>();
        ArrayList<Edge> edges = new ArrayList<Edge>();
        rule.computeIDS(nodes, edges, rule.node);
        for(Node node: nodes)
            add(node);
        for(Edge edge: edges)
            add(edge);
        endElement();
    }

    public void add(Node node) throws SAXException{
        startElement("node");
        addAttribute("name", node.name);
        if(node.action!=null)
            add(node.action);
        endElement();
    }

    public void add(Edge edge) throws SAXException{
        startElement("edge");
        addAttribute("source", ""+edge.source.id);
        addAttribute("target", ""+edge.target.id);
        addAttribute("fallback", ""+edge.fallback);
        if(edge.matcher!=null){
            if(edge.matcher.name==null)
                add(edge.matcher);
            else{
                startElement("matcher");
                addAttribute("name", edge.matcher.name);
                endElement();
            }
        }else if(edge.ruleTarget!=null){
            startElement("rule");
            addAttribute("name", edge.ruleTarget.rule.name);
            addAttribute("node", edge.ruleTarget.name);
            endElement();
        }
        endElement();
    }

    public void add(Action action) throws SAXException{
        if(action instanceof BufferAction)
            addElement("buffer", "");
        else if(action instanceof PublishAction){
            PublishAction publishAction = (PublishAction)action;
            startElement("publish");
            addAttribute("name", publishAction.name);
            addAttribute("begin", ""+publishAction.begin);
            addAttribute("end", ""+publishAction.end);
            endElement();
        }else if(action instanceof EventAction){
            EventAction eventAction = (EventAction)action;
            startElement("event");
            addAttribute("name", eventAction.name);
            endElement();
        }else
            throw new NotImplementedException(((Object)action).getClass().getName());
    }
}
