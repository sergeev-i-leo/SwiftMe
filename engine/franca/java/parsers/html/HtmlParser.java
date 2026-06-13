package franca.java.parsers.html;

import franca.java.expected.StringBuffer;
import franca.java.expected.Runtime;
import franca.java.parsers.Parser;
import franca.java.parsers.json.JsonArray;
import franca.java.parsers.json.JsonObject;
import franca.java.parsers.json.JsonStringPrimitive;

public class HtmlParser extends Parser {

  // 0 for nothing, 1 for nothing with input flag, 2 for debugging
  public int debuggingLevel = 0;

  public JsonArray parse(String input) {
    if (this.input != null) {
      delete(this.input);
    }
    this.input = copyOf(input);

    position = 0;

    JsonArray jsonArray = new JsonArray();
    parseHtmlNodeContents(null, jsonArray);

    return jsonArray;
  }

  private void parseHtmlNodeContents(String tagName, JsonArray jsonArray) {

    while (true) {

      while ((peekCharacter() == '<') && (peekNextCharacter(1) == '!')) {
        appendTextJsonObject(jsonArray, "<!");
        parseTextContents(jsonArray);
        skipWhitespaces();
      }

      // can be part of inline element <span>, <strong>, <em>
      parseTextContents(jsonArray);

      if (position >= input.length()) {
        // html is broken
        skipCharacters(1);
        return;
      }

      if (peekCharacter() != '<') {
        // oops, tag is missing
        skipCharacters(1);
        return;
      }

      if (peekNextCharacter(1) == '/') {
        // closing tag
        skipCharacters(2);
        String closingTagName = parseTagName();
        if ((position < input.length()) && (peekCharacter() == '>')) {
          skipCharacters(1);
        }

        if (debuggingLevel > 1) {
          System.out.println("</" + closingTagName + ">");
        }

        if (tagName == null) {
          // root of the document
          return;
        }
        if (closingTagName.equals(tagName)) {
          return;
        }
      }

      skipWhitespaces();

      int storedPosition = position;
      parseHtmlNode(jsonArray);
      if (storedPosition == position) {
        // avoid cycling
        return;
      }
    }
  }

  public void parseHtmlNode(JsonArray jsonArray) {
    if (consumeCharacter() != '<') {
      // avoid cycling
      skipCharacters(1);
      return;
    }

    String tagName = parseTagName();

    if (debuggingLevel > 1) {
      System.out.println("<" + tagName + ">");
    }

    JsonObject jsonObject = new JsonObject();
    jsonArray.add(jsonObject);
    jsonObject.putStringValue("tagName", tagName);
    parseHtmlAttributes(jsonObject);

    if (tagName.equals("img")) {
      // self-closing
      if ((peekCharacter() == '/') && (peekNextCharacter(1) == '>')) {
        skipCharacters(2);
      } else if (peekCharacter() == '>') {
        skipCharacters(1);
      }
      return;
    }

    if ((peekCharacter() == '/') && (peekNextCharacter(1) == '>')) {
      // self-closing
      skipCharacters(2);
      return;
    }

    // '>'
    skipCharacters(1);

    jsonArray = new JsonArray();
    jsonObject.put("contents", jsonArray);
    skipWhitespaces();
    parseHtmlNodeContents(tagName, jsonArray);
  }

  private String parseTagName() {
    StringBuffer stringBuffer = new StringBuffer();
    while ((position < input.length()) && (Character.isLetterOrDigit(peekCharacter()))) {
      char c = consumeCharacter();
      stringBuffer.appendCharacter(c);
    }
    String string = stringBuffer.getLowerCaseString();
    delete(stringBuffer);
    return string;
  }

