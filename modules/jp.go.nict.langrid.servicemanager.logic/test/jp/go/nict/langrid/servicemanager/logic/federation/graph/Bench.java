package jp.go.nict.langrid.servicemanager.logic.federation.graph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

import org.junit.BeforeClass;
import org.junit.Test;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.management.logic.federation.graph.BreathFirstSearch;
import jp.go.nict.langrid.management.logic.federation.graph.DijkstraSearch;
import jp.go.nict.langrid.management.logic.federation.graph.GraphSearch;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

public class Bench {
	public static final int nodeCount = 100;
	public static final int edgeCount = 20;
	public static final int edgeSkipWidth = 10;
	private static Map<String, Map<String, String>> graph;
	@SuppressWarnings("unchecked")
	private GraphSearch<String, String>[] algs = new GraphSearch[]{
//			new DepthFirstSearch<String, String>(),
			new BreathFirstSearch<String, String>(),
			new DijkstraSearch<String, String>()
	};

	@BeforeClass
	public static void setUp() throws Throwable{
		String name = "graph";
		File g = new File("data/" + name + ".json");
		File p = new File("data/" + name + ".png");
		if(!g.exists()){
			p.delete();
			Random r = new Random();
			g.getParentFile().mkdirs();
			Set<String> dup = new HashSet<>();
			Set<String[]> edges = new LinkedHashSet<>();
			for(int i = 0; i < nodeCount; i++){
				for(int j = 0; j < edgeCount; j++){
					int t = i + r.nextInt(edgeSkipWidth) - edgeSkipWidth / 2;
					t = Math.min(Math.max(t, 0), nodeCount - 1);
					if(i == t) continue;
					String[] f = {"node" + i, "node" + t};
					if(!dup.contains(StringUtil.join(f, ","))){
						edges.add(f);
						dup.add(StringUtil.join(f, ","));
					}
					String[] b = {"node" + t, "node" + i};
					if(r.nextBoolean() && !dup.contains(StringUtil.join(b, ","))){
						edges.add(b);
						dup.add(StringUtil.join(b, ","));
					}
				}
			}
			try(OutputStream os = new FileOutputStream(g)){
				JSON.encode(edges, os);
			}
		}

		Collection<String[]> edges = null;
		try(InputStream is = new FileInputStream(g)){
			edges = JSON.decode(is, new TypeReference<List<String[]>>(){}.getType());
		}

		if(!p.exists()){
			try(OutputStream os = new FileOutputStream(p)){
				Process pr = new ProcessBuilder("/usr/local/bin/dot", "-Tpng").start();
				try(OutputStream dot = pr.getOutputStream()){
					doGenerateGraph(edges, dot);
				}
				try(InputStream is = pr.getInputStream()){
					StreamUtil.transfer(is, os);
				} finally{
					pr.waitFor(1000, TimeUnit.MILLISECONDS);
				}
			}
		}

		graph = new LinkedHashMap<>();
		for(String[] edge : edges){
			graph.computeIfAbsent(edge[0], k -> new LinkedHashMap<>())
				.put(edge[1], edge[0] + ":" + edge[1]);
		}
	}

	static void doGenerateGraph(Collection<String[]> edges, OutputStream os)
	throws IOException, DaoException, InterruptedException{
		try(Writer w = new OutputStreamWriter(os, "UTF-8");
			PrintWriter pw = new PrintWriter(w)){
			pw.println("digraph g{");
			pw.println("\tgraph[layout=neato,overlap=false,outputorder=\"edgesfirst\"];");
			pw.println("\tnode[style=filled,fillcolor=white];");
			pw.println("\tedge[arrowhead=normalnormal];");
			for(String[] e : edges){
				pw.format("\t\"%s\"->\"%s\" [arrowhead=normal];%n", e[0], e[1]);
			}
			pw.println("}");
		}
	}

