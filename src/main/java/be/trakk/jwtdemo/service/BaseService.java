package be.trakk.jwtdemo.service;

import java.util.List;

public interface BaseService<TDTO, TFORM, TID> {

    List<TDTO> getAll();

    TDTO getOne(TID id);

    boolean insert(TFORM form);

    boolean delete(TID id);

    TDTO update(TFORM form, TID id);

}
