package kr.co.shineware.nlp.posta.en.core.lattice;

import java.util.HashMap;
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


	public Lattice(){
		this.init();
	}
	
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
}
