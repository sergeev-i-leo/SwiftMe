package me.swift.engine.expected;

import me.swift.engine.TranspilableClass;

public class ExpectedStringBuilder extends TranspilableClass {

  private StringBuilder stringBuilder = new StringBuilder();

  public void appendCharacter(char character) {
    stringBuilder.append(character);
  }

  public void appendString(String string) {
    stringBuilder.append(string);
  }

  public String getString() {
    return stringBuilder.toString();
  }
}
