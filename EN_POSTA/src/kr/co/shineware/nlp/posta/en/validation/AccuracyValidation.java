package kr.co.shineware.nlp.posta.en.validation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import kr.co.shineware.nlp.posta.corpus.parser.CorpusParser;
import kr.co.shineware.nlp.posta.en.core.EnPosta;
import kr.co.shineware.util.common.model.Pair;


public class AccuracyValidation {

	public static void main(String[] args) throws Exception{
		EnPosta posta = new EnPosta();
//		posta.buildCorpus("validation\\train");
//		posta.saveCorpus("validation"+File.separator+"corpus_build");
//		posta.buildModel("validation"+File.separator+"corpus_build");
//		posta.saveModel("validation"+File.separator+"model_build");
//		
		posta.load("validation"+File.separator+"model_build");
		posta.buildFailLink();
		
		BufferedReader br = new BufferedReader(new FileReader("validation"+File.separator+"test"+File.separator+"0.en"));
		String line = null;
		int correct = 0,incorrect = 0;
		int rcorrect = 0, rincorrect = 0;
		CorpusParser parser = new CorpusParser();		
		while((line = br.readLine()) != null){
			line = line.replaceAll("[ ]+", " ").trim();
			if(line.length() == 0)continue;
			List<Pair<String,String>> parsedList = parser.parse(line);
			if(parsedList == null)continue;
			String problem = makeProblem(parsedList);
			List<String> resultList = posta.analyze(problem);
			String[] answers = line.split(" ");
			List<String> answerList = Arrays.asList(answers);
			for (String answer : answers) {
				if(resultList.contains(answer)){
					correct++;
				}else{					
					incorrect++;
				}
			}
			for (String result : resultList) {
				if(answerList.contains(result)){
					rcorrect++;
				}else{
					rincorrect++;
				}
			}
		}		
		System.out.println("correct : "+correct);
		System.out.println("incorrect : "+incorrect);
		System.out.println("acc. : "+(double)correct/(correct+incorrect));
		System.out.println("correct : "+rcorrect);
		System.out.println("incorrect : "+rincorrect);
		System.out.println("acc. : "+(double)rcorrect/(rcorrect+rincorrect));
		br.close();
		
	}

	private static String makeProblem(List<Pair<String, String>> parsedList) {
		StringBuffer sb = new StringBuffer();
		for (Pair<String, String> pair : parsedList) {
			sb.append(pair.getFirst());
			sb.append(" ");
		}
		return sb.toString().trim();
	}
}
