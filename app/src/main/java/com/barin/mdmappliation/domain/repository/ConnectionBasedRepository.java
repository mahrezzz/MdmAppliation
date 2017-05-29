package com.barin.mdmappliation.domain.repository;

import java.util.List;
import com.barin.mdmappliation.application.exception.MdmException;
import com.barin.mdmappliation.data.connection.ServerConnection;
import com.barin.mdmappliation.domain.model.MdmUserCertificate;

/**
 * Created by barin on 4/16/2016.
 */
public interface ConnectionBasedRepository {

  void fillParameters(List<String> parameters) throws MdmException;

  ServerConnection getConnection(MdmUserCertificate mdmUserCertificate);
}
