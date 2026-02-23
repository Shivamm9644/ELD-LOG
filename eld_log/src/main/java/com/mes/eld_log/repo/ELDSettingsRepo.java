package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.ELDSettings;

@Repository
public interface ELDSettingsRepo extends MongoRepository<ELDSettings, String> {
	
	@Query(value="{'settingId' : ?0}", delete = true)
	public ELDSettings DeleteSettingById(Integer settingId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $settingId }}}" })
	public Object findMaxIdInELDSettings();
	
	@Query(value="{ 'settingId' : ?0}")
	public List<ELDSettings> findAndViewBySettingId(Integer settingId);
	

}
