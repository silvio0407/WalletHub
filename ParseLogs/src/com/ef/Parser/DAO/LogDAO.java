package com.ef.Parser.DAO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ef.Parser.database.ConnectionFactory;
import com.ef.Parser.entity.Log;

public class LogDAO {

	private ConnectionFactory connection;
	
    public LogDAO(){    
        this.connection = ConnectionFactory.getInstance();    
    }    
    public void salvar(Log log){    
            String sql = "INSERT INTO log_information(data_request,ip,description_request) VALUES(?,?,?)";    
            try {    
                PreparedStatement stmt = connection.getConnection().prepareStatement(sql);    
                stmt.setString(1, log.getRequestDate());    
                stmt.setString(2, log.getIp());    
                stmt.setString(3, log.getDescriptionRequest());    
                 stmt.execute();    
                stmt.close();    
            } catch (SQLException u) {    
                throw new RuntimeException(u);    
        }    
    }    
}
