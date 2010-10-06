package mobi.extension.db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {

	private static Connection connection;

	private ConnectionFactory(){}
	
	public static Connection getConnection() throws SQLException {
		try {
			if ((ConnectionFactory.connection == null) || (ConnectionFactory.connection.isClosed())){
//				Class.forName("org.postgresql.Driver");
//				return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","mobi");
//				Class.forName("com.mysql.jdbc.Driver");
//				return DriverManager.getConnection("jdbc:mysql://localhost:3306/mobi?relaxAutoCommit=true","root","123456");
				Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
				return DriverManager.getConnection("jdbc:derby:mobiDB;create=true;user=me;password=mine");
			}
			
			return ConnectionFactory.connection;
		}
		catch (ClassNotFoundException e) {
			throw new SQLException(e.getMessage());
		}
	}
	
	public static void main(String[] args) throws SQLException {
		Statement stmt = ConnectionFactory.getConnection().createStatement();
		String sql[] = {"CREATE  TABLE context ( "+
				"  id BIGINT NOT NULL primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "+
				"  uri VARCHAR(256) , "+
				"  comment VARCHAR(500) , "+
				"  UNIQUE(uri), "+
				"  valid integer DEFAULT 1) ",

				"CREATE  TABLE class ( "+
				"  id BIGINT NOT NULL primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "+
				"  uri VARCHAR(256) , "+
				"  comment VARCHAR(500) , "+
				"  UNIQUE(uri), "+
				"  valid integer DEFAULT 1) ",

				"CREATE  TABLE instance ( "+
				"  id BIGINT NOT NULL primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "+
				"  uri VARCHAR(256) , "+
				"  comment VARCHAR(500) , "+
				"  UNIQUE(uri), "+
				"  valid integer DEFAULT 1 ) ",

				"CREATE  TABLE relation ( "+
				"  id BIGINT NOT NULL primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "+
				"  context_id BIGINT NOT NULL, "+
				"  uri VARCHAR(256) , "+
				"  nameA VARCHAR(50) , "+
				"  nameB VARCHAR(50) , "+
				"  comment VARCHAR(500) , "+
				"  type integer , "+
				"  valid integer DEFAULT 1, "+
				"  UNIQUE(uri), "+
				"  CONSTRAINT fk_relation_context_id "+
				"    FOREIGN KEY (context_id) "+
				"    REFERENCES context (id) "+
				"    ON DELETE NO ACTION "+
				"    ON UPDATE NO ACTION) ",

				"CREATE  TABLE instance_relation ( "+
				"  instance_id BIGINT NOT NULL, "+
				"  relation_id BIGINT NOT NULL, "+
				"  associated_instance_id BIGINT NOT NULL , "+
				"  side CHAR(1)  NOT NULL, "+
				"  PRIMARY KEY (relation_id, instance_id, associated_instance_id, side), "+
				"  CONSTRAINT fk_instance_relation_instance_id "+
				"    FOREIGN KEY (instance_id ) "+
				"    REFERENCES instance (id ) "+
				"    ON DELETE NO ACTION "+
				"    ON UPDATE NO ACTION, "+
				"  CONSTRAINT fk_instance_relation_relation_id "+
				"    FOREIGN KEY (relation_id ) "+
				"    REFERENCES relation (id ) "+
				"    ON DELETE NO ACTION "+
				"    ON UPDATE NO ACTION, "+
				"  CONSTRAINT fk_instance_relation_instance1 "+
				"    FOREIGN KEY (associated_instance_id ) "+
				"    REFERENCES instance (id ) "+
				"    ON DELETE NO ACTION "+
				"    ON UPDATE NO ACTION) ",

				"CREATE  TABLE cardinality ( "+
				"  relation_id BIGINT NOT NULL, "+
				"  class_id BIGINT NOT NULL, "+
				"  type INT , "+
				"  minCardinality INT , "+
				"  maxCardinality INT , "+
				"  side CHAR(1)  NOT NULL, "+
				"  PRIMARY KEY (relation_id, class_id, side), "+
				"  CONSTRAINT fk_cartinality_relation1 "+
				"    FOREIGN KEY (relation_id ) "+
				"    REFERENCES relation (id ) "+
				"    ON DELETE NO ACTION "+
				"    ON UPDATE NO ACTION, "+
				"  CONSTRAINT fk_cartinality_class1 "+
				"    FOREIGN KEY (class_id ) "+
				"    REFERENCES class (id ) "+
				"    ON DELETE NO ACTION "+
				"    ON UPDATE NO ACTION) ",

				"CREATE  TABLE instance_class ( "+
				"  instance_id BIGINT NOT NULL, "+
				"  class_id BIGINT NOT NULL, "+
				"  PRIMARY KEY (instance_id, class_id), "+
				"  CONSTRAINT fk_class_has_instance_class1 "+
				"    FOREIGN KEY (class_id ) "+
				"    REFERENCES class (id ) "+
				"    ON DELETE NO ACTION "+
				"    ON UPDATE NO ACTION, "+
				"  CONSTRAINT fk_class_has_instance_instance1 "+
				"    FOREIGN KEY (instance_id ) "+
				"    REFERENCES instance (id ) "+
				"    ON DELETE NO ACTION "+
				"    ON UPDATE NO ACTION) "};
		
		for (int i = 0; i < sql.length; i++) {
			stmt.execute(sql[i]);
		}
		System.out.println("aqui");
	}
}