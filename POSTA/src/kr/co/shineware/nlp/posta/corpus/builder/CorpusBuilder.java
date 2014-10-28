package kr.co.shineware.nlp.posta.corpus.builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import kr.co.shineware.nlp.posta.constant.FILENAME;
import kr.co.shineware.nlp.posta.constant.SYMBOL;
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
		this.dictionary = new Dictionary();
		this.grammar = new Grammar();
	}
	
	public void buildPath(String path){
		this.buildPath(path, null);
	}
	public void buildPath(String path,String suffix){
		File rootPath = new File(path);
		if(rootPath.exists() && rootPath.isDirectory()){
			List<String> filenames = FileUtil.getFileNames(path);
			for (String filename : filenames) {
				System.out.println(filename);
				if(suffix != null){
					if(filename.endsWith(suffix)){
						this.build(filename);
					}
				}else{
					this.build(filename);
				}
				
			}
		}else{
			System.err.println("Corpus Build Path Error");
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
				if(wordPosList == null){
					continue;
				}
				this.buildDictionary(wordPosList);
				this.buildGrammar(wordPosList);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void save(String savePathName){
		File savePath = new File(savePathName);
		if (savePath.exists() && !savePath.isDirectory()) {
			System.err.println("CorpusBuilder.save error!");
			System.err
			.println("savePathName is exists, but it's not a directory.");
			System.err.println("please check path name to save");
			System.exit(1);
		}
		savePath.mkdirs();
		this.dictionary.save(savePathName + File.separator + FILENAME.WORD_DIC);		
		this.grammar.save(savePathName + File.separator + FILENAME.GRAMMAR);
		savePath = null;
	}

	private void buildDictionary(List<Pair<String, String>> wordPosList) {
		for (Pair<String, String> wordPos : wordPosList) {
			this.dictionary.append(wordPos);
		}
	}

	private void buildGrammar(List<Pair<String, String>> wordPosList) {
		String prevPos = SYMBOL.START;
		for(int i=0;i<wordPosList.size();i++){
			String pos = wordPosList.get(i).getSecond();
			this.grammar.append(prevPos, pos);
			prevPos = pos;
		}
		this.grammar.append(prevPos, SYMBOL.END);
	}

	public void setCorpusParser(CorpusParser corpusParser) {
		this.parser = corpusParser;		
	}
}