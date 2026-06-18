package org.projecthusky.cda.elga.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class HtmlToCdaConverter {

    private static final String LINE_SEPARATOR = "\n";
    private static final String CDA_BREAK = "<br/>";
    private static final String PARAGRAPH_START = "<paragraph>";
    private static final String PARAGRAPH_END = "</paragraph>" + LINE_SEPARATOR;
    private static final String CONTENT_END = "</content>";
    private static final String BOLD_CONTENT_START = "<content styleCode=\"Bold\">";
    private static final String ITALIC_CONTENT_START = "<content styleCode=\"Italic\">";
    private static final String UNDERLINE_CONTENT_START = "<content styleCode=\"Underline\">";
    private static final String ORDERED_LIST_START = "<list listType=\"ordered\">" + LINE_SEPARATOR;
    private static final String UNORDERED_LIST_START = "<list listType=\"unordered\">" + LINE_SEPARATOR;
    private static final String LIST_END = "</list>" + LINE_SEPARATOR;
    private static final String ITEM_START = "<item>";
    private static final String ITEM_END = "</item>" + LINE_SEPARATOR;

    private HtmlToCdaConverter() {
        /* This utility class should not be instantiated */
    }

    public static String convert(String htmlInput) {
        Document document = Jsoup.parseBodyFragment(htmlInput);
        StringBuilder cdaBuilder = new StringBuilder();

        appendConvertedNode(document.body(), cdaBuilder);
        return cdaBuilder.toString();
    }

    private static void appendConvertedNode(Node node, StringBuilder cdaBuilder) {
        if (node instanceof TextNode textNode) {
            cdaBuilder.append(textNode.getWholeText());
            return;
        }

        if (node instanceof Element element) {
            appendConvertedElement(element, cdaBuilder);
        }
    }

    private static void appendConvertedElement(Element element, StringBuilder cdaBuilder) {
        switch (element.normalName()) {
            case "h1", "h2", "h3", "h4", "h5", "h6":
                appendWrappedChildren(element, cdaBuilder, PARAGRAPH_START + BOLD_CONTENT_START, CONTENT_END + PARAGRAPH_END);
                break;
            case "p", "div":
                appendBlockElement(element, cdaBuilder);
                break;
            case "b", "strong":
                appendWrappedChildren(element, cdaBuilder, BOLD_CONTENT_START, CONTENT_END);
                break;
            case "i", "em":
                appendWrappedChildren(element, cdaBuilder, ITALIC_CONTENT_START, CONTENT_END);
                break;
            case "u":
                appendWrappedChildren(element, cdaBuilder, UNDERLINE_CONTENT_START, CONTENT_END);
                break;
            case "ul":
                appendWrappedChildren(element, cdaBuilder, UNORDERED_LIST_START, LIST_END);
                break;
            case "ol":
                appendWrappedChildren(element, cdaBuilder, ORDERED_LIST_START, LIST_END);
                break;
            case "li":
                appendWrappedChildren(element, cdaBuilder, ITEM_START, ITEM_END);
                break;
            case "br":
                cdaBuilder.append(CDA_BREAK);
                break;
            default:
                appendChildren(element, cdaBuilder);
        }
    }

    private static void appendBlockElement(Element element, StringBuilder cdaBuilder) {
        if (isBlankBlock(element)) {
            cdaBuilder.append(CDA_BREAK);
            return;
        }

        appendWrappedChildren(element, cdaBuilder, PARAGRAPH_START, PARAGRAPH_END);
    }

    private static boolean isBlankBlock(Element element) {
        return element.text().replace('\u00A0', ' ').trim().isEmpty();
    }

    private static void appendWrappedChildren(Element element, StringBuilder cdaBuilder, String startTag, String endTag) {
        cdaBuilder.append(startTag);
        appendChildren(element, cdaBuilder);
        cdaBuilder.append(endTag);
    }

    private static void appendChildren(Element element, StringBuilder cdaBuilder) {
        for (Node child : element.childNodes()) {
            appendConvertedNode(child, cdaBuilder);
        }
    }
}
