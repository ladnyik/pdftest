package com.ladnyik.neo4j;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class TestNeo4j {
	
	public static void main(String[] args) {
		
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(args[0]));
		
		Result result;
		
		Transaction tx = graphDb.beginTx();
		
		result = graphDb.execute("MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r");		
		System.out.println(result);
		tx.success();
		tx.close();
		
		Duration timeElapsed;		
		Instant endInstant, startInstant = Instant.now();

		result = graphDb.execute(
		        "USING PERIODIC COMMIT 1000 LOAD CSV WITH HEADERS FROM \"file:////home/lyz/neo4jforrm/import/nodes.csv\" AS line  CREATE (:AldonObject { id: line.ID, app: line.APP, rel: line.REL, name: line.NAME, family: line.FAMILY, env: line.ENV, developer: line.DEVELOPER,type: line.TYPE, attribute: line.ATTRIBUTE, extattr: line.EXTATTR, description: line.DESC, envNum: toInt(line.RANK), retired : line.RETIRED, version : line.VERSION, sourceFile : line.SOURCEFILE, sourceMember : line.SOURCEMEMBER,"
		        + " integrationObjectLibrary: line.ITGOBJLIBN, integrationSourceLibrary: line.ITGSRCLIBN, quaObjectLibrary: line.QUAOBJLIBN, quaSourceLibrary: line.QUASRCLIBN, productionObjectLibrary: line.DYNOBJLIBN, productionSourceLibrary: line.DYNSRCLIBN, sourceLibrary: line.SOURCELIBRARY  }) RETURN count(*)");
		while (result.hasNext()) {
			System.out.println(result.next());
		}
		endInstant = Instant.now();
		timeElapsed = Duration.between(startInstant, endInstant);

		System.out.println(String.format("Time taken to load nodes: %d,%d", timeElapsed.getSeconds(), timeElapsed.getNano()/1000000));
		
		
		startInstant = Instant.now();
		result = graphDb.execute(
				"USING PERIODIC COMMIT 500 LOAD CSV WITH HEADERS FROM \"file:////home/lyz/neo4jforrm/import/task.csv\" AS line  CREATE (:Task { task: line.TASK, desc: line.DESC}) RETURN count(*)");
		while (result.hasNext()) {
			System.out.println(result.next());
		}
		
		endInstant = Instant.now();
		timeElapsed = Duration.between(startInstant, endInstant);

		System.out.println(String.format("Time taken to load tasks: %d,%d", timeElapsed.getSeconds(), timeElapsed.getNano()/1000000));		
		
		startInstant = Instant.now();
		result = graphDb.execute(
				"USING PERIODIC COMMIT 500 LOAD CSV WITH HEADERS FROM \"file:////home/lyz/neo4jforrm/import/rel.csv\" AS line MATCH (o1:AldonObject { id: line.FROM}) MATCH (o2:AldonObject { id: line.TO}) "
				+ "CREATE (o1)-[:REQUISITE { type: line.TYPE, fromEnv : toInt(line.FROMENV), toEnv : toInt(line.TOENV) }]->(o2)RETURN count(*);");
		while (result.hasNext()) {
			System.out.println(result.next());
		}
		endInstant = Instant.now();
		timeElapsed = Duration.between(startInstant, endInstant);
		System.out.println(String.format("Time taken to load rels: %d,%d", timeElapsed.getSeconds(), timeElapsed.getNano()/1000000));
		
		result = graphDb.execute("MATCH (m:AldonObject)return m ORDER by m.name, m.family , m.envNum DESC");
		while (result.hasNext()) {
			Map object = result.next();
			
			for (Object key : object.keySet()) {
				Node node = (Node) object.get(key);
				System.out.println(node.getAllProperties());
			}
		}
				
		graphDb.shutdown();

	}
}
