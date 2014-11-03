package kr.co.shineware.nlp.posta.en.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kr.co.shineware.nlp.posta.constant.FILENAME;
import kr.co.shineware.nlp.posta.core.Posta;
import kr.co.shineware.nlp.posta.en.constant.SCORE;
import kr.co.shineware.nlp.posta.en.core.lattice.Lattice;
import kr.co.shineware.nlp.posta.en.parser.EnCorpusParser;
import kr.co.shineware.nlp.posta.en.parser.EnLanguageParser;
import kr.co.shineware.nlp.posta.modeler.model.Observation;
import kr.co.shineware.nlp.posta.modeler.model.PosTable;
import kr.co.shineware.nlp.posta.modeler.model.Transition;
import kr.co.shineware.util.common.model.Pair;

public class EnPosta extends Posta{	
	private Observation observation;
	private Transition transition;
	private PosTable posTable;
	private EnLanguageParser languageParser;
	private Lattice lattice;
	public int totalTokens = 0;
	public EnPosta(){
		super();
		this.init();
	}
	public EnPosta(String modelPath) {
		super();
		this.load(modelPath);
	}
	private void init() {
		languageParser = new EnLanguageParser();
		this.observation = null;
		this.transition = null;
		this.posTable = null;

		this.observation = new Observation();
		this.transition = new Transition();
		this.posTable = new PosTable();

		this.setCorpusParser(new EnCorpusParser());
		this.setLanguageParser(languageParser);
	}
	public void load(String modelPath){
		this.init();
		this.posTable.load(modelPath+File.separator+FILENAME.POS_TABLE);
		this.observation.load(modelPath+File.separator+FILENAME.OBSERVATION);
		this.transition.load(modelPath+File.separator+FILENAME.TRANSITION);
	}

	public void buildFailLink(){
		this.observation.buildFailLink();
	}

	public List<String> analyze(String in){
		lattice = null;
		lattice = new Lattice(this.posTable);
		lattice.setTransition(this.transition);
		String[] words = in.trim().replaceAll("[ ]+", " ").split(" ");
		int latticeIdx = 0;
		for(int i=0;i<words.length;i++){
			String word = words[i].trim();
			if(i == 0){
				word = " "+word;
			}
			System.out.println(word);
			Map<String,List<Pair<Integer,Double>>> result = this.observation.get(word+" ");
			if(result == null){				
				String[] splitedWords = this.splitPunctuataion(word+" ");
				if(splitedWords != null){
					for (String splitedWord : splitedWords) {
						result = this.observation.get(splitedWord+" ");
						if(result == null){
							result = this.makeOOVResult(word+" ");
						}
						boolean isInserted = this.insertLattice(result,latticeIdx);
						if(!isInserted){
							result = this.makeOOVResult(word+" ");
							this.insertLattice(result,latticeIdx);
						}
						latticeIdx++;
						this.lattice.print(latticeIdx);
					}
					continue;
				}
			}
			if(result == null){
				result = this.makeOOVResult(word+" ");
			}
			//			System.out.println(result);
			boolean isInserted = this.insertLattice(result,latticeIdx);
			if(!isInserted){
				result = this.makeOOVResult(word+" ");
				this.insertLattice(result,latticeIdx);
			}
//			this.lattice.print(latticeIdx);
			latticeIdx++;			
			this.lattice.print(latticeIdx);
		}
		totalTokens += latticeIdx;
//		this.lattice.printMax(latticeIdx);
		return this.lattice.getMax(latticeIdx);
	}

	private String[] splitPunctuataion(String word) {
		String splitedTokens = word.replaceAll("('ve|'d|'ll|'m|'s|'re)", " $1").replaceAll("([^0-9a-zA-Z-('ve|'d|'ll|'m|'s|'re)])", " $1 ").replaceAll("[ ]+", " ").trim();
		if(!word.replaceAll("[ ]+", " ").trim().equals(splitedTokens)){
			splitedTokens = splitedTokens.replaceAll(" \\. ", "\\.");
			return splitedTokens.split(" ");
		}
		return null;
	}
	private Map<String, List<Pair<Integer, Double>>> makeOOVResult(String word) {
		Map<String, List<Pair<Integer, Double>>> result = new HashMap<String, List<Pair<Integer,Double>>>();
		List<Pair<Integer, Double>> posIdScoreList = new ArrayList<Pair<Integer,Double>>();
		Set<Integer> posIdSet = this.posTable.getIdPosTable().keySet();
		for (Integer posId : posIdSet) {
			posIdScoreList.add(new Pair<Integer, Double>(posId, SCORE.OOV));
		}
		result.put(word, posIdScoreList);
		return result;
	}
	private boolean insertLattice(Map<String, List<Pair<Integer, Double>>> result, int i) {
		Set<Entry<String,List<Pair<Integer,Double>>>> entrySet = result.entrySet();
		boolean inserted = false;
		for (Entry<String, List<Pair<Integer, Double>>> entry : entrySet) {
			List<Pair<Integer, Double>> posIdScoreList = entry.getValue();
			String word = this.languageParser.unparsing(entry.getKey());
			int rewindIdx = word.trim().split(" ").length-1;
			int beginIdx = i-rewindIdx;
			int endIdx = i+1;
			for (Pair<Integer, Double> pair : posIdScoreList) {
				boolean isInsert = this.lattice.put(beginIdx,endIdx,word,pair.getFirst(),pair.getSecond());
				if(isInsert){
					inserted = true;
				}
			}
		}
		return inserted;
	}
//	private void printResult(Map<String, List<Pair<Integer, Double>>> result, int i) {
//		Set<Entry<String,List<Pair<Integer,Double>>>> entrySet = result.entrySet();
//		for (Entry<String, List<Pair<Integer, Double>>> entry : entrySet) {
//			List<Pair<Integer, Double>> posIdScoreList = entry.getValue();
//			String word = entry.getKey();
//			int rewindIdx = word.trim().split(" ").length-1;
//			for (Pair<Integer, Double> pair : posIdScoreList) {
//				System.out.println("["+(i-rewindIdx)+","+i+"] : "+this.languageParser.unparsing(word)+"/"+this.posTable.getPos(pair.getFirst()));
//			}
//		}
//	}
	public void appendUserDic(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = null;
			while((line = br.readLine()) != null){
				line = line.trim();
				if(line.length() == 0 || line.charAt(0) == '#')continue;
				String[] tmp = line.split("\t");
				String word = tmp[0];
				String pos = tmp[1];
				this.observation.put(this.languageParser.parsing(word), this.posTable.getId(pos), 0.0);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
