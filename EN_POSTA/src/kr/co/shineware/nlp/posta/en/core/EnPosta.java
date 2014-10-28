package kr.co.shineware.nlp.posta.en.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kr.co.shineware.nlp.posta.constant.FILENAME;
import kr.co.shineware.nlp.posta.core.Posta;
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
	public EnPosta(){
		super();
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
	
	public void analyze(String in){
		lattice = null;
		lattice = new Lattice();
		lattice.setTransition(this.transition);
		String[] words = in.replaceAll("[ ]+", " ").split(" ");
		for(int i=0;i<words.length;i++){
			String word = words[i].trim();
			Map<String,List<Pair<Integer,Double>>> result = this.observation.get(word+" ");
			if(result == null){
				continue;
			}
			this.insertLattice(result,i);
			this.printResult(result,i);
		}
	}
	private void insertLattice(Map<String, List<Pair<Integer, Double>>> result, int i) {
		Set<Entry<String,List<Pair<Integer,Double>>>> entrySet = result.entrySet();
		for (Entry<String, List<Pair<Integer, Double>>> entry : entrySet) {
			List<Pair<Integer, Double>> posIdScoreList = entry.getValue();
			String word = this.languageParser.unparsing(entry.getKey());
			int rewindIdx = word.trim().split(" ").length-1;
			int beginIdx = i-rewindIdx;
			int endIdx = i+1;
			for (Pair<Integer, Double> pair : posIdScoreList) {
				this.lattice.put(beginIdx,endIdx,word,pair.getFirst(),pair.getSecond());
			}
		}
	}
	private void printResult(Map<String, List<Pair<Integer, Double>>> result, int i) {
		Set<Entry<String,List<Pair<Integer,Double>>>> entrySet = result.entrySet();
		for (Entry<String, List<Pair<Integer, Double>>> entry : entrySet) {
			List<Pair<Integer, Double>> posIdScoreList = entry.getValue();
			String word = entry.getKey();
			int rewindIdx = word.trim().split(" ").length-1;
			for (Pair<Integer, Double> pair : posIdScoreList) {
				System.out.println("["+(i-rewindIdx)+","+i+"] : "+this.languageParser.unparsing(word)+"/"+this.posTable.getPos(pair.getFirst()));
			}
		}
	}
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
