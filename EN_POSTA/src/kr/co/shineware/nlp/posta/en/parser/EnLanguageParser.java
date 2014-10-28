package kr.co.shineware.nlp.posta.en.parser;

import kr.co.shineware.nlp.posta.modeler.parser.LanguageParser;

public class EnLanguageParser extends LanguageParser{
	public String parsing(String in){
		return " "+in+" ";
	}
	public String unparsing(String in){
		return in.trim();
	}
}
