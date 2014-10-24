package kr.co.shineware.nlp.posta.test;

import java.util.Scanner;

import kr.co.shineware.nlp.posta.modeler.builder.ModelBuilder;

public class ModelBuildTest {

	public static void main(String[] args) {
		ModelBuilder builder = new ModelBuilder();
		builder.buildPath("corpus_build");
		builder.save("model_build");
//		builder.buildObservation();
		
		builder.load("model_build");
		builder.buildObservation();
		System.out.println("load done");
		Scanner scanner = new Scanner(System.in);
		while(scanner.hasNext()){
			String in = scanner.nextLine();
			if(in.equals("exit")){
				break;
			}
			System.out.println("input : "+in);
			builder.printSearchResult(in.trim());
		}
		scanner.close();
	}

}
