package com.cloud.provider.api.Repository;

import org.springframework.data.aerospike.repository.AerospikeRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.provider.api.model.Server;

import javax.persistence.LockModeType;

public interface ServerRepository extends AerospikeRepository<Server, Integer> {

	//@Transactional(isolation = Isolation.SERIALIZABLE)
    	Server findByFreeMemoryGreaterThanEqualAndState(int size, String state);

		
	
	
}
