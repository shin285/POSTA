package kr.co.shineware.nlp.posta.corpus.builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import kr.co.shineware.nlp.posta.corpus.model.Dictionary;
import kr.co.shineware.nlp.posta.corpus.model.Grammar;
import kr.co.shineware.nlp.posta.corpus.parser.CorpusParser;
import kr.co.shineware.util.common.file.FileUtil;
import kr.co.shineware.util.common.model.Pair;

public class CorpusBuilder {
	
	private Dictionary dictionary;
	private Grammar grammar;
	private CorpusParser parser;
	
	public CorpusBuilder(){
		this.parser = new CorpusParser();
	}
	
	public void buildPath(String path){
		this.buildPath(path, null);
	}
	public void buildPath(String path,String suffix){
		File rootPath = new File(path);
		if(rootPath.exists() && rootPath.isDirectory()){
			List<String> filenames = FileUtil.getFileNames(path);
			for (String filename : filenames) {
				this.build(filename);
			}
		}else{
			;
		}
	}
	public void build(String filename){
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = null;
			while((line = br.readLine()) != null){
				line = line.trim();
				if(line.length() == 0){
					continue;
				}
				List<Pair<String,String>> wordPosList = this.parser.parse(line);
				this.buildDictionary(wordPosList);
				this.buildGrammar(wordPosList);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildDictionary(List<Pair<String, String>> wordPosList) {
		;
	}

	private void buildGrammar(List<Pair<String, String>> wordPosList) {
		// TODO Auto-generated method stub
		
	}
}