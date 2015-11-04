package mining.main;

import java.sql.Connection;

import java.util.Dictionary;

import mining.dbconnection.DBConnection;
import mining.graph.Entity;
import mining.service.DictionaryService;

public class MainProgram {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Entity en = DictionaryService.buildEntity("Sun");
	}
}
