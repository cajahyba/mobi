package mobi.extension.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import mobi.core.concept.Class;
import mobi.core.concept.Context;

public class ClassDAO {

	private Connection connection;

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public HashMap<String, Class> getContextClasses(Context context) throws SQLException{
		HashMap<String, Class> classMap = new HashMap<String, Class>();
		int position = 1;
		ResultSet res;
		PreparedStatement stmt;


			String sql =	
				" select c.id, " +
				"        c.uri, " +
				"        c.comment, " +
				"        c.valid " +
				"   from class c inner join cardinality ca on c.id   = ca.class_id " +
				"                inner join relation r     on r.id   = ca.relation_id "+
				"                inner join context ctx    on ctx.id = r.context_id "+
				"  where 1 = 1 ";
				 if (context != null) {
					 if (context.getId()  != null) sql += " and ctx.id  = ? ";
					 if (context.getUri() != null) sql += " and ctx.uri = ? ";
				 }

		stmt = this.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		if (context != null) {
			 if (context.getId()  != null) stmt.setLong(position++,   context.getId());
			 if (context.getUri() != null) stmt.setString(position++, context.getUri());
		 }
		
		
		res = stmt.executeQuery();


		while (res.next()) {
			Class c = new Class(res.getString("uri"));
			c.setId(res.getLong("id"));
			c.setComment(res.getString("comment"));
			c.setValid(res.getBoolean("valid"));
			
			classMap.put(c.getUri(), c);
		}

		return classMap;

	}
	
	public ArrayList<Class> getClasses() throws SQLException{

		ResultSet res;
		PreparedStatement stmt;

		ArrayList<Class> classList = new ArrayList<Class>();

		stmt = this.connection.prepareStatement(
				" select id, " +
				"        uri, " +
				"        comment, " +
				"        valid " +
				"   from class " +
				"  order by uri ");

		res = stmt.executeQuery();


		while (res.next()) {
			Class c = new Class(res.getString("uri"));
			c.setId(res.getLong("id"));
			c.setComment(res.getString("comment"));
			c.setValid(res.getBoolean("valid"));

			classList.add(c);
		}

		return classList;

	}
	
	public Class getClass(Class mobiClass) throws SQLException{

		ResultSet res;
		PreparedStatement stmt;
		int position = 1;
		
		String sql = " select id, " +
					 "        uri, " +
					 "        comment, " +
					 "        valid " +
					 "   from class " +
					 "  where 1 = 1 ";
					
					 if (mobiClass.getUri() != null) sql += " and uri = ? ";
					 if (mobiClass.getId()  != null) sql += " and id  = ?  ";
					 
					 sql +=  "  order by uri ";
		
		stmt = this.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

		if (mobiClass.getUri() != null) stmt.setString(position++, mobiClass.getUri());
		if (mobiClass.getId()  != null) stmt.setLong(position++, mobiClass.getId());
		
		res = stmt.executeQuery();

		if (res.first()) {
			System.out.println("achou");
			Class c = new Class(res.getString("uri"));
			c.setId(res.getLong("id"));
			c.setComment(res.getString("comment"));
			c.setValid(res.getBoolean("valid"));
			return c;
		}

		return null;

	}

	private void saveNewClass(Class mobiClass) throws SQLException {
		
		PreparedStatement stmt = this.connection.prepareStatement(
				" insert into class  (" +
				                      " uri, " +
				                      " comment, " +
				                      " valid " +
				                      " ) " +
					         " values (?, ?, ?) ", Statement.RETURN_GENERATED_KEYS);
		
		stmt.setString (1, mobiClass.getUri());
		stmt.setString (2, mobiClass.getComment());
		stmt.setBoolean(3, mobiClass.getValid());
		stmt.executeUpdate();
		ResultSet keys = stmt.getGeneratedKeys();
		
		if(keys.next()){
			mobiClass.setId(keys.getLong(1));
		}
			
	}

	private void updateClass(Class mobiClass) throws SQLException {

		PreparedStatement stmt = this.connection.prepareStatement(
				" update class " +
				"    set uri     = ?, " +
				"        comment = ?, " +
				"        valid   = ? " +
				"  where id      = ? ");
		
		stmt.setString (1, mobiClass.getUri());
		stmt.setString (2, mobiClass.getComment());
		stmt.setBoolean(3, mobiClass.getValid());
		stmt.setLong   (4, mobiClass.getId());

		stmt.executeUpdate();

}
	
	public void saveClass(Class mobiClass) throws SQLException {

			if(mobiClass.getId() == null) {
				this.saveNewClass(mobiClass);
			} else {
				this.updateClass(mobiClass);
			}
	}
	

	public int removeClass(Class mobiClass) throws SQLException {

		return this.removeClassLogically(mobiClass);
	
	}
	
	
	private int removeClassLogically(Class mobiClass) throws SQLException {

		PreparedStatement stmt = this.connection.prepareStatement(
			" update class " +
			"    set valid = ? " +
			"  where id    = ? ");
		
		stmt.setBoolean(1, Boolean.FALSE);
		stmt.setLong   (2, mobiClass.getId());

		return stmt.executeUpdate();
	
	}

}