	@Test
	public void bench() throws Throwable{
		int warmup = 20, repeat = 50;
		MemoryMXBean mb = ManagementFactory.getMemoryMXBean();
		for(GraphSearch<String, String> alg : algs){
			mb.gc();
			measure(graph, alg, warmup);
			Pair<Double, Double> r = measure(graph, alg, repeat);
			System.out.printf(
					"%s: %.3f(%.3f), %d%n",
					alg.getClass().getSimpleName(), r.getFirst(), r.getSecond(),
					mb.getHeapMemoryUsage().getUsed());
		}
	}

	private <V> Pair<Double, Double> measure(Map<String, Map<String, V>> graph,
			GraphSearch<String, V> alg, int repeatCount){
		LongAdder la = new LongAdder();
		LongAdder lac = new LongAdder();
		LongAdder elapse = new LongAdder();
		LongAdder found = new LongAdder(), notFound = new LongAdder();
		int maxLen = Integer.MIN_VALUE;
		List<V> maxPath = null;
		for(int r = 0; r < repeatCount; r++){
			for(int i = 0; i < nodeCount; i++){
				for(int j = 0; j < nodeCount; j++){
					if(i == j) continue;
					long start = System.currentTimeMillis();
					List<V> path = alg.searchShortestPath(
							graph, "node" + i, "node" + j, Collections.emptySet()
							);
					int len = path.size();
					elapse.add(System.currentTimeMillis() - start);
					if(len > 0){
						la.add(len);
						lac.increment();
						found.increment();
					} else{
						notFound.increment();
					}
					if(maxLen < len){
						maxLen = len;
						maxPath = path;
					}
				}
			}
		}
		System.out.printf("found: %d, notFound: %d, maxLen: %d, maxPath: %s%n",
				found.longValue(), notFound.longValue(), maxLen, maxPath);
		double time = elapse.doubleValue() / repeatCount;
		double len = la.doubleValue() / lac.longValue();
		return Pair.create(time, len);
	}

	@Test
	public void test() throws Throwable{
		List<List<String>> p0 = findPaths(graph, algs[0]);
		List<List<String>> p1 = findPaths(graph, algs[1]);
		compare("p0 and p1", p0, p1);
	}

	private void compare(String name, List<List<String>> left, List<List<String>> right){
		System.out.println("compare " + name);
		int c = 0;
		for(int i = 0; i < nodeCount; i++){
			for(int j = 0; j < nodeCount; j++){
				if(i == j) continue;
				List<String> p0p = left.get(c);
				List<String> p1p = right.get(c);
				if(!p0p.equals(p1p)){
					System.out.println(c + ": " + p0p + " <> " + p1p);
				}
				c++;
			}
		}
		System.out.println("done.");
	}

	private <V> List<List<V>> findPaths(Map<String, Map<String, V>> graph, GraphSearch<String, V> alg){
		List<List<V>> ret = new ArrayList<>();
		for(int i = 0; i < nodeCount; i++){
			for(int j = 0; j < nodeCount; j++){
				if(i == j) continue;
				ret.add(alg.searchShortestPath(graph, "node" + i, "node" + j, Collections.emptySet()));
			}
		}
		return ret;
	}

	@Test
	public void comparePath() throws Throwable{
		String source = "node99";
		String target = "node0";
		for(GraphSearch<String, String> alg : algs){
			System.out.println(
					alg.getClass().getSimpleName() + ": " +
					alg.searchShortestPath(graph, source, target, Collections.emptySet()));
		}
	}

	// originally from JSONIC
	public static abstract class TypeReference<T> implements Type {
		public Type getType() {
			Type type = this.getClass().getGenericSuperclass();
			if (type instanceof ParameterizedType) {
				Type[] args = ((ParameterizedType)type).getActualTypeArguments();
				if (args != null && args.length == 1) {
					return args[0];
				}
			}
			throw new IllegalStateException("Reference must be specified actual type.");
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(getClass().getSimpleName());
			sb.append("[").append(getType()).append("]");
			return sb.toString();
	}
}

}
