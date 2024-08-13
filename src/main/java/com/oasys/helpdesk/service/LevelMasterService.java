package com.oasys.helpdesk.service;

import com.oasys.helpdesk.request.LevelMasterDto;
import com.oasys.helpdesk.utility.GenericResponse;

public interface LevelMasterService {

    GenericResponse getAllLevel();

    GenericResponse getLevelById(Long id);

    GenericResponse createLevel(LevelMasterDto levelRequestDto);
}