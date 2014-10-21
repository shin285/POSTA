package kr.co.shineware.nlp.posta.corpus.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kr.co.shineware.nlp.posta.interfaces.FileAccessible;
import kr.co.shineware.util.common.file.FileUtil;
import kr.co.shineware.util.common.string.StringUtil;

public class Grammar implements FileAccessible{

	private Map<String,Map<String,Integer>> grammar;
	
	public Grammar() {
		this.grammar = new HashMap<String, Map<String,Integer>>();
	}
	public Grammar(String filename){
		this.load(filename);
	}
	
	@Override
	public void save(String filename) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			Set<Entry<String, Map<String,Integer>>> entrySet = grammar.entrySet();
			for (Entry<String, Map<String, Integer>> entry : entrySet) {

				bw.write(entry.getKey());
				bw.write("\t");

				Set<String> nextMorphSet = entry.getValue().keySet();
				int morphSize = nextMorphSet.size();
				int count = 0;
				for (String nextMorph : nextMorphSet) {
					bw.write(nextMorph);
					bw.write(":");
					Integer tf = entry.getValue().get(nextMorph);
					bw.write(""+tf);
					count++;
					if(morphSize != count){
						bw.write(",");
					}
				}
				bw.newLine();				
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void load(String filename) {
		this.grammar = null;
		this.grammar = new HashMap<String, Map<String,Integer>>();
		List<String> lines = FileUtil.load2List(filename);
		int size = lines.size();
		for(int i=0;i<size;i++){
			String line = lines.get(i);

			List<String> lineSplitedList = StringUtil.split(line, "\t");

			//previous POS
			String prevPos = lineSplitedList.get(0);

			//next POS parsing
			Map<String, Integer> nextPosMap = new HashMap<String,Integer>();
			String nextPosChunks = lineSplitedList.get(1);
			List<String> nextPosChunkList = StringUtil.split(nextPosChunks, ",");
			int posChunkListSize = nextPosChunkList.size();
			String commaPos = "";
			for(int j=0;j<posChunkListSize;j++){
				String nextPosTfPair = nextPosChunkList.get(j);
				if(nextPosTfPair.length() == 0){
					commaPos += ",";
					continue;
				}
				int separatorIdx = nextPosTfPair.lastIndexOf(':');
//				String nextPos = nextPosTfPair.split(":")[0];
//				Integer tf = Integer.parseInt(nextPosTfPair.split(":")[1]);
				String nextPos;
				if(separatorIdx == 0){
					nextPos = "";
				}else{
					nextPos = nextPosTfPair.substring(0, separatorIdx);
				}
				Integer tf = Integer.parseInt(nextPosTfPair.substring(separatorIdx+1));
				nextPosMap.put(commaPos+nextPos,tf);
				commaPos = "";
			}
			
			//load to memory
			this.grammar.put(prevPos, nextPosMap);
		}
	}
	
	public void append(String prevPos,String curPos){
		Map<String,Integer> nextPosTfMap = this.grammar.get(prevPos);
		if(nextPosTfMap == null){
			nextPosTfMap = new HashMap<String, Integer>();
		}
		Integer tf = nextPosTfMap.get(curPos);
		if(tf == null){
			tf = 0;
		}
		tf++;
		nextPosTfMap.put(curPos, tf);
		this.grammar.put(prevPos, nextPosTfMap);
	}

}