  private void parseHtmlAttributes(JsonObject jsonObject) {

    JsonArray attributesJsonArray = new JsonArray();
    jsonObject.put("attributes", attributesJsonArray);
    JsonArray styleJsonArray = new JsonArray();
    jsonObject.put("style", styleJsonArray);

    while ((position < input.length()) && (peekCharacter() != '>') && (peekCharacter() != '/')) {

      skipWhitespaces();

      // attribute name

      String attributeName = parseAttributeName();
      if (attributeName == null) {
        return;
      }

      skipWhitespaces();

      if (peekCharacter() != '=') {
        attributesJsonArray.add(new JsonStringPrimitive(attributeName));
        if (debuggingLevel > 1) {
          System.out.println("boolean attribute found " + attributeName);
        }
        delete(attributeName);
        continue;
      }

      skipCharacters(1);

      parseAttributeValue(attributeName, attributesJsonArray, styleJsonArray);
      delete(attributeName);
    }
  }

  private String parseAttributeName() {
    StringBuffer stringBuffer = new StringBuffer();
    while ((position < input.length()) && (isAttributeNameCharacter(peekCharacter()))) {
      char c = consumeCharacter();
      stringBuffer.appendCharacter(c);
    }
    if (stringBuffer.isEmpty()) {
      delete(stringBuffer);
      return null;
    }
    String string = stringBuffer.getLowerCaseString();
    delete(stringBuffer);
    return string;
  }

  private boolean isAttributeNameCharacter(char c) {
    return (c != '=') && (c != '>') && (c != '/') && (!Character.isWhitespace(c));
  }

  private void parseAttributeValue(String attributeName, JsonArray attributesJsonArray, JsonArray styleJsonArray) {
    skipWhitespaces();

    char attributeValueDelimiter = peekCharacter();
    if ((attributeValueDelimiter != '\"') && (attributeValueDelimiter != '\'')) {
      StringBuffer stringBuffer = new StringBuffer();
      while (position < input.length()) {
        char c = peekCharacter();
        if ((c == '>') || (c == '/') || (isWhitespace(c))) {
          break;
        }
        stringBuffer.appendCharacter(consumeCharacter());
      }
      JsonObject attributeJsonObject = new JsonObject();
      attributesJsonArray.add(attributeJsonObject);
      attributeJsonObject.putStringValue(attributeName, stringBuffer.toString());

      if (debuggingLevel > 1) {
        System.out.println("unquoted attribute found " + attributeName + " : " + stringBuffer.getString());
      }

      delete(stringBuffer);
      return;
    }

    skipCharacters(1);

    if (!attributeName.equals("style")) {
      StringBuffer stringBuffer = new StringBuffer();
      while (position < input.length()) {
        char c = peekCharacter();
        if (c == '\\') {
          skipCharacters(1);
          c = consumeCharacter();
          stringBuffer.appendCharacter(c);
          continue;
        }
        if (c == attributeValueDelimiter) {
          skipCharacters(1);
          break;
        }
        if ((c == '>') || (c == '/')) {
          break;
        }
        stringBuffer.appendCharacter(c);
        skipCharacters(1);
      }
      if (stringBuffer.isEmpty()) {
        delete(stringBuffer);
        return;
      }
      JsonObject attributeJsonObject = new JsonObject();
      attributesJsonArray.add(attributeJsonObject);
      attributeJsonObject.putStringValue(attributeName, stringBuffer.toString());

      if (debuggingLevel > 1) {
        System.out.println("quoted attribute found " + attributeName + " : " + stringBuffer.getString());
      }

      delete(stringBuffer);
      return;
    }

    while (position < input.length()) {
      if (peekCharacter() == attributeValueDelimiter) {
        break;
      }

      String styleName = parseStyleName();
      if (styleName == null) {
        return;
      }
      skipWhitespaces();
      if (peekCharacter() != ':') {
        delete(styleName);
        break;
      }
      skipCharacters(1);

      skipWhitespaces();
      StringBuffer stringBuffer = parseStyleValue();
      if (stringBuffer == null) {
        delete(styleName);
        break;
      }
      JsonObject styleJsonObject = new JsonObject();
      styleJsonArray.add(styleJsonObject);
      styleJsonObject.putStringValue(styleName, stringBuffer.getString());

      if (debuggingLevel > 1) {
        System.out.println("style found " + styleName + " : " + stringBuffer.getString());
      }

      delete(styleName);
      delete(stringBuffer);

      skipWhitespaces();
      if (peekCharacter() != ';') {
        break;
      }

      skipCharacters(1);
    }
  }

