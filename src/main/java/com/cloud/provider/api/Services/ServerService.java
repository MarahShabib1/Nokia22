package com.cloud.provider.api.Services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineConfig;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.aerospike.client.Key;
import com.aerospike.client.command.Buffer;
import com.cloud.provider.api.Repository.ServerRepository;
import com.cloud.provider.api.model.Server;

@Service
@EnableTransactionManagement
public class ServerService {

	@Autowired
    StateMachineFactory<String, String> factory;
	
	@Autowired
	public ServerRepository serverRepository;

	public Server createServer(int size) {
		 StateMachine<String,String> stateMachine = factory.getStateMachine();
		 Server server = new Server();
		 System.out.println("7");
		 stateMachine.stop();
		 stateMachine.start();
		 System.out.println("6");
		if (stateMachine.getState().getId() == "create") {    // to make sure that the first request will allocate the memory first
			 System.out.println("5");
			LocalDateTime myObj = LocalDateTime.now();
			Key key = new Key("test", "test", myObj.toString());
			server.setKey(Buffer.bytesToHexString(key.digest));
			server.setState("create");
			server.setRam(100);
			server.setFreeMemory(100-size);
			serverRepository.save(server);
		}
	
		stateMachine.sendEvent("wait");
		
		if (stateMachine.getState().getId() == "active") {
			return server;
		}
		
		return null;
	}

	public List<Server> getAllServers() {

		List<Server> servers = (List<Server>) serverRepository.findAll();

		return servers;
	}


	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Server allocateServer(int size) {

		Server server = serverRepository.findByFreeMemoryGreaterThanEqualAndStateOrderByFreeMemoryAsc(size,"active");  // to make sure that the sever is not in creating state
		if (server != null  ) {
			server.setFreeMemory(server.getFreeMemory() - size);
			serverRepository.save(server);

		} else {

			 server = serverRepository.findByFreeMemoryGreaterThanEqualAndStateOrderByFreeMemoryAsc(size,"create");  // if another request come while creating a new one and there is already no space it will wait to make sure that the new server may have space too 
			 System.out.println("1");
			 if (server != null  ) {
				 server.setFreeMemory(server.getFreeMemory() - size);
			 	serverRepository.save(server);
			 	 System.out.println("2");
				 while(true) {
					 if(server.getState()=="active") {
						 System.out.println("3");
						break;	 
					 }
				 }
 
			 }else {
				 System.out.println("4");
				server = createServer(size);		
			 } 
		}
		return server;
	}

}
