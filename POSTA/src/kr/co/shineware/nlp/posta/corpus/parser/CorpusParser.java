package kr.co.shineware.nlp.posta.corpus.parser;

import java.util.ArrayList;
import java.util.List;

import kr.co.shineware.util.common.model.Pair;

public class CorpusParser {
	private static final String TOKEN_SEPERATOR = " ";
	private static final String POS_SEPERATOR = "/";
	
	public List<Pair<String, String>> parse(String in){
		List<Pair<String,String>> wordPosList = new ArrayList<Pair<String,String>>();
		String[] tokens = in.split(TOKEN_SEPERATOR);
		for (String token : tokens) {
			int posIdx = token.lastIndexOf(POS_SEPERATOR);
			String word = token.substring(0, posIdx);
			String pos = token.substring(posIdx+1);
			wordPosList.add(new Pair<String, String>(word,pos));
		}
		return wordPosList;
	}
}
