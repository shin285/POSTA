package kr.co.shineware.nlp.posta.modeler.builder;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kr.co.shineware.nlp.posta.constant.FILENAME;
import kr.co.shineware.nlp.posta.constant.SYMBOL;
import kr.co.shineware.nlp.posta.corpus.model.Dictionary;
import kr.co.shineware.nlp.posta.corpus.model.Grammar;
import kr.co.shineware.nlp.posta.modeler.model.Observation;
import kr.co.shineware.nlp.posta.modeler.model.PosTable;
import kr.co.shineware.nlp.posta.modeler.model.Transition;
import kr.co.shineware.util.common.model.Pair;

public class ModelBuilder {
	private Dictionary wordDic;
	private Grammar grammar;
	private PosTable table;
	private Transition transition;
	private Observation observation;

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
		this.table.save(savePathName + File.separator + FILENAME.POS_TABLE);
		this.observation.save(savePathName + File.separator + FILENAME.OBSERVATION);		
		this.transition.save(savePathName + File.separator + FILENAME.TRANSITION);
		savePath = null;
	}
	public void load(String savePath){
		this.table = null;
		this.table = new PosTable();
		this.table.load(savePath + File.separator + FILENAME.POS_TABLE);

		this.observation = null;
		this.observation = new Observation(savePath + File.separator + FILENAME.OBSERVATION);

		this.transition = null;
		this.transition = new Transition();
		this.transition.load(savePath + File.separator + FILENAME.TRANSITION);
	}

	public void printSearchResult(String key){
		Map<String, List<Pair<Integer, Double>>> resultMap = this.observation.get(key+" ");
		if(resultMap == null){
			System.out.println(resultMap);
		}else{
			Set<Entry<String, List<Pair<Integer, Double>>>> entrySet = resultMap.entrySet();
			for (Entry<String, List<Pair<Integer, Double>>> entry : entrySet) {
				System.out.println(entry.getKey());
				System.out.println(entry.getValue());
				System.out.println();
			}
		}
	}

	public void buildPath(String path){
		this.wordDic = null;
		this.grammar = null;
		wordDic = new Dictionary(path+File.separator+FILENAME.WORD_DIC);
		grammar = new Grammar(path+File.separator+FILENAME.GRAMMAR);
		Map<String,Integer> totalPrevPOSTf = this.getTotalPrevPOSCount();

		//build POS table
		this.buildPosTable(totalPrevPOSTf);

		//build transition
		this.calTransition(totalPrevPOSTf);

		//build observation
		this.calObservation(totalPrevPOSTf);		
	}

	private Map<String, Integer> getTotalPrevPOSCount() {

		Map<String,Integer> posCountMap = new HashMap<String, Integer>();
		Set<String> prevPosSet = grammar.getGrammar().keySet();
		for (String prevPos : prevPosSet) {
			Map<String,Integer> prev2curPosMap = grammar.getGrammar().get(prevPos);
			Set<String> curPosSet = prev2curPosMap.keySet();
			for (String curPos : curPosSet) {
				Integer tf = posCountMap.get(prevPos);
				if(tf == null){
					tf = 0;
				}
				tf += prev2curPosMap.get(curPos);
				posCountMap.put(prevPos, tf);
			}
		}
		return posCountMap;

	}

	private void buildPosTable(Map<String, Integer> totalPrevPOSTf) {
		this.table = new PosTable();
		Set<String> posSet = totalPrevPOSTf.keySet();
		for (String pos : posSet) {
			this.table.put(pos);
		}
		this.table.put(SYMBOL.END);
	}

	private void calTransition(Map<String, Integer> totalPrevPOSTf) {

		this.transition  = new Transition(this.table.size());

		Set<String> prevPosSet = grammar.getGrammar().keySet();
		for (String prevPos : prevPosSet) {
			Map<String,Integer> curPosMap = grammar.getGrammar().get(prevPos);
			Set<String> curPosSet = curPosMap.keySet();
			for (String curPos : curPosSet) {
				int prev2CurTf = curPosMap.get(curPos);
				int prevTf = totalPrevPOSTf.get(prevPos);
				double transitionScore = prev2CurTf/(double)prevTf;
				transitionScore = Math.log10(transitionScore);
				this.transition.put(this.table.getId(prevPos),this.table.getId(curPos),transitionScore);
			}
		}
	}

	private void calObservation(Map<String, Integer> totalPrevPOSTf) {

		this.observation = new Observation();

		Set<Entry<String, Map<String,Integer>>> wordDicEntrySet = wordDic.getDictionary().entrySet();
		for (Entry<String, Map<String, Integer>> wordPosTfEntry : wordDicEntrySet) {
			String word = wordPosTfEntry.getKey();
			Set<Entry<String,Integer>> posTfSet = wordPosTfEntry.getValue().entrySet();
			for (Entry<String, Integer> posTf : posTfSet) {
				int totalPosTf = totalPrevPOSTf.get(posTf.getKey());
				double observationScore = (double)posTf.getValue()/totalPosTf;
				observationScore = Math.log10(observationScore);
				this.observation.put(word+" ",this.table.getId(posTf.getKey()),observationScore);
			}
		}
	}
}
