package kr.co.shineware.nlp.posta.modeler.model;

import java.util.ArrayList;
import java.util.List;

import kr.co.shineware.ds.aho_corasick.AhoCorasickDictionary;
import kr.co.shineware.nlp.posta.interfaces.FileAccessible;
import kr.co.shineware.util.common.model.Pair;

public class Observation implements FileAccessible{

	private AhoCorasickDictionary<List<Pair<Integer,Double>>> observation;
	
	public Observation(){
		;
	}
	public Observation(String filename){
		this.load(filename);
	}
	@Override
	public void save(String filename) {
		observation.save(filename);
	}

	@Override
	public void load(String filename) {
		observation = null;
		observation.load(filename);
	}
	public void put(String word, int id, double observationScore) {
		List<Pair<Integer,Double>> posIdScorePairList = observation.getValue(word);
		if(posIdScorePairList == null){
			posIdScorePairList = new ArrayList<>();
			posIdScorePairList.add(new Pair<Integer, Double>(id, observationScore));
		}else{
			int i=0;
			for(i=0;i<posIdScorePairList.size();i++){
				if(posIdScorePairList.get(i).getFirst() == id){
					break;
				}
			}
			if(posIdScorePairList.size() == i){
				posIdScorePairList.add(new Pair<Integer, Double>(id, observationScore));
			}
		}
		this.observation.put(word, posIdScorePairList);
	}


}
