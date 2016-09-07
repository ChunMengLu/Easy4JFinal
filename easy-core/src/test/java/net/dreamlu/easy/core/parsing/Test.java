package net.dreamlu.easy.core.parsing;

import net.dreamlu.easy.commons.parsing.GenericTokenParser;
import net.dreamlu.easy.commons.parsing.TokenHandler;

public class Test {

    public static void main(String[] args) {
        GenericTokenParser parser = new GenericTokenParser("${", "}", new TokenHandler() {
          
          @Override
          public String handleToken(String content) {
              System.out.println(content);
              return "${" + content.trim() + "}";
          }
      });
      System.out.println(parser.parse("${ a }, ${ b }, ${ c }"));
    }
}
