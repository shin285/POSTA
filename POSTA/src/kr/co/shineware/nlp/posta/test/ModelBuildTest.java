package kr.co.shineware.nlp.posta.test;

import java.util.Scanner;

import kr.co.shineware.nlp.posta.modeler.builder.ModelBuilder;

public class ModelBuildTest {

	public static void main(String[] args) {
		ModelBuilder builder = new ModelBuilder();
//		builder.buildPath("corpus_build");
//		builder.save("model_build_improv");
//		builder.buildObservation();
		
		builder.load("model_build_improv");
		builder.buildObservation();
		System.out.println("load done");
		Scanner scanner = new Scanner(System.in);
		while(scanner.hasNext()){
			String in = scanner.next();
			if(in.equals("exit")){
				break;
			}
			builder.printSearchResult(in.trim());
		}
		scanner.close();
	}

}
