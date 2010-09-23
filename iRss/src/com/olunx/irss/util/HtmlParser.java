package com.olunx.irss.util;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

public class HtmlParser {

	/**
	 * 处理各个节点的样式
	 * 
	 * @param list
	 * @return
	 */
	private NodeList parseNodes(NodeList list) {
		if (list == null)
			return null;

		NodeList nodes = new NodeList();
		Node node = null;
		SimpleNodeIterator iterator = list.elements();

		while (iterator.hasMoreNodes()) {
			node = iterator.nextNode();
			if (node == null)
				break;

			// 处理各种包含样式的节点
			if (node instanceof ImageTag) {// img
				ImageTag img = (ImageTag) node;
				img.setImageURL("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
			} else if (node instanceof ParagraphTag) {// p
				ParagraphTag p = (ParagraphTag) node;
				if (p.getAttribute("style") != null)
					p.setAttribute("style", "none");
			} else if (node instanceof Div) {// div
				Div div = (Div) node;
				if (div.getAttribute("style") != null)
					div.setAttribute("style", "none");
			} else if (node instanceof Span) {// span
				Span span = (Span) node;
				if (span.getAttribute("style") != null)
					span.setAttribute("style", "none");
			} else if (node instanceof TagNode) {// pre
				TagNode tag = (TagNode) node;
				if (tag.getAttribute("style") != null)
					tag.setAttribute("style", "none");
			}

			parseNodes(node.getChildren());// 处理子节点
			nodes.add(node);
		}

		return nodes;
	}

	/**
	 * @throws ParserException
	 */
	public void parse(){
		Parser parser = new Parser();
		NodeList list = null;
		try {
			parser.setURL("html/feed.html");
			list = parser.parse(null);
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		System.out.println(parseNodes(list).toHtml());
	}
}