package com.cloud.provider.api.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aerospike.client.Key;
import com.aerospike.client.command.Buffer;
import com.cloud.provider.api.Repository.ServerRepository;
import com.cloud.provider.api.Services.ServerService;
import com.cloud.provider.api.model.Server;

@RestController
@RequestMapping("/test")
public class Controller {

	@Autowired
	public ServerService serverService;
	@Autowired
	public ServerRepository serverRepository;

	@GetMapping("servers")
	public List<Server> getAllServers() {

		return serverService.getAllServers();
	}
	
	/*@GetMapping("create/{size}")
	public Server createServer(@PathVariable int size) {
		
		 Server server = new Server();	
			LocalDateTime myObj = LocalDateTime.now();
			Key key = new Key("test", "test", myObj.toString());
			server.setKey(Buffer.bytesToHexString(key.digest));
			server.setState("create");
			server.setRam(100);
			server.setFreeMemory(100-size);
			serverRepository.save(server);
		
		return server;
	}
	
	
	
	@GetMapping("best/{size}")

	public Server bestServer(@PathVariable int size) {

		return serverRepository.findByFreeMemoryGreaterThanEqualAndStateOrderByFreeMemoryAsc(size, "create");
	}*/

	
	
	

	@GetMapping("allocate/{size}")

	public Server allocateServer(@PathVariable int size) {

		return serverService.allocateServer(size);
	}

}
