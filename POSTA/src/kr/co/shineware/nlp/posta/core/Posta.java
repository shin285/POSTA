package kr.co.shineware.nlp.posta.core;

import kr.co.shineware.nlp.posta.corpus.builder.CorpusBuilder;
import kr.co.shineware.nlp.posta.corpus.parser.CorpusParser;
import kr.co.shineware.nlp.posta.modeler.builder.ModelBuilder;
import kr.co.shineware.nlp.posta.modeler.parser.LanguageParser;

public class Posta {
	private ModelBuilder modelBuilder;
	private CorpusBuilder corpusBuilder;
	public Posta(){
		this.init();
	}
	private void init() {
		this.modelBuilder = null;
		this.corpusBuilder = null;
		this.modelBuilder = new ModelBuilder();
		this.corpusBuilder = new CorpusBuilder();
	}
	protected void setCorpusParser(CorpusParser corpusParser) {
		this.corpusBuilder.setCorpusParser(corpusParser);
	}
	protected void setLanguageParser(LanguageParser languageParser) {
		this.modelBuilder.setLanguageParser(languageParser);
	}	
	
	public void buildModel(String path){
		modelBuilder.buildPath(path);
	}
	public void saveModel(String path){
		modelBuilder.save(path);
	}
	public void buildCorpus(String path){
		corpusBuilder.buildPath(path);
	}
	public void saveCorpus(String path){
		corpusBuilder.save(path);
	}
}
