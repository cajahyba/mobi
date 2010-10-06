package mobi.extension.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import mobi.core.concept.Context;
import mobi.core.concept.Instance;

public class InstanceDAO {

	private Connection connection;

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public HashMap<String, Instance> getContextInstances(Context context) throws SQLException{
		HashMap<String, Instance> instanceMap = new HashMap<String, Instance>();

		int position = 1;
		ResultSet res;
		PreparedStatement stmt;


		String sql =	
			" select i.id, " +
			"        i.uri, " +
			"        i.comment, " +
			"        i.valid " +
			"   from instance i inner join instance_relation ir on i.id   = ir.instance_id " +
			"                   inner join relation r           on r.id   = ir.relation_id "+
			"                   inner join context ctx          on ctx.id = r.context_id "+
			"  where 1 = 1 ";
			 if (context != null) {
				 if (context.getId()  != null) sql += " and ctx.id  = ? ";
				 if (context.getUri() != null) sql += " and ctx.uri = ? ";
			 }

		stmt = this.connection.prepareStatement(sql);
		if (context != null) {
			 if (context.getId()  != null) stmt.setLong(position++,   context.getId());
			 if (context.getUri() != null) stmt.setString(position++, context.getUri());
		 }
		
		
		res = stmt.executeQuery();


		while (res.next()) {
			Instance i = new Instance(res.getString("uri"));
			i.setId(res.getLong("id"));
			i.setComment(res.getString("comment"));
			i.setValid(res.getBoolean("valid"));
			
			instanceMap.put(i.getUri(), i);
		}

		return instanceMap;

	}
	
	public ArrayList<Instance> getInstances() throws SQLException{

		ResultSet res;
		PreparedStatement stmt;

		ArrayList<Instance> instanceList = new ArrayList<Instance>();

		stmt = this.connection.prepareStatement(
				" select id, " +
				"        uri, " +
				"        comment, " +
				"        valid " +
				"   from instance " +
				"  order by uri ");

		res = stmt.executeQuery();


		while (res.next()) {
			Instance c = new Instance(res.getString("uri"));
			c.setId(res.getLong("id"));
			c.setComment(res.getString("comment"));
			c.setValid(res.getBoolean("valid"));

			instanceList.add(c);
		}

		return instanceList;

	}
	
	public Instance getInstance(Instance instance) throws SQLException{

		ResultSet res;
		PreparedStatement stmt;
		int position = 1;
		
		String sql = " select id, " +
					 "        uri, " +
					 "        comment, " +
					 "        valid " +
					 "   from instance " +
					 "  where 1 = 1 ";
					
					 if (instance.getUri() != null) sql += " and uri = ? ";
					 if (instance.getId()  != null) sql += " and id  = ?  ";
					 
					 sql +=  "  order by uri ";
		
		stmt = this.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

		if (instance.getUri() != null) stmt.setString(position++, instance.getUri());
		if (instance.getId()  != null) stmt.setLong(position++, instance.getId());
		
		res = stmt.executeQuery();

		if (res.first()) {
			System.out.println("achou");
			Instance c = new Instance(res.getString("uri"));
			c.setId(res.getLong("id"));
			c.setComment(res.getString("comment"));
			c.setValid(res.getBoolean("valid"));
			return c;
		}

		return null;

	}

	private void saveNewInstance(Instance instance) throws SQLException {
		
		PreparedStatement stmt = this.connection.prepareStatement(
				" insert into instance (" +
				                      " uri, " +
				                      " comment, " +
				                      " valid " +
				                      " ) " +
					         " values (?, ?, ?) ", Statement.RETURN_GENERATED_KEYS);
		
		stmt.setString (1, instance.getUri());
		stmt.setString (2, instance.getComment());
		stmt.setBoolean(3, instance.getValid());
		stmt.executeUpdate();
		ResultSet keys = stmt.getGeneratedKeys();
		
		if(keys.next()){
			instance.setId(keys.getLong(1));
		}
			
	}

	private void updateInstance(Instance instance) throws SQLException {

		PreparedStatement stmt = this.connection.prepareStatement(
				" update instance " +
				"    set uri     = ?, " +
				"        comment = ?, " +
				"        valid   = ? " +
				"  where id      = ? ");
		
		stmt.setString (1, instance.getUri());
		stmt.setString (2, instance.getComment());
		stmt.setBoolean(3, instance.getValid());
		stmt.setLong   (4, instance.getId());

		stmt.executeUpdate();

}
	
	public void saveInstance(Instance instance) throws SQLException {

			if(instance.getId() == null) {
				this.saveNewInstance(instance);
			} else {
				this.updateInstance(instance);
			}
	}
	

	public int removeInstance(Instance instance) throws SQLException {

		return this.removeInstanceLogically(instance);
	
	}
	
	
	private int removeInstanceLogically(Instance instance) throws SQLException {

		PreparedStatement stmt = this.connection.prepareStatement(
			" update instance " +
			"    set valid = ? " +
			"  where id    = ? ");
		
		stmt.setBoolean(1, Boolean.FALSE);
		stmt.setLong   (2, instance.getId());

		return stmt.executeUpdate();
	
	}

}