package kr.co.shineware.nlp.posta.modeler.model;

import kr.co.shineware.nlp.posta.interfaces.FileAccessible;

public class Observation implements FileAccessible{

	public Observation(){
		;
	}
	public Observation(String filename){
		this.load(filename);
	}
	@Override
	public void save(String filename) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void load(String filename) {
		// TODO Auto-generated method stub
		
	}


}
