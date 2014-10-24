package kr.co.shineware.nlp.posta.en;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kr.co.shineware.nlp.posta.constant.FILENAME;
import kr.co.shineware.nlp.posta.modeler.model.Observation;
import kr.co.shineware.nlp.posta.modeler.model.PosTable;
import kr.co.shineware.nlp.posta.modeler.model.Transition;
import kr.co.shineware.util.common.model.Pair;

public class EnPosta {
	private Observation observation;
	private Transition transition;
	private PosTable posTable;
	public EnPosta(String modelPath) {
		this.init();
		this.load(modelPath);
		this.observation.put(" This is ", 29, 0.0);
		this.observation.buildFailLink();
	}
	private void init() {
		this.observation = null;
		this.transition = null;
		this.posTable = null;
		
		this.observation = new Observation();
		this.transition = new Transition();
		this.posTable = new PosTable();
	}
	private void load(String modelPath){
		posTable.load(modelPath+File.separator+FILENAME.POS_TABLE);
		observation.load(modelPath+File.separator+FILENAME.OBSERVATION);
		transition.load(modelPath+File.separator+FILENAME.TRANSITION);
	}
	
	public void analyze(String in){
		String[] words = in.split(" ");
		for(int i=0;i<words.length;i++){
			String word = words[i].trim();
			if(i == 0){
				word = " " +word;
			}
			System.out.println("Word : "+word);
			Map<String,List<Pair<Integer,Double>>> result = observation.get(word+" ");
			this.printResult(result,i);
		}
	}
	private void printResult(Map<String, List<Pair<Integer, Double>>> result, int i) {
		Set<Entry<String,List<Pair<Integer,Double>>>> entrySet = result.entrySet();
		for (Entry<String, List<Pair<Integer, Double>>> entry : entrySet) {
			List<Pair<Integer, Double>> posIdScoreList = entry.getValue();
			String word = entry.getKey();
			int rewindIdx = word.trim().split(" ").length-1;
			for (Pair<Integer, Double> pair : posIdScoreList) {
				System.out.println("["+(i-rewindIdx)+","+i+"] : "+word.trim()+"/"+this.posTable.getPos(pair.getFirst()));
			}
		}
	}
}
