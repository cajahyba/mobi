package mobi.extension.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import mobi.core.cardinality.Cardinality;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;
import mobi.core.relation.CompositionRelation;
import mobi.core.relation.EquivalenceRelation;
import mobi.core.relation.InheritanceRelation;
import mobi.core.relation.InstanceRelation;
import mobi.core.relation.SymmetricRelation;

public class RelationDAO {

	private Connection connection;

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public void saveRelation(Relation relation) throws SQLException {
		
		if(relation.getId() == null) {
			this.saveNewRelation(relation);
		} else {
			this.updateRelation(relation);
			this.removeAllCardinalitiesFromRelation(relation);
			this.removeAllInstanceRelationsFromRelation(relation);
		}
		this.saveRelationCardinalities(relation);
		this.saveRelationInstaceRelations(relation);
		
	}
	
	private void updateRelation(Relation relation) throws SQLException {
		String nameA = null;
		String nameB = null;
		
		PreparedStatement stmt = this.connection.prepareStatement(
				" update relation " +
				"    set uri        = ?, " +
				"        context_id = ?, " +
				"        nameA      = ?, " +
				"        nameB      = ?, " +
				"        type       = ?, " +
				"        comment    = ?, " +
				"        valid      = ? " +
				"  where id         = ? ");
		
		switch(relation.getType()){
		
			case Relation.BIDIRECIONAL_COMPOSITION:
			case Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO:
				nameA = ((CompositionRelation) relation).getNameA();
				nameB = ((CompositionRelation) relation).getNameB();
				break;
			case Relation.UNIDIRECIONAL_COMPOSITION:
				nameA = ((CompositionRelation) relation).getNameA();
				break;
			case Relation.SYMMETRIC_COMPOSITION:
				nameA = ((SymmetricRelation)relation).getName();
				break;
			default:
				break;
		}
		
		stmt.setString (1, relation.getUri());
		stmt.setLong   (2, relation.getContext().getId());
		stmt.setString (3, nameA);
		stmt.setString (4, nameB);
		stmt.setInt    (5, relation.getType());
		stmt.setString (6, relation.getComment());
		stmt.setBoolean(7, relation.getValid());
		stmt.setLong   (8, relation.getId());

		stmt.executeUpdate();
	}
	
	private void saveNewRelation(Relation relation) throws SQLException {
		String nameA = null;
		String nameB = null;
		
		PreparedStatement stmt = this.connection.prepareStatement(
				" insert into relation (" +
				                      " uri, " +
				                      " context_id, " +
				                      " nameA, " +
				                      " nameB, " +
				                      " type, " +
				                      " comment, " +
				                      " valid " +
				                      " ) " +
					         " values (?, ?, ?, ?, ?, ?, ?) ", Statement.RETURN_GENERATED_KEYS);
		
		switch(relation.getType()){
		
			case Relation.BIDIRECIONAL_COMPOSITION:
			case Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO:
				nameA = ((CompositionRelation) relation).getNameA();
				nameB = ((CompositionRelation) relation).getNameB();
				break;
			case Relation.UNIDIRECIONAL_COMPOSITION:
				nameA = ((CompositionRelation) relation).getNameA();
				break;
			case Relation.SYMMETRIC_COMPOSITION:
				nameA = ((SymmetricRelation)relation).getName();
				break;
			default:
				break;
		}
		
		stmt.setString (1, relation.getUri());
		stmt.setLong   (2, relation.getContext().getId());
		stmt.setString (3, nameA);
		stmt.setString (4, nameB);
		stmt.setInt    (5, relation.getType());
		stmt.setString (6, relation.getComment());
		stmt.setBoolean(7, relation.getValid());
		
		stmt.executeUpdate();
		ResultSet keys = stmt.getGeneratedKeys();
		
		if(keys.next()){
			relation.setId(keys.getLong(1));
		}
	}
	
	private int removeAllInstanceRelationsFromRelation(Relation relation) throws SQLException{
		
		PreparedStatement stmt = this.connection.prepareStatement(
				" delete from instance_relation " +
				"  where relation_id    = ? ");
			
			stmt.setLong   (1, relation.getId());

			return stmt.executeUpdate();
	}
	
	private int removeAllCardinalitiesFromRelation(Relation relation) throws SQLException{
		
		PreparedStatement stmt = this.connection.prepareStatement(
				" delete from cardinality " +
		"  where relation_id    = ? ");
		
		stmt.setLong   (1, relation.getId());
		
		return stmt.executeUpdate();
	}
	
	public int removeRelation(Relation relation) throws SQLException {
		return this.removeRelationLogically(relation);
	}
	
	
	private int removeRelationLogically(Relation relation) throws SQLException {

		PreparedStatement stmt = this.connection.prepareStatement(
			" update relation " +
			"    set valid = ? " +
			"  where id    = ? ");
		
		stmt.setBoolean(1, Boolean.FALSE);
		stmt.setLong   (2, relation.getId());

		return stmt.executeUpdate();
	
	}

