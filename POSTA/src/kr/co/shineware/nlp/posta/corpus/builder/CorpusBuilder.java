package kr.co.shineware.nlp.posta.corpus.builder;

import java.io.File;

import kr.co.shineware.nlp.posta.corpus.model.Dictionary;
import kr.co.shineware.nlp.posta.corpus.model.Grammar;

public class CorpusBuilder {
	
	private Dictionary dictionary;
	private Grammar grammar;
	
	public CorpusBuilder(){
		;
	}
	
	public void buildPath(String path){
		this.buildPath(path, null);
	}
	public void buildPath(String path,String suffix){
		File rootPath = new File(path);
		if(rootPath.exists()){
			;
		}else{
			;
		}
	}
	public void build(String filename){
		;
	}
}