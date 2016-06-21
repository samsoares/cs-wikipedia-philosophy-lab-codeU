package com.flatironschool.javacs;

import java.io.IOException;
import java.util.*;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {

	final static WikiFetcher wf = new WikiFetcher();
    final static String BASE_URL = "https://en.wikipedia.org";
    final static String PHILO_URL = "https://en.wikipedia.org/wiki/Philosophy";
	private String url;
	private List<String> visitedUrls; 

	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 *
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 *
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 *
	 * @param args
	 * @throws IOException
	 */

    public WikiPhilosophy(String url){
	    this.url = url;
	    visitedUrls = new ArrayList<String>(); 
	}

	public static void main(String[] args) throws IOException {
        String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		WikiPhilosophy wp = new WikiPhilosophy(url); 
		wp.visit(); 

	}
	
	private void visit () throws IOException{
		do {
		    visitedUrls.add(url);
            System.out.println ("Visited: " + url);
			Iterable<Node> iter = getFirstParagraphNodes(url);
            url = getFirstValidLink(iter);
		} while (!visitedUrls.contains(url) && !PHILO_URL.equals(url) && url.length() > 0);

		if (PHILO_URL.equals(url)){
			System.out.println ("Visited: " + url);
			System.out.println ("SUCCESS");
		} else {
			System.out.println ("FAILED");
		}
	}

	private String getFirstValidLink (Iterable<Node> iter) {
	     for (Node node: iter) {
	          if (node.hasAttr("href")) {
	               for (Node n : node.childNodes()) {
		               if (n instanceof TextNode) {
		                    String title = ((TextNode) n).text();
		                    if (isValid((TextNode) n)) {
			                    return node.absUrl("href");
		                    }
		               }
	               }
	          }
	     }
	     return "";
	} 
	

	private static Iterable<Node> getFirstParagraphNodes (String url) throws IOException {
		Elements paragraphs = wf.fetchWikipedia(url);
		Element firstPara = paragraphs.get(0);
		return new WikiNodeIterable(firstPara);
	}

	private boolean isValid (TextNode node) {
		String title = node.text();
		return Character.isLowerCase(title.charAt(0));
	}
	
	public void processElements(Elements paragraphs) {
        for (Node node: paragraphs) {
            processTree(node);
        }
    }
 
    public void processTree(Node root) {
        for (Node node: new WikiNodeIterable(root)) {
            if (node instanceof TextNode) {
                processTextNode(((TextNode) node));
            }
        }
    }
 
    public void processTextNode(TextNode tNode) {
    	
    }
}
