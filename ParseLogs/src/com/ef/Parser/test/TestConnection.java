package com.ef.Parser.test;

import com.ef.Parser.database.ConnectionFactory;

public class TestConnection {

	public static void main(String[] args) {

		ConnectionFactory conn = ConnectionFactory.getInstance();
		
		conn.getConnection();
	}

}
