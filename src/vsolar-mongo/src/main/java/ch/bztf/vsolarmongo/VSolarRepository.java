package ch.bztf.vsolarmongo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VSolarRepository extends MongoRepository<VSolarENV, ObjectId> {
    
}
