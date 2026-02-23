package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.LanguageMaster;

@Repository
public interface LanguageMasterRepo extends MongoRepository<LanguageMaster, String> {
	
	@Query(value="{ 'languageId' : ?0 }")
	public LanguageMaster findByLanguageId(Integer languageId);
	
	@Query(value="{ 'languageId' : ?0}")
	public List<LanguageMaster> findAndViewByLanguageId(Integer languageId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<LanguageMaster> findAllLanguages(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $languageId }}}" })
	public Object findMaxIdInLanguageMaster();

	@Query(value="{'languageId' : ?0}", delete = true)
	public LanguageMaster DeleteLanguageById(Integer languageId);
	
}
