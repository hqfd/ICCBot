package main.java.analyze.model.analyzeModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.java.analyze.utils.output.PrintUtils;

public class SinglePathModel {
	private SingleMethodModel singleMethod;
	private List<UnitNode> nodes;
	private List<String> methodTraceUnrepeated;
	private Set<SingleObjectModel> singleObjectSet;
	private Map<Integer, List<String>> node2TraceMap;

	public SinglePathModel() {
		this(null);
	}

	public SinglePathModel(SingleMethodModel singleMethod) {
		setSingleMethod(singleMethod);
		setNodes(new ArrayList<UnitNode>());
		setMethodTrace(new LinkedList<String>());
		setSingleObjectSet(new HashSet<SingleObjectModel>());
		setNode2TraceMap(new HashMap<Integer, List<String>>());
	}

	public List<String> getMethodTrace() {
		return methodTraceUnrepeated;
	}

	public void setMethodTrace(List<String> methodTrace) {
		this.methodTraceUnrepeated = methodTrace;
	}

	public SingleMethodModel getSingleMethod() {
		return singleMethod;
	}

	public void setSingleMethod(SingleMethodModel singleMethod) {
		this.singleMethod = singleMethod;
	}

	public List<UnitNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<UnitNode> nodes) {
		this.nodes = nodes;
	}

	public void addNode(UnitNode node) {
		this.nodes.add(node);
	}

	public void copy(SinglePathModel temp) {
		setSingleMethod(temp.getSingleMethod());
		setMethodTrace(new LinkedList<String>(temp.getMethodTrace()));
		setNode2TraceMap(new HashMap<Integer, List<String>>(temp.getNode2TraceMap()));

		// for(SingleObjectModel model :temp.getSingleObjectSet())
		// getSingleObjectSet().add(model);

		for (UnitNode n : temp.getNodes())
			addNode(n);

	}

	public void merge(SinglePathModel temp, String curentContextSig) {
		if (this.hashCode() == temp.hashCode())
			return;
		for (String me : temp.getMethodTrace()) {
			if (!getMethodTrace().contains(me))
				getMethodTrace().add(me);
		}
		for (int i : temp.getNode2TraceMap().keySet()) {
			List<String> oldContext = temp.getNode2TraceMap().get(i);
			List<String> newContext = new ArrayList<String>();
			newContext.add(curentContextSig);
			newContext.addAll(oldContext);
			getNode2TraceMap().put(getNode2TraceMap().size(), newContext);
		}

		for (UnitNode n : temp.getNodes())
			addNode(n);
	}

	public StringBuilder appendList2SB(StringBuilder sb, List<?> list, String tag) {
		if (list.size() > 0)
			sb.append(tag + ":\n" + PrintUtils.printList(list, "\n") + "\n");
		return sb;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		appendList2SB(sb, getNodes(), "nodes");
		return sb.toString();
	}

	public Map<Integer, List<String>> getNode2TraceMap() {
		return node2TraceMap;
	}

	public void setNode2TraceMap(HashMap<Integer, List<String>> hashMap) {
		this.node2TraceMap = hashMap;
	}

	/**
	 * @return the singleObjectSet
	 */
	public Set<SingleObjectModel> getSingleObjectSet() {
		return singleObjectSet;
	}

	/**
	 * @param singleObjectSet
	 *            the singleObjectSet to set
	 */
	public void setSingleObjectSet(Set<SingleObjectModel> singleObjectSet) {
		this.singleObjectSet = singleObjectSet;
	}
}