	public HashMap<String, CompositionRelation> getCompositionRelations(Relation relation) throws SQLException {
		HashMap<String, CompositionRelation> crMap = new HashMap<String, CompositionRelation>();

		ResultSet res = this.getRelations(relation);
		
		while (res.next()){
			CompositionRelation ir = new CompositionRelation(res.getString("uri"));

			ir.setId     (res.getLong("id"));
			ir.setComment(res.getString("comment"));
			ir.setNameA  (res.getString("nameA"));
			ir.setNameB  (res.getString("nameB"));
			ir.setType   (res.getInt("type"));
			ir.setValid  (res.getBoolean("valid"));

			//Set cardinality with classes
			Map<String, Cardinality> cardinalityMap = getRelationCardinality(ir);
			ir.setCardinalityA(cardinalityMap.get("A"));
			ir.setCardinalityB(cardinalityMap.get("B"));

			ir.setInstanceRelationMapA(this.getAllRelationInstances(ir, "A"));
			ir.setInstanceRelationMapB(this.getAllRelationInstances(ir, "B"));
			
			crMap.put(ir.getUri(), ir);
		}
		
		return crMap;
	}
	
	private int saveRelationInstaceRelations(Relation relation) throws SQLException {
	
		for (InstanceRelation instanceRelation : relation.getInstanceRelationMapA().values()) {
			
			for (Instance associatedInstance : instanceRelation.getAllInstances().values()) {
				PreparedStatement stmt = this.connection.prepareStatement(
						" insert into instance_relation (" +
						                      " instance_id, " +
						                      " relation_id, " +
						                      " associated_instance_id, " +
						                      " side " +
						                      " ) " +
							         " values (?, ?, ?, ?)");
				
				stmt.setLong   (1, instanceRelation.getInstance().getId());
				stmt.setLong   (2, relation.getId());
				stmt.setLong   (3, associatedInstance.getId());
				stmt.setString (4, "A");
				
				stmt.executeUpdate();
			}
		}
		
		for (InstanceRelation instanceRelation : relation.getInstanceRelationMapB().values()) {
			
			for (Instance associatedInstance : instanceRelation.getAllInstances().values()) {
				PreparedStatement stmt = this.connection.prepareStatement(
						" insert into instance_relation (" +
						                      " instance_id, " +
						                      " relation_id, " +
						                      " associated_instance_id, " +
						                      " side " +
						                      " ) " +
							         " values (?, ?, ?, ?)");
				
				stmt.setLong   (1, instanceRelation.getInstance().getId());
				stmt.setLong   (2, relation.getId());
				stmt.setLong   (3, associatedInstance.getId());
				stmt.setString (4, "B");
				
				stmt.executeUpdate();
			}
		}
		return 1;
		
	}
	
	private int saveRelationCardinalities(Relation relation) throws SQLException {
		
		
		PreparedStatement stmt = this.connection.prepareStatement(
				" insert into cardinality (" +
				                      " class_id, " +
				                      " relation_id, " +
				                      " minCardinality, " +
				                      " maxCardinality, " +
				                      " type, " +
				                      " side " +
				                      " ) " +
					         " values (?, ?, ?, ?, ?, ?)");
		
		stmt.setLong   (1, relation.getCardinalityA().getMobiClass().getId());
		stmt.setLong   (2, relation.getId());
		stmt.setLong   (3, relation.getCardinalityA().getMinCardinality());
		stmt.setInt    (4, relation.getCardinalityA().getMaxCardinality());
		stmt.setInt    (5, relation.getCardinalityA().getType());
		stmt.setString (6, "A");
		
		stmt.executeUpdate();
		
		 stmt = this.connection.prepareStatement(
				" insert into cardinality (" +
				                      " class_id, " +
				                      " relation_id, " +
				                      " minCardinality, " +
				                      " maxCardinality, " +
				                      " type, " +
				                      " side " +
				                      " ) " +
					         " values (?, ?, ?, ?, ?, ?)");
		
		stmt.setLong   (1, relation.getCardinalityB().getMobiClass().getId());
		stmt.setLong   (2, relation.getId());
		stmt.setLong   (3, relation.getCardinalityB().getMinCardinality());
		stmt.setInt    (4, relation.getCardinalityB().getMaxCardinality());
		stmt.setInt    (5, relation.getCardinalityB().getType());
		stmt.setString (6, "B");
		
		stmt.executeUpdate();
		
		return 1;
		
	}
	