  private String parseStyleName() {
    StringBuffer stringBuffer = new StringBuffer();
    while ((position < input.length()) && (isAttributeNameCharacter(peekCharacter()))) {
      char c = consumeCharacter();
      stringBuffer.appendCharacter(c);
    }
    if (stringBuffer.isEmpty()) {
      delete(stringBuffer);
      return null;
    }
    String string = stringBuffer.getLowerCaseString();
    delete(stringBuffer);
    return string;
  }

  private StringBuffer parseStyleValue() {
    skipWhitespaces();

    StringBuffer stringBuffer = new StringBuffer();
    boolean insideQuotes = false;
    char quoteCharacter = 0;

    while (position < input.length()) {
      char c = peekCharacter();

      if (c == '\\') {
        skipCharacters(1);
        c = consumeCharacter();
        stringBuffer.appendCharacter(c);
        continue;
      }

      // quotes
      if (((c == '"') || (c == '\'')) && (!insideQuotes)) {
        insideQuotes = true;
        quoteCharacter = c;
        stringBuffer.appendCharacter(consumeCharacter());
        continue;
      }
      if ((insideQuotes) && (c == quoteCharacter)) {
        insideQuotes = false;
        quoteCharacter = 0;
        stringBuffer.appendCharacter(consumeCharacter());
        continue;
      }

      // inside quotes
      if (insideQuotes) {
        stringBuffer.appendCharacter(consumeCharacter());
        continue;
      }

      // outside quotes
      if ((c == ';') || (c == '>') || (c == '/')) {
        break;
      }

      stringBuffer.appendCharacter(consumeCharacter());
    }

    if (stringBuffer.isEmpty()) {
      delete(stringBuffer);
      return null;
    }

    return stringBuffer;
  }

