package kr.co.shineware.nlp.posta.modeler.builder;

import java.io.File;

import kr.co.shineware.nlp.posta.constant.FILENAME;
import kr.co.shineware.nlp.posta.corpus.model.Dictionary;
import kr.co.shineware.nlp.posta.corpus.model.Grammar;

public class ModelBuilder {
	private Dictionary wordDic;
	private Grammar grammar;
	
	public void buildPath(String path){
		this.wordDic = null;
		this.grammar = null;

		wordDic = new Dictionary(path+File.separator+FILENAME.WORD_DIC);
		grammar = new Grammar(path+File.separator+FILENAME.GRAMMAR);
	}
}