	public HashMap<String,EquivalenceRelation> getEquivalenceRelations(Relation relation) throws SQLException {
		HashMap<String,EquivalenceRelation> erMap = new HashMap<String,EquivalenceRelation>();

		ResultSet res = this.getRelations(relation);
		
		while (res.next()){
			EquivalenceRelation ir = new EquivalenceRelation(res.getString("uri"));

			ir.setId     (res.getLong("id"));
			ir.setComment(res.getString("comment"));
			ir.setType   (res.getInt("type"));
			ir.setValid  (res.getBoolean("valid"));

			//Set cardinality with classes
			Map<String, Cardinality> cardinalityMap = getRelationCardinality(ir);
			ir.setCardinalityA(cardinalityMap.get("A"));
			ir.setCardinalityB(cardinalityMap.get("B"));

			ir.setInstanceRelationMapA(this.getAllRelationInstances(ir, "A"));
			ir.setInstanceRelationMapB(this.getAllRelationInstances(ir, "B"));
			
			erMap.put(ir.getUri(), ir);
		}
		
		return erMap;
	}
	
	public HashMap<String,SymmetricRelation> getSymmetricRelations(Relation relation) throws SQLException {
		HashMap<String,SymmetricRelation> srMap = new HashMap<String,SymmetricRelation>();

		ResultSet res = this.getRelations(relation);
		
		while (res.next()){
			SymmetricRelation ir = new SymmetricRelation(res.getString("uri"));

			ir.setId     (res.getLong("id"));
			ir.setComment(res.getString("comment"));
			ir.setName   (res.getString("nameA"));
			ir.setType   (res.getInt("type"));
			ir.setValid  (res.getBoolean("valid"));

			//Set cardinality with classes
			Map<String, Cardinality> cardinalityMap = getRelationCardinality(ir);
			ir.setCardinalityA(cardinalityMap.get("A"));
			ir.setCardinalityB(cardinalityMap.get("B"));

			ir.setInstanceRelationMapA(this.getAllRelationInstances(ir, "A"));
			ir.setInstanceRelationMapB(this.getAllRelationInstances(ir, "B"));
			
			srMap.put(ir.getUri(), ir);
		}
		
		return srMap;
	}
	
	public HashMap<String,InheritanceRelation> getInheritanceRelations(Relation relation) throws SQLException {
		HashMap<String,InheritanceRelation> irMap = new HashMap<String,InheritanceRelation>();

		ResultSet res = this.getRelations(relation);
		
		while (res.next()){
			InheritanceRelation ir = new InheritanceRelation(res.getString("uri"));

			ir.setId     (res.getLong("id"));
			ir.setComment(res.getString("comment"));
			ir.setType   (res.getInt("type"));
			ir.setValid  (res.getBoolean("valid"));

			//Set cardinality with classes
			Map<String, Cardinality> cardinalityMap = getRelationCardinality(ir);
			ir.setCardinalityA(cardinalityMap.get("A"));
			ir.setCardinalityB(cardinalityMap.get("B"));

			ir.setInstanceRelationMapA(this.getAllRelationInstances(ir, "A"));
			ir.setInstanceRelationMapB(this.getAllRelationInstances(ir, "B"));
			
			irMap.put(ir.getUri(), ir);
		}
		
		return irMap;
	}
	
	private ResultSet getRelations(Relation relation) throws SQLException {
		int position = 1;
		
		String sql = " select r.id, " +
					 "        r.uri, " +
					 "        r.comment," +
					 "        r.type, " +
					 "        r.nameA, " +
					 "        r.nameB, " +
					 "        c.uri as context_uri, " +
					 "        r.valid " +
					 "   from relation r " +
					 "        inner join context c on c.id = r.context_id " +
					 "  where 1 = 1 ";
		System.out.println("Teste: "+relation.getType());
					 if (relation.getType() > 0) sql += " and r.type = ? ";
					 
					 if (relation.getContext() != null) {
						 if (relation.getContext().getId()  != null) sql += " and c.id  = ? ";
						 if (relation.getContext().getUri() != null) sql += " and c.uri = ? ";
					 }

					 if (relation.getId()  != null) sql += " and r.id  = ? ";
					 if ((relation.getUri() != null) && (!relation.getUri().equalsIgnoreCase("")))  sql += " and r.uri = ? ";

//					 sql += "  order by context_uri, type, uri ";
					 sql += "  order by c.uri, type, r.uri ";
		PreparedStatement stmt = this.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

		if(relation != null ){
			//Filter Parameters
			if (relation.getType() > 0) stmt.setInt(position++, relation.getType());
	
			if (relation.getContext() != null) {
				if(relation.getContext().getId()  != null) stmt.setLong  (position++, relation.getContext().getId());
				if(relation.getContext().getUri() != null) stmt.setString(position++, relation.getContext().getUri());
			}
		
			if (relation.getId()  != null) stmt.setLong  (position++, relation.getId());
			if ((relation.getUri() != null) && (!relation.getUri().equalsIgnoreCase(""))) stmt.setString(position++, relation.getUri());
		}
		
		//Execution
		return stmt.executeQuery();

	}
	
