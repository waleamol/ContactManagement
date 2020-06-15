package com.assignment.Contact.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.assignment.Contact.modal.Sequence;
import com.assignment.Contact.security.SequenceException;


@Repository
public class SequenceDALImpl implements SequenceDAL {

	@Autowired
	public MongoTemplate mongoTemplate;

	@Override
	public long getNextSequenceId(String key) {
		Sequence seqId = mongoTemplate.findAndModify(new Query(Criteria.where("_id").is(key)),
				new Update().inc("seq", 1), new FindAndModifyOptions().returnNew(true).upsert(true), Sequence.class);
		if (seqId == null) {
			throw new SequenceException("Unable to get sequence id for key : " + key);
		}
		return seqId.getSeq();

	}

}