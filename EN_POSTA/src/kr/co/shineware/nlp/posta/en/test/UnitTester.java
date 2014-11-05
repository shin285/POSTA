package kr.co.shineware.nlp.posta.en.test;

import java.util.List;
import java.util.Scanner;

import kr.co.shineware.nlp.posta.en.core.EnPosta;

public class UnitTester {

	public static void main(String[] args) {
		EnPosta posta = new EnPosta();

		posta.load("model_build");
		posta.appendUserDic("dic.user");
		posta.buildFailLink();
		
		Scanner sc = new Scanner(System.in);
		String line = null;
		while(true){
			System.out.print("Input : ");
			line = sc.nextLine();
			if(line.trim().equals("exit"))break;
			List<String> resultList = posta.analyze(line.trim());
			for (String result : resultList) {
				System.out.println(result);
			}
			System.out.println();
		}
		sc.close();
	}

}
