package kr.co.shineware.nlp.posta.en.core.lattice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kr.co.shineware.nlp.posta.constant.SYMBOL;
import kr.co.shineware.nlp.posta.en.core.lattice.model.LatticeNode;
import kr.co.shineware.nlp.posta.modeler.model.PosTable;
import kr.co.shineware.nlp.posta.modeler.model.Transition;

public class Lattice {
	//key = endIdx
	//value.key = prev lattice node's hashcode
	//value.value = prev lattice node that matched hashcode
	private Map<Integer,Map<Integer,LatticeNode>> lattice;
	private Transition transition = null;
	private PosTable table;
	
	public Lattice(PosTable table){
		this.table = table;
		this.init();
	}
	public void init() {


		this.lattice = null;
		this.lattice = new HashMap<Integer, Map<Integer,LatticeNode>>();
		Map<Integer,LatticeNode> initNodeMap = new HashMap<>();
		LatticeNode initNode = new LatticeNode();
		initNode.setMorph(SYMBOL.START);
		initNode.setPosId(this.table.getId(SYMBOL.START));
		initNode.setScore(0);
		initNodeMap.put(initNode.hashCode(), initNode);
		this.lattice.put(0, initNodeMap);

		//init
		initNode = null;
	}

	public void put(int beginIdx,int endIdx,String morph,int posId,double score){
		LatticeNode prevMaxNode = this.getPrevMaxNode(beginIdx,posId);
		LatticeNode curNode = new LatticeNode();
		curNode.setMorph(morph);
		curNode.setPosId(posId);
		curNode.setPrevHashcode(prevMaxNode.hashCode());
		curNode.setPrevIdx(beginIdx);
		curNode.setScore(prevMaxNode.getScore()+this.transition.get(prevMaxNode.getPosId(), posId)+score);
		this.insertLattice(endIdx,curNode);
	}

	private void insertLattice(int endIdx, LatticeNode curNode) {
		Map<Integer,LatticeNode> latticeNodeMap = this.lattice.get(endIdx);
		if(latticeNodeMap == null){
			latticeNodeMap = new HashMap<>();
		}
		
		latticeNodeMap.put(curNode.hashCode(), curNode);
		this.lattice.put(endIdx, latticeNodeMap);
	}

	private LatticeNode getPrevMaxNode(int beginIdx, int posId) {
		Map<Integer,LatticeNode> prevNodes = this.lattice.get(beginIdx);
		Set<Entry<Integer,LatticeNode>> prevNodeSet = prevNodes.entrySet();
		double maxScore = Double.NEGATIVE_INFINITY;
		LatticeNode maxNode = null;
		for (Entry<Integer, LatticeNode> prevNode : prevNodeSet) {
			int prevPosId = prevNode.getValue().getPosId();
			Double transitionScore = this.transition.get(prevPosId, posId);
			if(transitionScore == null){
				continue;
			}
			double score = prevNode.getValue().getScore()+transitionScore;
			if(score > maxScore){
				maxScore = score;
				maxNode = prevNode.getValue();
			}
		}
		return maxNode;
	}

	public void setTransition(Transition transition) {
		this.transition = transition;
	}
	public void print(int maxIdx) {
		for(int i=0;i<maxIdx;i++){
			Map<Integer,LatticeNode> nodeMap = this.lattice.get(i);
			Set<Entry<Integer,LatticeNode>> entrySet = nodeMap.entrySet();
			for (Entry<Integer, LatticeNode> entry : entrySet) {
				System.out.println("["+entry.getValue().getPrevIdx()+","+i+"]"+entry.getValue());
			}
			System.out.println();
		}
	}
	public void printMax(int length) {
		LatticeNode lastNode = this.getPrevMaxNode(length, table.getId(SYMBOL.END));
		List<String> result = new ArrayList<>();
		while(true){
			System.out.println(lastNode);
			if(lastNode.getPosId() == 0)break;
			String token = lastNode.getMorph()+"/"+table.getPos(lastNode.getPosId());
			result.add(token);
			lastNode = this.lattice.get(lastNode.getPrevIdx()).get(lastNode.getPrevHashcode());
		}
		for(int i=result.size()-1;i>=0;i--){
			System.out.println(result.get(i));
		}
	}
}
