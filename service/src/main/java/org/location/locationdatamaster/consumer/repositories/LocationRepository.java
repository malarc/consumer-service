package org.location.locationdatamaster.consumer.repositories;

import org.location.locationdatamaster.consumer.domains.LocationMaster;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LocationRepository extends ReactiveCrudRepository<LocationMaster, Long> {



}
