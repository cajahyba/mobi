package mobi.extension.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import mobi.core.concept.Context;

public class ContextDAO {

	private Connection connection;

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public ArrayList<Context> getContexts() throws SQLException{

		ResultSet res;
		PreparedStatement stmt;

		ArrayList<Context> contextList = new ArrayList<Context>();

		stmt = this.connection.prepareStatement(
				" select id, " +
				"        uri, " +
				"        comment, " +
				"        valid " +
				"   from context " +
				"  order by uri ");

		res = stmt.executeQuery();


		while (res.next()) {
			Context c = new Context(res.getString("uri"));
			c.setId(res.getLong("id"));
			c.setComment(res.getString("comment"));
			c.setValid(res.getBoolean("valid"));

			contextList.add(c);
		}

		return contextList;

	}
	
	public Context getContext(Context context) throws SQLException{

		ResultSet res;
		PreparedStatement stmt;
		int position = 1;
		
		String sql = " select id, " +
					 "        uri, " +
					 "        comment, " +
					 "        valid " +
					 "   from context " +
					 "  where 1 = 1 ";
					
					 if (context.getUri() != null) sql += "    and uri = ? ";
					 if (context.getId()  != null) sql += "    and id  = ?  ";
					 
					 sql +=  "  order by uri ";
		
		stmt = this.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

		if (context.getUri() != null) stmt.setString(position++, context.getUri());
		if (context.getId()  != null) stmt.setLong(position++, context.getId());
		
		res = stmt.executeQuery();

		if (res.first()) {
			Context c = new Context(res.getString("uri"));
			c.setId(res.getLong("id"));
			c.setComment(res.getString("comment"));
			c.setValid(res.getBoolean("valid"));
			return c;
		}

		return null;

	}

	private void saveNewContext(Context context) throws SQLException {

		PreparedStatement stmt = this.connection.prepareStatement(
				" insert into context (" +
				                      " uri, " +
				                      " comment, " +
				                      " valid " +
				                      " ) " +
					         " values (?, ?, ?) ", Statement.RETURN_GENERATED_KEYS);
		
		stmt.setString (1, context.getUri());
		stmt.setString (2, context.getComment());
		stmt.setBoolean(3, context.getValid());
		
		stmt.executeUpdate();
		ResultSet keys = stmt.getGeneratedKeys();
		
		if(keys.next()){
			context.setId(keys.getLong(1));
		}
			
	}

	private void updateContext(Context context) throws SQLException {

		PreparedStatement stmt = this.connection.prepareStatement(
				" update context " +
				"    set uri     = ?, " +
				"        comment = ?, " +
				"        valid   = ? " +
				"  where id      = ? ");
		
		stmt.setString (1, context.getUri());
		stmt.setString (2, context.getComment());
		stmt.setBoolean(3, context.getValid());
		stmt.setLong   (4, context.getId());

		stmt.executeUpdate();

	}
	
	public void saveContext(Context context) throws SQLException {
		this.removeContextFisically();
			if(context.getId() == null) {
				this.saveNewContext(context);
			} else {
				this.updateContext(context);
			}
	}
	

	public int removeContext(Context context) throws SQLException {

		return this.removeContextLogically(context);
	
	}
	
	private int removeContextLogically(Context context) throws SQLException {

		PreparedStatement stmt = this.connection.prepareStatement(
			" update context " +
			"    set valid = ? " +
			"  where id    = ? ");
		
		stmt.setBoolean(1, Boolean.FALSE);
		stmt.setLong   (2, context.getId());

		return stmt.executeUpdate();
	
	}
	
	private int removeContextFisically() throws SQLException {

		String sql[]={
				" delete from instance_relation ",
				" delete from instance_class ",
				" delete from cardinality ",
				" delete from relation ",
				" delete from instance ",
				" delete from class ",
				" delete from context "
		};
		for (int i = 0; i < sql.length; i++) {
			PreparedStatement stmt = this.connection.prepareStatement(sql[i]);
			stmt.executeUpdate();
		}
		
		return 1;
	
	}

}