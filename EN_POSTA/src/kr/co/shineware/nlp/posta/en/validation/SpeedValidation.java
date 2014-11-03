package kr.co.shineware.nlp.posta.en.validation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import kr.co.shineware.nlp.posta.corpus.parser.CorpusParser;
import kr.co.shineware.nlp.posta.en.core.EnPosta;
import kr.co.shineware.util.common.model.Pair;

public class SpeedValidation {
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
		long elapsedTime = 0;
		long begin,end;
		CorpusParser parser = new CorpusParser();
		int lineCount = 0;
		while((line = br.readLine()) != null){
			line = line.replaceAll("[ ]+", " ").trim();
			if(line.length() == 0)continue;
			List<Pair<String,String>> parsedList = parser.parse(line);
			if(parsedList == null)continue;
			String problem = makeProblem(parsedList);
			begin = System.currentTimeMillis();
			posta.analyze(problem);
			end = System.currentTimeMillis();
			elapsedTime += (end-begin);
			lineCount++;
		}
		System.out.println("ElapsedTime : "+elapsedTime/1000.0+" sec");
		System.out.println("line : "+lineCount / (elapsedTime/1000.0) + " line/sec");
		System.out.println("tokens : "+posta.totalTokens / (elapsedTime/1000.0) + " token/sec");
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
