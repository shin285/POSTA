package kr.co.shineware.nlp.posta.modeler.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kr.co.shineware.nlp.posta.interfaces.FileAccessible;

public class PosTable implements FileAccessible{

	//key = pos
	//value = id
	private Map<String,Integer> posIdTable;

	//key = id
	//value = pos
	private Map<Integer,String> idPosTable;

	public PosTable(){
		this.init();
	}
	
	public Map<String, Integer> getPosIdTable(){
		return this.posIdTable;
	}
	
	public Map<Integer, String> getIdPosTable(){
		return this.idPosTable;
	}
	

	private void init() {
		this.posIdTable = null;
		this.idPosTable = null;
		this.posIdTable = new HashMap<String, Integer>();
		this.idPosTable = new HashMap<Integer,String>();
	}

	public void put(String pos) {
		Integer id = posIdTable.get(pos);
		if(id == null){
			posIdTable.put(pos, posIdTable.size());
			idPosTable.put(idPosTable.size(), pos);
		}
	}

	public int getId(String pos){
		return posIdTable.get(pos);
	}

	public String getPos(int id){
		return idPosTable.get(id);
	}

	public int size(){
		return posIdTable.size();
	}

	@Override
	public void save(String filename) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			Set<Entry<String,Integer>> posIdEntrySet = posIdTable.entrySet();
			for (Entry<String, Integer> entry : posIdEntrySet) {
				bw.write(entry.getKey()+"\t"+entry.getValue());
				bw.newLine();
			}
			bw.close();
			bw = null;
			posIdEntrySet = null;
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void load(String filename) {
		try{
			this.init();
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = null;
			while((line = br.readLine()) != null){
				String[] tokens = line.split("\t");
				this.posIdTable.put(tokens[0], Integer.parseInt(tokens[1]));
				this.idPosTable.put(Integer.parseInt(tokens[1]),tokens[0]);
			}
			br.close();
			br = null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}