  private void parseTextContents(JsonArray jsonArray) {

    StringBuffer textStringBuffer = new StringBuffer();
    StringBuffer htmlLetterStringBuffer = null;

    while (position < input.length()) {
      char c = peekCharacter();
      if (c == '<') {
        if (peekString("<br>")) {
          if (textStringBuffer.isNotEmpty()) {
            appendTextJsonObject(jsonArray, textStringBuffer.toString());
            delete(textStringBuffer);
            textStringBuffer = new StringBuffer();
          }
          appendTextJsonObject(jsonArray, "<br>");
          skipCharacters(4);
          continue;
        } else if (peekNextCharacter(1) != '!') {
          break;
        }
      }

      if (c == '\r') {
        skipCharacters(1);
        if (peekCharacter() == '\n') {
          skipCharacters(1);
        }
        skipWhitespaces();
        continue;
      }

      if ((peekNextCharacter(0) == '&') && ((peekNextCharacter(1) == '#'))) {
        if (htmlLetterStringBuffer != null) {
          // html letter not finished
          textStringBuffer.appendString(htmlLetterStringBuffer.getString());
          delete(htmlLetterStringBuffer);
        }
        htmlLetterStringBuffer = new StringBuffer();
        skipCharacters(2);
        continue;
      }
      if (htmlLetterStringBuffer != null) {
        c = peekCharacter();
        if (c == ';') {
          // try to convert to char
          Integer parsedInteger = Runtime.stringToHexInteger(htmlLetterStringBuffer.getString());
          if (parsedInteger == null) {
            textStringBuffer.appendString(htmlLetterStringBuffer.getString());
          } else {
            textStringBuffer.appendCharacter((char) parsedInteger.intValue());
          }
          delete(htmlLetterStringBuffer);
          htmlLetterStringBuffer = null;
          skipCharacters(1);
          continue;
        }
        if (Character.isLetterOrDigit(c)) {
          htmlLetterStringBuffer.appendCharacter(c);
          skipCharacters(1);
          continue;
        }
        // error in html
        textStringBuffer.appendString(htmlLetterStringBuffer.getString());
        delete(htmlLetterStringBuffer);
        htmlLetterStringBuffer = null;
      }
      if (peekString("&amp;")) {
        textStringBuffer.appendString("&");
        skipCharacters(5);
        continue;
      }
      if (peekString("&lt;")) {
        textStringBuffer.appendString("<");
        skipCharacters(4);
        continue;
      }
      if (peekString("&gt;")) {
        textStringBuffer.appendString(">");
        skipCharacters(4);
        continue;
      }
      if (peekString("&quot;")) {
        textStringBuffer.appendString("\"");
        skipCharacters(6);
        continue;
      }
      if (peekString("&#39;")) {
        textStringBuffer.appendString("'");
        skipCharacters(5);
        continue;
      }
      if (peekString("&nbsp;")) {
        textStringBuffer.appendString(" ");
        skipCharacters(6);
        continue;
      }
      if (peekString("&Aacute;")) {
        textStringBuffer.appendString("Á");
        position += 8;
        continue;
      }
      if (peekString("&aacute;")) {
        textStringBuffer.appendString("á");
        position += 8;
        continue;
      }
      if (peekString("&Eacute;")) {
        textStringBuffer.appendString("É");
        position += 8;
        continue;
      }
      if (peekString("&eacute;")) {
        textStringBuffer.appendString("é");
        position += 8;
        continue;
      }
      if (peekString("&Iacute;")) {
        textStringBuffer.appendString("Í");
        position += 8;
        continue;
      }
      if (peekString("&iacute;")) {
        textStringBuffer.appendString("í");
        position += 8;
        continue;
      }
      if (peekString("&Oacute;")) {
        textStringBuffer.appendString("Ó");
        position += 8;
        continue;
      }
      if (peekString("&oacute;")) {
        textStringBuffer.appendString("ó");
        position += 8;
        continue;
      }
      if (peekString("&Uacute;")) {
        textStringBuffer.appendString("Ú");
        position += 8;
        continue;
      }
      if (peekString("&uacute;")) {
        textStringBuffer.appendString("ú");
        position += 8;
        continue;
      }
      if (peekString("&Ntilde;")) {
        textStringBuffer.appendString("Ñ");
        position += 8;
        continue;
      }
      if (peekString("&ntilde;")) {
        textStringBuffer.appendString("ñ");
        position += 8;
        continue;
      }
      if (peekString("&copy;")) {
        textStringBuffer.appendString("©");
        skipCharacters(6);
        continue;
      }
      if (peekString("&reg;")) {
        textStringBuffer.appendString("®");
        skipCharacters(5);
        continue;
      }
      if (peekString("&trade;")) {
        textStringBuffer.appendString("™");
        position += 7;
        continue;
      }
      if (peekString("&euro;")) {
        textStringBuffer.appendString("€");
        skipCharacters(6);
        continue;
      }
      if (peekString("&pound;")) {
        textStringBuffer.appendString("£");
        position += 7;
        continue;
      }
      if (peekString("&cent;")) {
        textStringBuffer.appendString("¢");
        skipCharacters(6);
        continue;
      }
      if (peekString("&yen;")) {
        textStringBuffer.appendString("¥");
        skipCharacters(5);
        continue;
      }

      textStringBuffer.appendCharacter(consumeCharacter());
    }

    if (htmlLetterStringBuffer != null) {
      // not completed html character
      textStringBuffer.appendString(htmlLetterStringBuffer.getString());
      delete(htmlLetterStringBuffer);
    }

    if (textStringBuffer.isNotEmpty()) {
      if (debuggingLevel > 1) {
        System.out.println("text found " + textStringBuffer.getString());
      }
      appendTextJsonObject(jsonArray, textStringBuffer.getString());
      delete(textStringBuffer);
    }
  }

  private void appendTextJsonObject(JsonArray jsonArray, String text) {
    JsonObject jsonObject = new JsonObject();
    jsonArray.add(jsonObject);
    jsonObject.putStringValue("tagName", "#text");
    jsonObject.putStringValue("value", text);
  }

  @Override
  public char peekCharacter() {
    if (debuggingLevel > 0) {
      if (peekString("<!-- debuggingLevel = 0")) {
        debuggingLevel = 0;
      } else if (peekString("<!-- debuggingLevel = 1")) {
        debuggingLevel = 1;
      } else if (peekString("<!-- debuggingLevel = 2")) {
        debuggingLevel = 2;
      }
    }
    return super.peekCharacter();
  }
}