	public Map<String, Cardinality> getRelationCardinality(Relation relation) throws SQLException {
		
		Map<String, Cardinality> cardinalityMap = new HashMap<String, Cardinality>();

		//Get all instances of this side
		String sql = " select c.type, " +
					 "        c.minCardinality, " +
					 "        c.maxCardinality, " +
					 "        c.side, " +
					 "        cl.id  as class_id, " +
					 "        cl.uri, " +
					 "        cl.comment, " +
					 "        cl.valid " +
					 "   from cardinality c " +
					 "        inner join class cl on cl.id = c.class_id " +
					 "  where  1 = 1 " +
					 "    and c.relation_id  = ? "+
					 "  order by c.side ";

		PreparedStatement stmt = this.connection.prepareStatement(sql);

		stmt.setLong(1, relation.getId());

		ResultSet res = stmt.executeQuery();

		while (res.next()){

			Cardinality c = new Cardinality(res.getInt("type"));

			c.setMinCardinality(res.getInt("minCardinality"));
			c.setMaxCardinality(res.getInt("maxCardinality"));

			Class cl = new Class(res.getString("uri"));
			
			cl.setId     (res.getLong("class_id"));
			cl.setComment(res.getString("comment"));
			cl.setValid  (res.getBoolean("valid"));
			
			c.setMobiClass(cl);
			
			cardinalityMap.put(res.getString("side"), c);
		}

		return cardinalityMap;
	}

	public Map<String, InstanceRelation> getAllRelationInstances(Relation relation, String side) throws SQLException {
		int position = 1;
		
		if ((relation == null) || ((relation.getId() == null) && (relation.getUri() == null))) return null;
		
		Map<String, InstanceRelation> instancesRelation = new HashMap<String, InstanceRelation>();
		
		//Get all instances of this side
		String sql = " select i.id, " +
					 "        i.uri, " +
					 "        i.comment, " +
					 "        i.valid," +
					 "        ir.side " +
					 "   from instance_relation ir " +
					 "        inner join relation r on r.id = ir.relation_id " +
					 "        left join instance  i on i.id = ir.instance_id " +
					 "  where 1 = 1 " +
					 "    and ir.side = ? ";
		
		if(relation.getId()  != null) sql += " and r.id  = ? ";
		if((relation.getUri() != null) && (!relation.getUri().equalsIgnoreCase(""))) sql += " and r.uri = ? ";
		
		sql += "  order by ir.side, i.uri ";
		
		PreparedStatement stmt = this.connection.prepareStatement(sql);
		
		//Parameters
		stmt.setString(position++, side);
		if(relation.getId()  != null) stmt.setLong  (position++, relation.getId());
		if((relation.getUri() != null) && (!relation.getUri().equalsIgnoreCase(""))) stmt.setString(position++, relation.getUri());
		
		//Execution
		ResultSet res = stmt.executeQuery();
		
		while (res.next()){

			Instance i = new Instance(res.getString("uri"));

			i.setId     (res.getLong("id"));
			i.setComment(res.getString("comment"));
			i.setValid  (res.getBoolean("valid"));

			InstanceRelation ir = new InstanceRelation();
			ir.setInstance(i);
			
			instancesRelation.put(i.getUri(), ir);
		}
	
		for (InstanceRelation ir : instancesRelation.values()) {
			ir.setInstanceMap(this.getAllRelationsBetweenInstances(relation, ir.getInstance(), side));
		}

		return instancesRelation;
	}
	
	private Map<String, Instance> getAllRelationsBetweenInstances(Relation relation, Instance instance, String side) throws SQLException {
		
		Map<String, Instance> instances = new HashMap<String, Instance>();
		//Get all instances of this side
		String sql = " select i.id, " +
					 "        i.uri, " +
					 "        i.comment, " +
					 "        i.valid " +
					 "   from instance i " +
					 "        left join instance_relation ir on i.id = ir.associated_instance_id " +
					 "  where 1 = 1 " +
					 "    and ir.relation_id = ? "+
					 "    and ir.instance_id = ? "+
					 "    and ir.side        = ? "+
					 "  order by i.uri ";
		
		PreparedStatement stmt = this.connection.prepareStatement(sql);
		
		stmt.setLong  (1, relation.getId());
		stmt.setLong  (2, instance.getId());
		stmt.setString(3, side);
		
		ResultSet res = stmt.executeQuery();
		
		while (res.next()){
			
			Instance i = new Instance(res.getString("uri"));
			
			i.setId     (res.getLong("id"));
			i.setComment(res.getString("comment"));
			i.setValid  (res.getBoolean("valid"));

			instances.put(res.getString("uri"), i);
		}
		
		return instances;
	}

}