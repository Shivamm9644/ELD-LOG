package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.MainTerminalMaster;

@Repository
public interface MainTerminalMasterRepo extends MongoRepository<MainTerminalMaster, String> {
	
	@Query(value="{ 'mainTerminalId' : ?0 }")
	public MainTerminalMaster findByMainTerminalId(Integer mainTerminalId);
	
	@Query(value="{ 'mainTerminalId' : ?0}")
	public List<MainTerminalMaster> findAndViewByMainTerminalId(Integer mainTerminalId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<MainTerminalMaster> findAllMainTerminals(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $mainTerminalId }}}" })
	public Object findMaxIdInMainTerminalMaster();
	
	@Query(value="{'mainTerminalId' : ?0}", delete = true)
	public MainTerminalMaster DeleteMainTerminalById(Integer mainTerminalId);

	@Query(value="{'clientId' : ?0}", delete = true)
	long DeleteMainTerminalMasterByClientId(long clientId);
	
}
